package com.gmail.luxdevpl.fbot.impl.storage.storages;

import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.basic.TS3Server;
import com.gmail.luxdevpl.fbot.configuration.DefaultConfiguration;
import com.gmail.luxdevpl.fbot.storage.util.AbstractStorage;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.basic.enums.TopTypes;
import com.gmail.luxdevpl.fbot.storage.mode.MySQLStorage;
import com.gmail.luxdevpl.fbot.impl.storage.util.Query;
import com.google.common.collect.Maps;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MySQLStorageImpl extends AbstractStorage implements MySQLStorage {

    private HikariDataSource dataSource;

    private IBotWrapper fireBot;

    public MySQLStorageImpl(IBotWrapper fireBot) {
        super(fireBot);

        this.fireBot = fireBot;
    }

    @Override
    protected void create(DefaultConfiguration configuration) {
        this.dataSource = new HikariDataSource();

        this.dataSource.setMaximumPoolSize((Runtime.getRuntime().availableProcessors() * 2) + 1);

        this.dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        this.dataSource.setJdbcUrl("jdbc:mysql://" + configuration.databaseSection.mysqlHostname + ":" + configuration.databaseSection.mysqlPort + "/" + configuration.databaseSection.mysqlDatabase);

        this.dataSource.setUsername(configuration.databaseSection.mysqlUser);
        this.dataSource.setPassword(configuration.databaseSection.mysqlPassword);

        this.dataSource.setConnectionTestQuery("SELECT 1");

        this.dataSource.addDataSourceProperty("cachePrepStmts", true);
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.dataSource.addDataSourceProperty("useServerPrepStmts", true);

        this.initializeTables();

        fireBot.getBotLogger().getLogger().info("Connection with database has been established.");
    }

    @Override
    protected void destroy() {
        this.dataSource.close();

        fireBot.getBotLogger().getLogger().info("Connection with database has been closed.");
    }

    @Override
    public void loadClientInfo(IClient client) {
        try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.USER_QUERY)) {
            preparedStatement.setString(1, client.getUniqueIdentifier());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    client.load(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadServerInfo(TS3Server server){
        try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.SERVER_QUERY)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    server.deserialize(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveServerInfo(TS3Server server){
        try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.SERVER_INSERT)) {
            preparedStatement.setInt(1, server.getOnlineRecord());
            preparedStatement.setString(2, server.getOnlineRecordTimestamp());
            preparedStatement.setInt(3, server.getRegistredClientAmount());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveClientInfo(IClient client) {
        try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.USER_INSERT)) {
            preparedStatement.setString(1, client.getUniqueIdentifier());
            preparedStatement.setString(2, client.getNickname());

            preparedStatement.setLong(3, client.getTimeSpent());
            preparedStatement.setLong(4, client.getLongestConnection());
            preparedStatement.setLong(5, client.getLastDisconnect());
            preparedStatement.setLong(6, client.getLastConnect());

            preparedStatement.setInt(7, client.getDatabaseId());
            preparedStatement.setInt(8, client.getConnections());
            preparedStatement.setLong(9, client.getCreated());
            preparedStatement.setInt(10, client.getLevel());
            preparedStatement.setInt(11, client.getPrivateChannelId());
            preparedStatement.setString(12, client.getIp());

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Long> getTopValues(TopTypes topTypes, int indexAmount) {
        Map<String, Long> values = new LinkedHashMap<>(indexAmount);

        switch (topTypes) {
            case CONNECTIONS:
                try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.TOP_CONNECTIONS_QUERY + indexAmount)) {
                    try (ResultSet rs = preparedStatement.executeQuery()) {
                        while (rs.next()) {
                            values.put(rs.getString("nickname"), rs.getLong("connectionsAmount"));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case TIME_SPENT:
                try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.TOP_SPENT_TIME_QUERY + indexAmount)) {
                    try (ResultSet rs = preparedStatement.executeQuery()) {
                        while (rs.next()) {
                            values.put(rs.getString("nickname"), rs.getLong("timeSpent"));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case LONGEST_CONNECTION:
                try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.TOP_LONGEST_CONNECTION_QUERY + indexAmount)) {
                    try (ResultSet rs = preparedStatement.executeQuery()) {
                        while (rs.next()) {
                            values.put(rs.getString("nickname"), rs.getLong("longestConnection"));
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }
        return values;
    }

    @Override
    public List<String> getNewUsers(int indexAmount) {
        List<String> newUsers = new LinkedList<>();
        try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.NEW_USERS_QUERY + indexAmount)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    newUsers.add(rs.getString("nickname"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newUsers;
    }

    @Override
    public Map<String, Long> getBestFriends(int databaseClientId) {
        Map<String, Long> bestFriends = Maps.newHashMap();
        try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.BEST_FRIENDS_QUERY)) {
            preparedStatement.setInt(1, databaseClientId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    bestFriends.put(rs.getString("uid"), rs.getLong("timeSpent"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bestFriends;
    }

    @Override
    public Optional<String> getNicknameByDatabaseId(int databaseId) {
        try (Connection connection = this.dataSource.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(Query.NAME_BY_DBID_QUERY)) {
            preparedStatement.setInt(1, databaseId);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    return Optional.ofNullable(rs.getString("nickname"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void initializeTables(){
        try (Connection connection = this.dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(Query.USERS_TABLES)) {
                preparedStatement.execute();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(Query.SERVER_TABLES)) {
                preparedStatement.execute();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(Query.FRIENDS_TABLES)) {
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
