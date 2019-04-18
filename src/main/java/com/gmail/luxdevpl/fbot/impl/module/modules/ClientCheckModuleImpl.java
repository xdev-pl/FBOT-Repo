package com.gmail.luxdevpl.fbot.impl.module.modules;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;
import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ClientCheckModuleImpl extends AbstractModule implements Runnable {

    public ClientCheckModuleImpl(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        super(name, wrapper, configuration);
    }

    @Override
    public void enable() {
        if(getConfiguration().genericClientUpdaterModule.levelingUsersStatus) {
            super.getExecutorService().scheduleWithFixedDelay(this, 1, 1, TimeUnit.MINUTES);
        }
    }

    @Override
    public void disable() {
        super.getExecutorService().shutdown();
    }

    @Override
    public void performActions() {
        getWrapper().getAsyncWrapper().ifPresent(ts3ApiAsync -> {
            List<Client> clientList = getWrapper().getFbotApi().getClients();

            getWrapper().getClientManager().getClients().values().forEach(ts3Client -> {
                ts3Client.recalculateLevel(getWrapper());

                if (getConfiguration().genericClientUpdaterModule.timeRankUpdateStatus) {
                    if (!getWrapper().getFbotApi().isInServerGroup(getConfiguration().genericClientUpdaterModule.timeRankUpdateGroupId, ts3Client.getUniqueIdentifier()) && TimeUnit.MILLISECONDS.toMinutes(ts3Client.getTimeSpent()) >= getConfiguration().genericClientUpdaterModule.timeRankUpdateRequirement) {
                        ts3ApiAsync.addClientToServerGroup(getConfiguration().genericClientUpdaterModule.timeRankUpdateGroupId, ts3Client.getDatabaseId());
                        ts3ApiAsync.pokeClient(ts3Client.getClientId(), "Spedziles wymagany czas na naszym serwerze, od teraz masz rangę!");
                    }
                }

                //
                long diff = System.currentTimeMillis();

                ClientInfo clientInfo = getWrapper().getFbotApi().getClientByUid(ts3Client.getUniqueIdentifier());

                if (!clientInfo.isOutputMuted()) {
                    clientList.stream().filter(client -> client.getChannelId() == clientInfo.getChannelId()).forEach(user -> {
                        long oldValue = ts3Client.getBestFriends().get(user.getUniqueIdentifier()) == null ? 0 : ts3Client.getBestFriends().get(user.getUniqueIdentifier());
                        ts3Client.getBestFriends().put(user.getUniqueIdentifier(), oldValue + 60000);
                    });
                }
                System.out.println("Took: " + (System.currentTimeMillis() - diff) +  " ms.");
            });
        });
    }

    @Override
    public void run() {
        this.performActions();
    }
}
