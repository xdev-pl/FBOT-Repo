package com.gmail.luxdevpl.fbot.launcher;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;

import com.gmail.luxdevpl.fbot.api.FireBotAPI;
import com.gmail.luxdevpl.fbot.basic.TS3Server;
import com.gmail.luxdevpl.fbot.configuration.DefaultConfiguration;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.event.EventCaller;
import com.gmail.luxdevpl.fbot.impl.manager.ClientManagerImpl;
import com.gmail.luxdevpl.fbot.logger.BotLogger;
import com.gmail.luxdevpl.fbot.manager.ModuleManager;
import com.gmail.luxdevpl.fbot.manager.PrivateChannelsManager;
import com.gmail.luxdevpl.fbot.storage.mode.MySQLStorage;
import com.gmail.luxdevpl.fbot.utils.StringUtils;

import java.util.Optional;

public interface IBotWrapper {

    void start();

    void stop();

    TS3Api getUnsafeWrapper();

    TS3ApiAsync getUnsafeAsyncWrapper();

    default Optional<TS3Api> getWrapper() { return Optional.ofNullable(this.getUnsafeWrapper()); }

    default Optional<TS3ApiAsync> getAsyncWrapper() { return Optional.ofNullable(this.getUnsafeAsyncWrapper()); }

    Optional<MySQLStorage> getMainStorage();

    TS3Server getServerHook();

    FireBotAPI getFbotApi();

    DefaultConfiguration getBotConfiguration();

    ExtendedConfiguration getExtendedConfiguration();

    PrivateChannelsManager getPrivateChannelsManager();

    BotLogger getBotLogger();

    ClientManagerImpl getClientManager();

    ModuleManager getIModuleManager();

    StringUtils getStringUtils();

    default String getVersion(){
        return "2.2";
    }

    EventCaller getEventCaller();

}
