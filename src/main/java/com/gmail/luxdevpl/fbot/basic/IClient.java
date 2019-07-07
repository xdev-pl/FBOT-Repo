package com.gmail.luxdevpl.fbot.basic;

import com.gmail.luxdevpl.fbot.basic.enums.RegisterCause;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.sql.ResultSet;
import java.util.Map;

public interface IClient {

    String getNickname();

    String getUniqueIdentifier();

    String getIp();

    String getCountry();

    int getClientId();

    int getDatabaseId();

    int getLevel();

    int getConnections();

    int getPrivateChannelId();

    long getTimeSpent();

    long getLastConnect();

    long getLastDisconnect();

    long getLongestConnection();

    long getCreated();

    void recalculateStatictics();

    void setPrivateChannel(int id);

    void recalculateLevel(IBotWrapper wrapper);

    void handleLogin(RegisterCause registerCause);

    void handleDisconnect();

    void load(ResultSet rs);

    void save();

    Map<String, Long> getBestFriends();

}
