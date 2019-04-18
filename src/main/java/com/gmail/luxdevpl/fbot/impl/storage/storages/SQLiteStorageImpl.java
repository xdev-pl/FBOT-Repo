package com.gmail.luxdevpl.fbot.impl.storage.storages;

import com.gmail.luxdevpl.fbot.configuration.DefaultConfiguration;
import com.gmail.luxdevpl.fbot.storage.util.AbstractStorage;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.storage.mode.SQLiteStorage;
import com.zaxxer.hikari.HikariDataSource;

import java.io.File;

public class SQLiteStorageImpl extends AbstractStorage implements SQLiteStorage {

    private final String DRIVER_CLASS = "org.sqlite.JDBC";

    private HikariDataSource dataSource;

    private IBotWrapper fireBot;

    public SQLiteStorageImpl(IBotWrapper fireBot) {
        super(fireBot);

        this.fireBot = fireBot;
    }

    @Override
    protected void create(DefaultConfiguration configuration) {
        File sqliteFile = new File("database.db");
        sqliteFile.mkdir();

        this.dataSource.setDriverClassName(DRIVER_CLASS);
        this.dataSource.setJdbcUrl("jdbc:sqlite:" + sqliteFile.getAbsolutePath());

        //TODO Init tables.

        fireBot.getBotLogger().getLogger().info("Connection with database has been initialized.");
    }

    @Override
    protected void destroy() {
        this.dataSource.close();

        fireBot.getBotLogger().getLogger().info("Connection with database has been closed.");
    }

}
