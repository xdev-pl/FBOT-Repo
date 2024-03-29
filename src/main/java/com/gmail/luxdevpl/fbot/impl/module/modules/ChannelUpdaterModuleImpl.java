package com.gmail.luxdevpl.fbot.impl.module.modules;

import com.github.theholywaffle.teamspeak3.api.wrapper.*;
import com.gmail.luxdevpl.fbot.api.FireBotAPI;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.utils.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ChannelUpdaterModuleImpl extends AbstractModule implements Runnable {

    private FireBotAPI api;

    private StringUtils stringUtils;

    private ExtendedConfiguration.ChannelUpdaterModuleSettings channelUpdater = getConfiguration().channelUpdaterModuleSettings;
    private ExtendedConfiguration.ChannelUpdaterModuleSettings.ChannelUpdaterFunctionsStatus function = channelUpdater.channelUpdaterFunctionsStatus;
    private ExtendedConfiguration.ChannelUpdaterModuleSettings.ChannelUpdaterModuleValues strings = channelUpdater.channelUpdaterModuleValues;
    private ExtendedConfiguration.ChannelUpdaterModuleSettings.ChannelUpdaterValuesID ids = channelUpdater.channelUpdaterValuesID;

    private ExtendedConfiguration.AdminChannelUpdaterModule adminChannelUpdaterModule = getConfiguration().adminUpdaterModuleSettings;

    public ChannelUpdaterModuleImpl(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        super(name, wrapper, configuration);

        this.api = wrapper.getFbotApi();
        this.stringUtils = wrapper.getStringUtils();


    }

    @Override
    public void performActions(){
        try {

            if(function.virtualServerNameEnabled) {
                api.updateVirtualServerName(strings.virtualServerName.replace("%online", String.valueOf(api.getOnlineClients())));
            }

            if(function.timeChannelEnabled){
                api.editChannelName(ids.timeChannelId, strings.timeChannelName.replace("%hms", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))));
            }

            if(function.onlineChannelEnabled){
                api.editChannelName(ids.onlineChannelId, strings.onlineChannelName.replace("%online", String.valueOf(api.getOnlineClients())));
            }

            if(function.pingAmountEnabled){
                api.editChannelName(ids.pingAmountChannelId, strings.pingAmountChannelName.replace("%ping", String.valueOf(api.getAveragePing())));

            }

            if(function.lostPacketEnabled){
                api.editChannelName(ids.lostPacketsChannelid, strings.lostPacketsChannelName.replace("%packets", String.valueOf(api.getAveragePacketLoss())));
            }

            if(function.uniqueClientAmountEnabled){
                api.editChannelName(ids.uniqueClientAmountChannelId, strings.uniqueClientAmountChannelName.replace("%unique", String.valueOf(getWrapper().getServerHook().getRegistredClientAmount())));
            }

            if(function.channelsAmountEnabled){
                api.editChannelName(ids.channelsAmountId, strings.channelsAmountName.replace("%channels", String.valueOf(api.getChannelsAmount())));
            }

            if(function.registeredClientsEnabled){
                api.editChannelName(ids.registerdClientsChannelId, strings.registerClientsChannelName.replace("%registred", String.valueOf(getWrapper().getServerHook().getRegistredClientAmount())));
            }

            if(function.usersRecordEnabled){
                api.editChannelName(ids.newRecordChannelId, strings.newRecordChannelName.replace("%record", String.valueOf(getWrapper().getServerHook().getOnlineRecord())));
            }

            if(function.bannedUsersInfoEnabled) {
                api.editChannelDescription(ids.bannedUsersChannelId, this.getBans());
            }

            if(function.usersFromOtherCountryEnabled) {
                api.editChannelDescription(ids.usersFromAnotherCountryChannelId, this.getUsersFromOtherCountry());
            }

            if(adminChannelUpdaterModule.adminStatusOnChannelStatus) {
                api.editChannelDescription(adminChannelUpdaterModule.staffStatusChannelId, this.getAdminStatus());
            }

            if(adminChannelUpdaterModule.adminStatusOnChannelStatus) {
                this.updateAdministrativeChannels();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void enable() {
        super.getExecutorService().scheduleWithFixedDelay(this, 5, 25, TimeUnit.SECONDS);
    }

    @Override
    public void disable() {
        super.getExecutorService().shutdown();
    }

    @Override
    public void run() {
        this.performActions();
    }

    private List<String> getUsersFromOtherCountry(){
        List<String> description = new ArrayList<>(strings.usersFromOtherCountryChannelDescription);

        //[url=client://0/%UID] nick [/url]
        List<String> clients = getWrapper().getClientManager().getClients().values().stream().filter(client -> !client.getCountry().equals(getConfiguration().channelUpdaterModuleSettings.channelUpdaterValuesOther.defaultCountry) && !client.getCountry().isEmpty()).
                map(n -> "[img]https://flagcdn.com/16x12/" + n.getCountry() + ".png[/img]" + "[url=client://0/" + n.getUniqueIdentifier() + "] [b]" + n.getNickname() + "[/url]").collect(Collectors.toList());

        return stringUtils.findAndReplace(description, "%users", stringUtils.parseMessage(clients).toString());

    }

    private void updateAdministrativeChannels(){
        FireBotAPI api = getWrapper().getFbotApi();

        for (Map.Entry<String, Integer> entry : getConfiguration().adminUpdaterModuleSettings.administrativeUuids.entrySet()) {
            ClientInfo clientInfo = api.getClientByUid(entry.getKey());
            String nickname = api.getDatabaseClientInfoByUid(entry.getKey()).getNickname();
            if (clientInfo == null) {
                api.editChannelName(entry.getValue(), nickname + getConfiguration().adminUpdaterModuleSettings.statusValues.get(0));
            } else {
                if (clientInfo.isOutputMuted()){
                    api.editChannelName(entry.getValue(), nickname + getConfiguration().adminUpdaterModuleSettings.statusValues.get(2));
                } else {
                    api.editChannelName(entry.getValue(), nickname + getConfiguration().adminUpdaterModuleSettings.statusValues.get(1));
                }
            }
        }
    }

    private StringBuilder getBans(){
        StringBuilder stringBuilder = new StringBuilder();

        List<Ban> bans = getWrapper().getFbotApi().getBans();

        stringBuilder.append("[hr] \n");
        stringBuilder.append("[center][size=14][b]Lista banów[/b][/size][/center] \n");
        stringBuilder.append("[hr] \n");

        stringBuilder.append("[size=14] [b] Blokad razem: ").append(bans.size() == 0 ? 0 : bans.size() / 2).append("[/size] \n");

        int banIndex = 0;
        for (Ban ban : bans) {
            if(stringBuilder.length() >= 8192){
                stringBuilder.setLength(8192);
                break;
            }

            if(ban.getLastNickname().isEmpty()) continue;

            banIndex++;

            stringBuilder.append("[size=12]● Blokada: ").append(banIndex).append("\n");
            stringBuilder.append("   • Pseudonim posiadacza: ").append(ban.getLastNickname()).append("\n");
            stringBuilder.append("   • Czas (s): ").append(ban.getDuration() == 0 ? "Permamentny" : ban.getDuration()).append("\n");
            stringBuilder.append("   • Powód: ").append(ban.getReason().isEmpty() ? "Brak powodu " : ban.getReason()).append("\n");
            stringBuilder.append("   • Nadał bana: ").append(ban.getInvokerName()).append("\n");
            stringBuilder.append("   • Data nadania: ").append(ban.getCreatedDate()).append("[/size] \n");
            stringBuilder.append("[hr] \n");
        }

        return stringBuilder;
    }

    private StringBuilder getAdminStatus() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[hr] \n");
        stringBuilder.append("[center][size=14][b]Administracja serwera[/b][/size] \n");
        stringBuilder.append("[/center] [hr] \n");

        for (Integer integer : getWrapper().getExtendedConfiguration().adminUpdaterModuleSettings.administrativeGroupIds) {
            stringBuilder.append(" \n");

            int clientAmount = getWrapper().getFbotApi().getServerGroupClientsAmount(integer);

            ServerGroup serverGroup = getWrapper().getFbotApi().getServerGroup(integer);

            stringBuilder.append("[size=10][b] [ ").append(serverGroup.getName()).append(" ] [/size] \n");

            if (clientAmount == 0) {
                stringBuilder.append("Osób w grupie: ").append(clientAmount).append("\n");
                stringBuilder.append("\n");
                stringBuilder.append("● [img]http://i.imgur.com/xf55jvq.png[/img][size=9] Aktualnie brak administratorów w tej grupie.[/size] \n");
            } else {
                stringBuilder.append("Osób w grupie: ").append(clientAmount).append("\n");

                for (ServerGroupClient serverGroupClient : getWrapper().getFbotApi().getServerGroupClients(integer)) {
                    DatabaseClientInfo databaseClientInfo = getWrapper().getFbotApi().getDatabaseClientInfo(serverGroupClient.getClientDatabaseId());
                    stringBuilder.append("\n");

                    stringBuilder.append("● [size=9]Nick: ").append("[url=client://0/").append(databaseClientInfo.getUniqueIdentifier()).append("] [b]").append(databaseClientInfo.getNickname()).append("[/url]").append(" [/size] ").append("\n");
                    stringBuilder.append("● [size=9]Status: ").append(getWrapper().getFbotApi().isOnline(databaseClientInfo.getUniqueIdentifier()) ? "[/size][color=green][b]Online[/b][/color] od[b] " +
                            getWrapper().getStringUtils().getDurationBreakdown(System.currentTimeMillis() - databaseClientInfo.getLastConnectedDate().getTime()) + "[/b] \n" : "[/size][color=red][b]Offline[/b][/color] ostatnie polaczenie[b] " +
                            getWrapper().getStringUtils().getDurationBreakdown(System.currentTimeMillis() - databaseClientInfo.getLastConnectedDate().getTime()) + "[/b] temu \n");
                }
            }
        }
        stringBuilder.append("[hr] \n");

        return stringBuilder;
    }
}
