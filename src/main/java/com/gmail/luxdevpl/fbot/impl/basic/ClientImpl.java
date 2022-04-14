package com.gmail.luxdevpl.fbot.impl.basic;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.gmail.luxdevpl.fbot.api.FireBotAPI;
import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.basic.enums.RegisterCause;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ClientImpl implements IClient {

    private int clientId;
    private int databaseId;
    private int connections;
    private int level;
    private int privateChannelId;

    private String nickname;
    private String uid;
    private String ip;
    private String country;

    private long timeSpent;
    private long lastConnect;
    private long lastDisconnect;
    private long longestConnection;
    private long createTimestamp;

    private Map<String, Long> bestFriendsMap;

    public ClientImpl(ClientInfo clientInfo){
        this.clientId = clientInfo.getId();
        this.databaseId = clientInfo.getDatabaseId();
        this.connections = 0;
        this.level = 0;

        this.nickname = clientInfo.getNickname();
        this.uid = clientInfo.getUniqueIdentifier();
        this.ip = clientInfo.getIp();
        this.country = clientInfo.getCountry();

        this.timeSpent = 0;
        this.lastConnect = 0;
        this.lastDisconnect = 0;
        this.longestConnection = 0;
        this.privateChannelId = 0;

        this.bestFriendsMap = new HashMap<>();
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    @Override
    public String getUniqueIdentifier() {
        return uid;
    }

    @Override
    public String getIp(){
        return ip;
    }

    @Override
    public int getClientId() {
        return clientId;
    }

    @Override
    public int getDatabaseId() {
        return databaseId;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getConnections() {
        return connections;
    }

    @Override
    public int getPrivateChannelId(){
        return privateChannelId;
    }

    @Override
    public long getTimeSpent() {
        return timeSpent;
    }

    @Override
    public long getLastConnect() {
        return lastConnect;
    }

    @Override
    public long getLastDisconnect() {
        return lastDisconnect;
    }

    @Override
    public long getLongestConnection() {
        return longestConnection;
    }

    @Override
    public long getCreated() {
        return createTimestamp;
    }

    @Override
    public String getCountry(){
        return country;
    }

    @Override
    public void recalculateStatictics() {
        if(System.currentTimeMillis() - this.lastConnect > this.longestConnection){
            this.longestConnection = System.currentTimeMillis() - this.lastConnect;
        }

        this.timeSpent += 60000; //1min
    }

    @Override
    public void recalculateLevel(IBotWrapper wrapper) {
        ExtendedConfiguration configuration = wrapper.getExtendedConfiguration();
        FireBotAPI api = wrapper.getFbotApi();

        long minutes = TimeUnit.MILLISECONDS.toMinutes(this.timeSpent);

        int level = this.level;

        configuration.genericClientUpdaterModule.levelingTimeGroupsId.forEach((key, value) -> {
            if (minutes >= key) {
                this.level = value;
            }
        });

        if (level != this.level) {
            String groupName = api.getServerGroupName(this.level);

            api.removeClientFromServerGroup(this.databaseId, level);
            api.addClientToServerGroup(this.databaseId, this.level);

            api.pokeClient(this.clientId, "Awansowales na następny poziom: " + groupName + ". Gratulacje. ");
        }
    }

    @Override
    public Map<String, Long> getBestFriends(){
        return this.bestFriendsMap;
    }

    @Override
    public void handleLogin(RegisterCause registerCause) {
        if (this.createTimestamp == 0) {
            this.createTimestamp = System.currentTimeMillis();
        }

        this.lastConnect = System.currentTimeMillis();

        switch (registerCause) {
            case USER:
                this.connections++;
                break;
            default:
                break;
        }
    }

    @Override
    public void setPrivateChannel(int privateChannelId){
        this.privateChannelId = privateChannelId;
    }

    @Override
    public void handleDisconnect() {
        this.lastDisconnect = System.currentTimeMillis();
    }

    @Override
    public void save(){

    }

    @Override
    public void load(ResultSet resultSet){
        try {
            this.connections = resultSet.getInt("connectionsAmount");
            this.timeSpent = resultSet.getLong("timeSpent");
            this.lastConnect = resultSet.getLong("lastConnect");
            this.lastDisconnect = resultSet.getLong("lastDisconnect");
            this.longestConnection = resultSet.getLong("longestConnection");
            this.createTimestamp = resultSet.getLong("createTimestamp");
            this.level = resultSet.getInt("level");
            this.privateChannelId = resultSet.getInt("privateChannelId");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        return "privateChannelId:"+this.privateChannelId+";";
    }

}
