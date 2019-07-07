package com.gmail.luxdevpl.fbot.listeners;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.basic.enums.RegisterCause;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.event.IEventHandler;
import com.gmail.luxdevpl.fbot.impl.basic.ClientImpl;
import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClientJoinServerListener implements IEventHandler<ClientJoinEvent> {

    @Override
    public void handleEvent(ClientJoinEvent event, IBotWrapper bot) {
        //Checking if the client is an regular client.
        if(event.getClientType() == 0) {
            ExtendedConfiguration extendedConfiguration = bot.getExtendedConfiguration();

            if(extendedConfiguration.clientJoinServer.badNicknames.nicknames.contains(event.getClientNickname())){
                bot.getFbotApi().kickUser(event.getClientId(), bot.getExtendedConfiguration().clientJoinServer.badNicknames.kickReason, true, false);
                return;
            }

            ClientInfo clientInfo = bot.getFbotApi().getClientByUid(event.getUniqueClientIdentifier());

            IClient clientInterface = bot.getClientManager().register(new ClientImpl(clientInfo), RegisterCause.USER);

            bot.getAsyncWrapper().ifPresent(wrapper -> {
                //Admin channel update
                if (bot.getExtendedConfiguration().adminUpdaterModuleSettings.staffUids.containsKey(event.getUniqueClientIdentifier())) {
                    bot.getFbotApi().updateAdministrativeChannel(event.getUniqueClientIdentifier());
                }

                //Welcome message
                if(extendedConfiguration.clientJoinServer.welcomeMessage.status){
                    String message = bot.getStringUtils().parseMessage(extendedConfiguration.clientJoinServer.welcomeMessage.joinMessage).toString();
                    message = bot.getStringUtils().basicVariables(message);
                    message = bot.getStringUtils().userVariables(clientInfo, message);

                    wrapper.sendPrivateMessage(event.getClientId(), message);
                }

                //Adding new registred user & updating newest users channel description
                if(clientInfo.getTotalConnections() == 1){
                    bot.getServerHook().addNewRegistredUser();

                    if(bot.getExtendedConfiguration().channelUpdaterModuleSettings.newUsersChannelStatus){
                        bot.getFbotApi().editChannelDescription(bot.getExtendedConfiguration().channelUpdaterModuleSettings.newUserChannelId, bot.getStringUtils().getNewUsers());
                    }

                }

                if(extendedConfiguration.clanTeleportFunctionSettings.clanTeleportFunctionStatus) {
                    wrapper.sendPrivateMessage(event.getClientId(), "[b] Teleporter klanowy [/b]");
                    wrapper.sendPrivateMessage(event.getClientId(), "Te klany posiadaja opcje teleportacji: " + bot.getExtendedConfiguration().clanTeleportFunctionSettings.clansMap.keySet().stream().map(Object::toString).collect(Collectors.joining(",")));
                    wrapper.sendPrivateMessage(event.getClientId(), "Wystarczy ze napiszesz !teleport <nazwa klanu> aby się tam przenieść");
                }

                //Own channel update (if needed)
                if(extendedConfiguration.adminUpdaterModuleSettings.staffUids.get(event.getUniqueClientIdentifier()) != null){
                    bot.getFbotApi().editChannelName(extendedConfiguration.adminUpdaterModuleSettings.staffUids.get(event.getUniqueClientIdentifier()), event.getClientNickname() + extendedConfiguration.adminUpdaterModuleSettings.statusValues.get(1));
                }

                Channel privateChannel = clientInterface.getPrivateChannelId() != 0 ? bot.getFbotApi().getChannelById(clientInterface.getPrivateChannelId()) : null;

                if(clientInterface.getPrivateChannelId() != 0){
                    if(privateChannel == null || privateChannel.getTopic().equals("free")){
                        wrapper.pokeClient(event.getClientId(), "Twoj kanał prywatny został usunięty!");

                        clientInterface.setPrivateChannel(0);
                    } else {
                        String channelDate = privateChannel.getTopic();
                        String date = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now());

                        if (channelDate.equals(date)) {
                            return;
                        }

                        bot.getFbotApi().editChannelTopic(clientInterface.getPrivateChannelId(), date);

                        wrapper.sendPrivateMessage(event.getClientId(), "Aktynowść twojego prywatnego kanału została podbita.");
                    }
                }

                //Checking new server online user count record & eventually updating it.
                bot.getServerHook().updateStatistics();
            });
        }
    }

    @Override
    public EventTypes getEventType() {
        return EventTypes.CLIENT_JOIN_EVENT;
    }

}
