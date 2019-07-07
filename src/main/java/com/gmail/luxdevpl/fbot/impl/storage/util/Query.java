package com.gmail.luxdevpl.fbot.impl.storage.util;

public final class Query {

    public static String USERS_TABLES = "CREATE TABLE IF NOT EXISTS users (" +
            "uid varchar(50) not null, " +
            "nickname varchar(31) not null, " +
            "timeSpent bigint not null, " +
            "longestConnection bigint not null, " +
            "lastDisconnect bigint not null, " +
            "lastConnect bigint not null, " +
            "clientDatabaseId int not null, " +
            "connectionsAmount int not null, " +
            "createTimestamp bigint not null, " +
            "level int not null, " +
            "privateChannelId int not null, " +
            "address varchar(12) not null, " +
            "primary key (clientDatabaseId)" +
            ")";

    public static String SERVER_TABLES = "CREATE TABLE IF NOT EXISTS server (" +
            "onlineRecord int not null, " +
            "onlineRecordDate varchar(100) not null, " +
            "amountOfClients bigint not null, " +
            "primary key (amountOfClients)" +
            ")";

    public static String FRIENDS_TABLES = "CREATE TABLE IF NOT EXISTS friends (" +
            "clientDatabaseId int not null, " +
            "uid varchar(50) not null, " +
            "timeSpent bigint not null, " +
            "primary key (clientDatabaseId)" +
            ")";

    public static String NAME_BY_DBID_QUERY = "SELECT nickname FROM users WHERE clientDatabaseId = ?";

    public static String BEST_FRIENDS_QUERY = "SELECT * FROM friends WHERE clientDatabaseId = ?";

    public static String SERVER_INSERT = "REPLACE INTO server (onlineRecord, onlineRecordDate, amountOfClients) VALUES (?, ?, ?)";

    public static String SERVER_QUERY = "SELECT * FROM server";

    public static String USER_QUERY = "SELECT * FROM users WHERE uid = ?";

    public static String FRIENDS_INSERT = "INSERT INTO friends (clientDatabaseId, uid, timeSpent) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE clientDatabaseId = VALUES(clientDatabaseId), uid = VALUES(uid), timeSpent = VALUES(timeSpent)";

    public static String USER_INSERT = "INSERT INTO users (uid, nickname, timeSpent, longestConnection, lastDisconnect, lastConnect, clientDatabaseId, connectionsAmount, createTimestamp, level, privateChannelId, address) " +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " + "uid = VALUES(uid), nickname = VALUES(nickname), timeSpent = VALUES(timeSpent), longestConnection = VALUES(longestConnection), lastDisconnect = VALUES(lastDisconnect)" +
            ", lastConnect = VALUES(lastConnect), clientDatabaseId = VALUES(clientDatabaseId), connectionsAmount = VALUES(connectionsAmount), createTimestamp = VALUES(createTimestamp), level = VALUES(level), privateChannelId = VALUES(privateChannelId), address = VALUES(address)";

    public static String TOP_CONNECTIONS_QUERY = "SELECT * FROM users ORDER BY connectionsAmount DESC LIMIT ";

    public static String TOP_LONGEST_CONNECTION_QUERY = "SELECT * FROM users ORDER BY longestConnection DESC LIMIT ";

    public static String TOP_SPENT_TIME_QUERY = "SELECT * FROM users ORDER BY timeSpent DESC LIMIT ";

    public static String TOP_LEVEL_CLIENTS_QUERY = "SELECT * FROM levels ORDER BY level DESC LIMIT ";

    public static String NEW_USERS_QUERY = "SELECT * FROM users ORDER BY createTimestamp DESC LIMIT ";
}
