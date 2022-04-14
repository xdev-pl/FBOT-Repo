package com.gmail.luxdevpl.fbot.listeners;

import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.event.IEventHandler;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.concurrent.TimeUnit;

public class ClientChannelJoinListener implements IEventHandler<ClientMovedEvent> {

    @Override
    public void handleEvent(ClientMovedEvent event, IBotWrapper bot) {
        int channelId = event.getTargetChannelId();

        ExtendedConfiguration configuration = bot.getExtendedConfiguration();
        if(channelId == configuration.helpCenterModuleSettings.privateChannelAssignerModule.channelCreatorChannelId) {
            ClientInfo clientInfo = bot.getFbotApi().getClientByClientId(event.getClientId());

            bot.getClientManager().getClient(event.getClientId()).ifPresent(client -> {
                if(client.getPrivateChannelId() != 0) {
                    bot.getAsyncWrapper().ifPresent(wrapper -> wrapper.moveClient(event.getClientId(), client.getPrivateChannelId()));
                } else {
                    if(System.currentTimeMillis() - clientInfo.getCreatedDate().getTime() < TimeUnit.MINUTES.toMillis(configuration.helpCenterModuleSettings.privateChannelAssignerModule.channelCreatorTimeRequirement)){
                        bot.getFbotApi().kickUser(event.getClientId(), "Musisz u nas spedzic wiecej czasu", false, true);
                        return;
                    }

                    bot.getPrivateChannelsManager().createFreeChannels(clientInfo.getNickname(), configuration.helpCenterModuleSettings.privateChannelAssignerModule.channelsToBeCreatedAmount, event.getClientId(), clientInfo.getDatabaseId());
                }
            });
        }

        if(channelId == configuration.helpCenterModuleSettings.rankAssignerModule.manGroupAssignerChannelId){
            ClientInfo clientInfo = bot.getFbotApi().getClientByClientId(event.getClientId());

            if(bot.getFbotApi().isInServerGroup(configuration.helpCenterModuleSettings.rankAssignerModule.womanGroupId, clientInfo.getUniqueIdentifier())){
                bot.getFbotApi().kickUser(event.getClientId(), configuration.helpCenterModuleSettings.rankAssignerModule.userAlreadyRegistered, false, true);
                return;
            }

            if(bot.getFbotApi().isInServerGroup(configuration.helpCenterModuleSettings.rankAssignerModule.manGroupId, clientInfo.getUniqueIdentifier())){
                bot.getFbotApi().removeClientFromServerGroup(clientInfo.getDatabaseId(), configuration.helpCenterModuleSettings.rankAssignerModule.manGroupId);
                bot.getFbotApi().kickUser(event.getClientId(), "Ranga zabrana.", false, true);
                return;
            }

            if(configuration.helpCenterModuleSettings.adminHelpSectorModule.timeSpentRequirement != 0){
                if(System.currentTimeMillis() - clientInfo.getCreatedDate().getTime() < TimeUnit.MINUTES.toMillis(configuration.helpCenterModuleSettings.adminHelpSectorModule.timeSpentRequirement)){
                    bot.getFbotApi().kickUser(event.getClientId(), "Musisz u nas spedzic wiecej czasu", false, true);
                    return;
                }
            }
            bot.getFbotApi().addClientToServerGroup(clientInfo.getDatabaseId(), configuration.helpCenterModuleSettings.rankAssignerModule.manGroupId);
            bot.getFbotApi().kickUser(event.getClientId(), "Ranga nadana.", false, true);
        }
        if(channelId == configuration.helpCenterModuleSettings.rankAssignerModule.womanGroupAssignerChannelId){
            ClientInfo clientInfo = bot.getFbotApi().getClientByClientId(event.getClientId());

            if(bot.getFbotApi().isInServerGroup(configuration.helpCenterModuleSettings.rankAssignerModule.manGroupId, clientInfo.getUniqueIdentifier())){
                bot.getFbotApi().kickUser(event.getClientId(), configuration.helpCenterModuleSettings.rankAssignerModule.userAlreadyRegistered, false, true);
                return;
            }

            if(bot.getFbotApi().isInServerGroup(configuration.helpCenterModuleSettings.rankAssignerModule.womanGroupId, clientInfo.getUniqueIdentifier())){
                bot.getFbotApi().removeClientFromServerGroup(clientInfo.getDatabaseId(), configuration.helpCenterModuleSettings.rankAssignerModule.womanGroupId);
                bot.getFbotApi().kickUser(event.getClientId(), "Ranga zabrana.", false, true);
                return;
            }

            if(configuration.helpCenterModuleSettings.rankAssignerModule.timeSpentRequirement > 0){
                if(System.currentTimeMillis() - clientInfo.getCreatedDate().getTime() < TimeUnit.MINUTES.toMillis(configuration.helpCenterModuleSettings.rankAssignerModule.timeSpentRequirement)){
                    bot.getFbotApi().kickUser(event.getClientId(), "Musisz u nas spedzic wiecej czasu", false, true);
                    return;
                }
            }

            bot.getFbotApi().addClientToServerGroup(clientInfo.getDatabaseId(), configuration.helpCenterModuleSettings.rankAssignerModule.womanGroupId);
            bot.getFbotApi().kickUser(event.getClientId(), "Ranga nadana.", false, true);
        }
    }

    @Override
    public EventTypes getEventType() {
        return EventTypes.CLIENT_MOVED_EVENT;
    }

}
