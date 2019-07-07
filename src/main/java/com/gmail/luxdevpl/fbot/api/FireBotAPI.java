package com.gmail.luxdevpl.fbot.api;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.VirtualServerProperty;

import com.github.theholywaffle.teamspeak3.api.wrapper.*;
import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.basic.enums.RegisterCause;
import com.gmail.luxdevpl.fbot.impl.basic.ClientImpl;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class FireBotAPI {

    private IBotWrapper botWrapper;

    public FireBotAPI(IBotWrapper botWrapper){
        this.botWrapper = botWrapper;
    }

    public void updateVirtualServerName(String paramString){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.editServer(Collections.singletonMap(VirtualServerProperty.VIRTUALSERVER_NAME, paramString)));
    }

    public void editChannelName(int paramInt, String paramString){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.editChannel(paramInt, Collections.singletonMap(ChannelProperty.CHANNEL_NAME, paramString)));
    }

    public void pokeClient(int clientId, String reason){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.pokeClient(clientId, reason));
    }

    public void editChannelDescription(int channelId, List<String> message){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.editChannel(channelId, Collections.singletonMap(ChannelProperty.CHANNEL_DESCRIPTION, botWrapper.getStringUtils().parseMessage(message).toString())));
    }

    public void editChannelDescription(int channelId, StringBuilder description){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.editChannel(channelId, Collections.singletonMap(ChannelProperty.CHANNEL_DESCRIPTION, description.toString())));
    }

    public void editChannelDescription(int channelId, String description){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.editChannel(channelId, Collections.singletonMap(ChannelProperty.CHANNEL_DESCRIPTION, description)));
    }

    public void editChannel(int channelId, Map<ChannelProperty, String> propertyStringMap){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.editChannel(channelId, propertyStringMap));
    }

    public void setClientChannelGroup(int groupId, int channelId, int clientDatabaseId){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.setClientChannelGroup(groupId, channelId, clientDatabaseId));
    }

    public void moveClient(int clientId, int channelId){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.moveClient(clientId, channelId));
    }

    public void createChannel(String name, Map<ChannelProperty, String> channelPropertyStringMap){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.createChannel(name, channelPropertyStringMap));
    }

    public void addClientToServerGroup(int clientDatabaseId, int groupId){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.addClientToServerGroup(groupId, clientDatabaseId));
    }

    public void removeClientFromServerGroup(int clientDatabaseId, int groupId){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> wrapper.removeClientFromServerGroup(groupId, clientDatabaseId));
    }

    public Optional<IClient> getClientIdByDatabaseId(int databaseId){
        return botWrapper.getClientManager().getClients().values().stream().filter(client -> client.getDatabaseId() == databaseId).findFirst();
    }

    public void editChannelTopic(int channelId, String string){
        this.editChannel(channelId, Collections.singletonMap(ChannelProperty.CHANNEL_TOPIC, string));
    }

    public void removeSubchannels(Channel channel){
        this.getChannels().stream()
                .filter(filteredChannel -> filteredChannel.getParentChannelId() == channel.getId())
                .forEach(forEachChannel -> botWrapper.getUnsafeAsyncWrapper().deleteChannel(forEachChannel.getId()));
    }

    public void kickUser(int clientId, String reason, boolean fromServer, boolean withPoke) {
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> {
            if (fromServer) {
                wrapper.kickClientFromServer(reason, clientId);
                return;
            }
            if (withPoke) {
                wrapper.pokeClient(clientId, reason);
                wrapper.kickClientFromChannel(reason, clientId);
            }
            wrapper.kickClientFromChannel(reason, clientId);
        });
    }

    public boolean isClientOnline(String uniqueIdentifier){
        try {
            return botWrapper.getAsyncWrapper().get().isClientOnline(uniqueIdentifier).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getActiveUsers(List<ServerGroupClient> serverGroupClients){
        try {
            if(serverGroupClients.size() == 0){
                return 0;
            }

            int online = 0;

            for(ServerGroupClient serverGroupClient : serverGroupClients){
                if(botWrapper.getAsyncWrapper().get().isClientOnline(serverGroupClient.getUniqueIdentifier()).get()){
                    online++;
                }
            }
            return online;
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        return 0;
    }

    public List<Ban> getBans(){
        try {
            return botWrapper.getAsyncWrapper().get().getBans().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Channel> getChannels(){
        try {
            return botWrapper.getAsyncWrapper().get().getChannels().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getChannelsAmount(){
        try {
            return botWrapper.getAsyncWrapper().get().getChannels().get().size();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAveragePing(){
        try {
            return (int) botWrapper.getAsyncWrapper().get().getConnectionInfo().get().getPing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getAveragePacketLoss(){
        try {
            return (int) botWrapper.getAsyncWrapper().get().getConnectionInfo().get().getPacketLoss();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getVirtualServerLogEntries(int amount){
        try {
            return botWrapper.getAsyncWrapper().get().getVirtualServerLogEntries(amount).get();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean isOnline(String uniqueIdentifier){
        return this.getClientByUid(uniqueIdentifier) != null;
    }

    public DatabaseClientInfo getDatabaseClientInfo(int databaseId){
        try {
            return botWrapper.getAsyncWrapper().get().getDatabaseClientInfo(databaseId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public DatabaseClientInfo getDatabaseClientInfoByUid(String uniqueIdentifier){
        try {
            return botWrapper.getAsyncWrapper().get().getDatabaseClientByUId(uniqueIdentifier).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ServerGroupClient> getServerGroupClients(int serverGroupId){
        try {
            return botWrapper.getAsyncWrapper().get().getServerGroupClients(serverGroupId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DatabaseClient> getDatabaseClients(){
        try {
            return botWrapper.getAsyncWrapper().get().getDatabaseClients().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Client> getClients(){
        try {
            return botWrapper.getAsyncWrapper().get().getClients().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getServerGroupClientsAmount(int serverGroupId){
        try {
            return botWrapper.getAsyncWrapper().get().getServerGroupClients(serverGroupId).get().size();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ServerGroup getServerGroup(int serverGroupId){
        try {
            return botWrapper.getAsyncWrapper().get().getServerGroups().get().stream().filter(serverGroup -> serverGroup.getId() == serverGroupId).findFirst().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ClientInfo getClientByUid(String uid){
        try {
            return botWrapper.getAsyncWrapper().get().getClientByUId(uid).get();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Integer> serverGroupsAsList(int[] groups){
        return Arrays.stream(groups).boxed().collect(Collectors.toList());
    }

    public ClientInfo getClientByClientId(int clientId){
        try {
            return botWrapper.getAsyncWrapper().get().getClientInfo(clientId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Channel getChannelById(int channelId) {
        try {
            return botWrapper.getAsyncWrapper().get().getChannels().get().stream().filter(channel -> channel.getId() == channelId).findFirst().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ChannelInfo getChannelInfoById(int channelId) {
        try {
            return botWrapper.getAsyncWrapper().get().getChannelInfo(channelId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isInServerGroup(int serverGroupId, String clientUid){
        try {
            return botWrapper.getAsyncWrapper().get().getClientByUId(clientUid).get().isInServerGroup(serverGroupId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getServerGroupName(int serverGroupId){
        try {
            for(ServerGroup serverGroup : botWrapper.getAsyncWrapper().get().getServerGroups().get()){
                if(serverGroup.getId() == serverGroupId){
                    return serverGroup.getName();
                }
            }
        } catch (InterruptedException e) {
            botWrapper.getBotLogger().getLogger().error("Blad! W API wystapil blad przy pobieraniu nazwy grupy", e);
        }
        return null;
    }

    public int getOnlineClients(){
        try {
            return botWrapper.getAsyncWrapper().get().getClients().get().size();
        } catch (InterruptedException e) {
            botWrapper.getBotLogger().getLogger().error("Blad! W API wystapil blad przy pobieraniu ilosci osob online", e);
        }
        return 0;
    }

    public void performLateJoinAction(){
        botWrapper.getAsyncWrapper().ifPresent(wrapper -> {
            try {
                wrapper.getClients().get()
                        .stream()
                        .filter(Client::isRegularClient)
                        .forEach(client -> botWrapper.getClientManager()
                                .register(new ClientImpl(botWrapper.getFbotApi().getClientByClientId(client.getId())), RegisterCause.SERVER));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateAdministrativeChannel(String uid){
        int channelToUpdate = botWrapper.getExtendedConfiguration().adminUpdaterModuleSettings.staffUids.get(uid);

        List<String> newChannelDescription = new ArrayList<>(botWrapper.getExtendedConfiguration().adminUpdaterModuleSettings.adminChannelsDescription.get(uid));

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        newChannelDescription = botWrapper.getStringUtils().findAndReplace(newChannelDescription, Arrays.asList("%lastTimeActive", "%lastSessionTime"), this.isClientOnline(uid) ? "teraz." : now.format(formatter), this.isClientOnline(uid) ? "Sesja aktualnie aktywna" : botWrapper.getStringUtils().getDurationBreakdown(System.currentTimeMillis() - this.getDatabaseClientInfoByUid(uid).getLastConnectedDate().getTime()));

        botWrapper.getFbotApi().editChannelDescription(channelToUpdate, newChannelDescription);

    }

}
