package com.gmail.luxdevpl.fbot.configuration;

import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;

import com.gmail.luxdevpl.fbot.impl.storage.util.StorageTypes;

import org.diorite.cfg.annotations.CfgClass;
import org.diorite.cfg.annotations.CfgComment;
import org.diorite.cfg.annotations.CfgName;
import org.diorite.cfg.annotations.defaults.CfgDelegateDefault;

@CfgClass(name = "DefaultConfiguration")
@CfgDelegateDefault("{new}")

@CfgComment(" ")
@CfgComment(" ")
@CfgComment("█▀▀ █▀▀▄ █▀▀█ ▀▀█▀▀")
@CfgComment("█▀▀ █▀▀▄ █░░█ ░░█░░")
@CfgComment("▀░░ ▀▀▀░ ▀▀▀▀ ░░▀░░")
@CfgComment("https://github.com/xdev-pl")
@CfgComment(" ")
@CfgComment("Welcome to the configuration file.")

public class DefaultConfiguration {

    @CfgComment("Wszystko co zwiazane z baza danych.")
    @CfgName("database-configurable-section")
    public DatabaseConfig databaseSection = new DatabaseConfig();

    @CfgComment("Wszystko co zwiazane z polaczeniem sie bota na serwer")
    @CfgName("query-configurable-section")
    public QueryConfig querySection = new QueryConfig();

    public static class QueryConfig {

        @CfgComment("ID Wirtualnego serwera")
        @CfgComment("Jesli posiadasz wiecej niz jeden wirtualny serwer, wskaz na ktory ma wchodzic bot.")
        @CfgName("virtual-server-id")
        public int virtualServerId = 1;

        @CfgComment("Haslo do wirtualnego serwera")
        @CfgComment("Jesli nie wiesz jakie masz haslo do query, mozesz je zmienic, poszukaj poradnika na forach.")
        @CfgName("virtual-server-query-password")
        public String password = "password";

        @CfgComment("Adres IP na ktory ma wchodzic bot.")
        @CfgName("virtual-server-address")
        public String ip = "127.0.0.1";

        @CfgComment("Port wirtualnego serwera")
        @CfgComment("Standardowo: 10011")
        @CfgName("virtual-sever-port")
        public int port = 10011;

        @CfgComment("Login do wirutalnego serwera")
        @CfgComment("Standardowo: serveradmin")
        @CfgName("virutal-server-login")
        public String queryLogin = "serveradmin";

        @CfgComment("Nazwa bota na serwerze.")
        @CfgName("bot-name")
        public String botName = "FBOT @ Administrator";

        public TS3Config parse() {
            TS3Config config = new TS3Config();

            config.setHost(this.ip);
            config.setQueryPort(this.port);

            config.setReconnectStrategy(ReconnectStrategy.constantBackoff());
            config.setFloodRate(TS3Query.FloodRate.UNLIMITED);

            return config;
        }

    }

    public static class DatabaseConfig {

        @CfgComment("Typ bazy danych")
        @CfgComment("Typy: SQLITE, MYSQL")
        @CfgName("storage-mode")
        public StorageTypes storage = StorageTypes.MYSQL;

        @CfgComment("Adres serwera mysql")
        @CfgName("mysql-server-address")
        public String mysqlHostname = "localhost";

        @CfgComment("Port serwera mysql")
        @CfgComment("Standardowo: 3306")
        @CfgName("mysql-server-port")
        public String mysqlPort = "3306";

        @CfgComment("Uzytkownik bazy danych")
        @CfgName("mysql-server-user")
        public String mysqlUser = "root";

        @CfgComment("Haslo do uzytkownika bazy danych")
        @CfgName("mysql-server-password")
        public String mysqlPassword = "password";

        @CfgComment("Baza danych w ktorej ma zapisywac dane")
        @CfgComment("Standardowo: firebot")
        @CfgName("mysql-server-database")
        public String mysqlDatabase = "firebot";
    }

}
