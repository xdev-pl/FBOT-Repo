package com.gmail.luxdevpl.fbot.impl.module.modules;

import com.github.theholywaffle.teamspeak3.api.PermissionGroupDatabaseType;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;
import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClientCheckModuleImpl extends AbstractModule implements Runnable {

    private Map<String, Integer> afkGroups = new HashMap<>(); //Nazwa grupy, ID grupy

    private Map<String, Integer> afkClientGroups = new HashMap<>(); //UID klienta, ID Grupy w ktorej jest afk

    private void checkIdleGroups(){

    }

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
        try {
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

                    if (clientInfo.getIdleTime() > TimeUnit.MINUTES.toMillis(1)) {
                        if (clientInfo.isOutputMuted()) {
                            String idleTime = "AFK: " + getWrapper().getStringUtils().getDurationBreakdown(clientInfo.getIdleTime());

                            //Jesli uzytkownik jest juz w jakiejs grupie AFK
                            if (this.afkClientGroups.containsKey(ts3Client.getUniqueIdentifier())) {
                                //Jesli grupa w ktorej jest bedzie taka sama co jego aktualny czas afkk
                                if (this.afkClientGroups.get(ts3Client.getUniqueIdentifier()).equals(this.afkGroups.get(idleTime))) {
                                    return;
                                }

                                getWrapper().getFbotApi().removeClientFromServerGroup(clientInfo.getDatabaseId(), this.afkClientGroups.get(ts3Client.getUniqueIdentifier()));

                                if (this.afkGroups.containsKey(idleTime)) {

                                    this.afkClientGroups.remove(clientInfo.getUniqueIdentifier());
                                    this.afkClientGroups.put(clientInfo.getUniqueIdentifier(), this.afkGroups.get(idleTime));

                                    getWrapper().getFbotApi().addClientToServerGroup(clientInfo.getDatabaseId(), this.afkGroups.get(idleTime));

                                } else {
                                    int createdIdleGroup = getWrapper().getUnsafeWrapper().copyServerGroup(164, idleTime, PermissionGroupDatabaseType.REGULAR);

                                    this.afkGroups.put(idleTime, createdIdleGroup);
                                    this.afkClientGroups.remove(clientInfo.getUniqueIdentifier());
                                    this.afkClientGroups.put(clientInfo.getUniqueIdentifier(), createdIdleGroup);

                                    getWrapper().getFbotApi().addClientToServerGroup(clientInfo.getDatabaseId(), createdIdleGroup);
                                }

                            } else if (!this.afkClientGroups.containsKey(ts3Client.getUniqueIdentifier())) {
                                if (this.afkGroups.containsKey(idleTime)) {
                                    getWrapper().getFbotApi().addClientToServerGroup(clientInfo.getDatabaseId(), this.afkGroups.get(idleTime));
                                    this.afkClientGroups.put(clientInfo.getUniqueIdentifier(), this.afkGroups.get(idleTime));
                                } else {
                                    int createdIdleGroup = getWrapper().getUnsafeWrapper().copyServerGroup(164, idleTime, PermissionGroupDatabaseType.REGULAR);

                                    this.afkGroups.put(idleTime, createdIdleGroup);
                                    this.afkClientGroups.remove(clientInfo.getUniqueIdentifier());
                                    this.afkClientGroups.put(clientInfo.getUniqueIdentifier(), createdIdleGroup);

                                    getWrapper().getFbotApi().addClientToServerGroup(clientInfo.getDatabaseId(), createdIdleGroup);
                                }
                            }
                        }
                    } else {
                        if (this.afkClientGroups.containsKey(clientInfo.getUniqueIdentifier())) {
                            getWrapper().getFbotApi().removeClientFromServerGroup(clientInfo.getDatabaseId(), this.afkClientGroups.get(clientInfo.getUniqueIdentifier()));
                            this.afkClientGroups.remove(clientInfo.getUniqueIdentifier());
                        }
                    }

                    if (!clientInfo.isOutputMuted()) {
                        clientList.stream().filter(client -> client.getChannelId() == clientInfo.getChannelId()).forEach(user -> {
                            long oldValue = ts3Client.getBestFriends().get(user.getUniqueIdentifier()) == null ? 0 : ts3Client.getBestFriends().get(user.getUniqueIdentifier());
                            ts3Client.getBestFriends().put(user.getUniqueIdentifier(), oldValue + 60000);
                        });
                    }
                    System.out.println("Took: " + (System.currentTimeMillis() - diff) + " ms.");
                });
            });
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        performActions();
    }
}
