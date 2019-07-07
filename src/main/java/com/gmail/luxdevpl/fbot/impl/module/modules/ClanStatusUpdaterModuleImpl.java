package com.gmail.luxdevpl.fbot.impl.module.modules;

import com.github.theholywaffle.teamspeak3.api.wrapper.Ban;
import com.github.theholywaffle.teamspeak3.api.wrapper.DatabaseClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;
import com.gmail.luxdevpl.fbot.api.FireBotAPI;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ClanStatusUpdaterModuleImpl extends AbstractModule implements Runnable {

    private ExtendedConfiguration.ClanStatusUpdaterModuleSettings settings;

    private FireBotAPI api;

    public ClanStatusUpdaterModuleImpl(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        super(name, wrapper, configuration);

        this.settings = getConfiguration().clanStatusUpdaterModuleSettings;
        this.api = getWrapper().getFbotApi();
    }

    @Override
    public void performActions() {
        settings.clanStatusUpdaterModuleChannelIdToGroupId.forEach((key, value) -> {
            List<ServerGroupClient> serverGroupClients = api.getServerGroupClients(value);

            int online = api.getActiveUsers(serverGroupClients);
            String groupName = api.getServerGroupName(value);

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("[center][size=14][b]Szczegółowe informacje [/b][/size][/center] \n");

            List<DatabaseClientInfo> onlineClients = new ArrayList<>();
            List<DatabaseClientInfo> offlineClients = new ArrayList<>();
            List<DatabaseClientInfo> awayClients = new ArrayList<>();

            serverGroupClients.forEach(serverGroupClient -> {
                DatabaseClientInfo databaseClientInfo = api.getDatabaseClientInfo(serverGroupClient.getClientDatabaseId());

                if(api.isOnline(serverGroupClient.getUniqueIdentifier())){
                    if(api.getClientByUid(serverGroupClient.getUniqueIdentifier()).isOutputMuted()){
                        awayClients.add(databaseClientInfo);
                        return;
                    }
                    onlineClients.add(databaseClientInfo);
                } else {
                    offlineClients.add(databaseClientInfo);
                }
            });

            stringBuilder.append("[size=12] [b] Lista uzytkownikow [color=green] online [/color] [/b] [/size] \n");

            if(onlineClients.isEmpty()){
                stringBuilder.append("[b]").append("Brak uzytkownikow.").append("\n");
            }

            onlineClients.forEach(onlineClient -> stringBuilder.append("[b]").append(onlineClient.getNickname()).append("[/b] jest [color=green] [b]online [/color] \n"));

            stringBuilder.append(" \n");

            stringBuilder.append("[size=12] [b] Lista uzytkonikow [color=gold] away [/color] [/b] [/size] \n");

            if(awayClients.isEmpty()){
                stringBuilder.append("[b]").append("Brak uzytkownikow.").append("\n");
            }

            awayClients.forEach(awayClient -> stringBuilder.append("[b]").append(awayClient.getNickname()).append("[/b] jest [color=gold] [b]away [/color] \n"));

            stringBuilder.append(" \n");

            stringBuilder.append("[size=12] [b] Lista uzytkonikow [color=red] offline [/color] [/b] [/size] \n");
            if(offlineClients.isEmpty()){
                stringBuilder.append("[b]").append("Brak uzytkownikow.").append("\n");
            }

            offlineClients.forEach(offlineClient -> stringBuilder.append("[b]").append(offlineClient.getNickname()).append("[/b] jest [color=red] [b]offline [/color] od [b]").append(getWrapper().getStringUtils().getDurationBreakdown(System.currentTimeMillis() - offlineClient.getLastConnectedDate().getTime())).append("[/b] \n"));
            stringBuilder.append(" \n");

            getWrapper().getFbotApi().editChannelName(key, settings.clanStatusUpdaterChannelName.replace("%clanName", groupName).replace("%active", String.valueOf(online)));
            getWrapper().getFbotApi().editChannelDescription(key, stringBuilder);
        });
    }

    @Override
    public void enable() {
        if(settings.clanStatusUpdaterStatus){
            super.getExecutorService().scheduleWithFixedDelay(this, 5, settings.clanStatusUpdaterModuleInterval, TimeUnit.SECONDS);
        }
    }

    @Override
    public void disable() {
        super.getExecutorService().shutdown();
    }

    @Override
    public void run() {
        this.performActions();
    }
}
