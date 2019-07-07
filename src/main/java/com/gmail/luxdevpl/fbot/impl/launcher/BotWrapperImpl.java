package com.gmail.luxdevpl.fbot.impl.launcher;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3ApiAsync;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.gmail.luxdevpl.fbot.api.FireBotAPI;
import com.gmail.luxdevpl.fbot.basic.TS3Server;
import com.gmail.luxdevpl.fbot.configuration.DefaultConfiguration;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.event.EventCaller;
import com.gmail.luxdevpl.fbot.impl.manager.ClientManagerImpl;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.impl.module.modules.*;
import com.gmail.luxdevpl.fbot.impl.module.modules.internal.DataSaveModuleImpl;
import com.gmail.luxdevpl.fbot.impl.module.modules.internal.VersionAnalyzerModuleImpl;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.logger.BotLogger;
import com.gmail.luxdevpl.fbot.manager.ModuleManager;
import com.gmail.luxdevpl.fbot.manager.PrivateChannelsManager;
import com.gmail.luxdevpl.fbot.configuration.utils.ConfigUtil;
import com.gmail.luxdevpl.fbot.impl.storage.storages.MySQLStorageImpl;
import com.gmail.luxdevpl.fbot.storage.mode.MySQLStorage;
import com.gmail.luxdevpl.fbot.impl.storage.util.StorageTypes;
import com.gmail.luxdevpl.fbot.utils.StringUtils;
import org.apache.commons.lang3.Validate;
import org.json.JSONArray;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public final class BotWrapperImpl implements IBotWrapper {

    private BotLogger logger;

    private TS3Query ts3Query;

    private TS3Api ts3Api;
    private TS3ApiAsync ts3ApiAsync;

    private FireBotAPI fbotApi;

    private TS3Server serverHook;

    private MySQLStorage database;

    private DefaultConfiguration botConfiguration;
    private ExtendedConfiguration extendedConfiguration;

    private PrivateChannelsManager channelsSectionManager;

    private ClientManagerImpl ts3ClientManager;
    private ModuleManager moduleManager;

    private StringUtils stringUtils;

    private EventCaller eventCaller;

    public BotWrapperImpl(BotLogger logger){
        Validate.notNull(logger);

        this.logger = logger;
    }

    @Override
    public void start() {
        this.logger.getLogger().info("Bot version: " + getVersion());

        this.initConfiguration();
        this.initStorage();
        this.initMiscellaneous();

        this.initQuery();

        this.initModules();

        //Initializing server statistics collector.
        this.serverHook.load();

        this.logger.getLogger().info("Structure of the bot has been initialized.");
        this.logger.getLogger().info("Performing late join action.");

        this.fbotApi.performLateJoinAction();

        this.logger.getLogger().info("Bot has been enabled.");
        this.logger.getLogger().info("Note: Thank you for using my bot ~luxdevpl");

    }

    @Override
    public void stop() {
        this.moduleManager.getModules()
                .stream()
                .peek(AbstractModule::disable)
                .forEach(module -> this.logger.getLogger().info("Module: [" + module.getModuleName() + "] has been disabled."));

        this.ts3ClientManager.getClients().values().forEach(this.database::saveClientInfo);

        this.serverHook.save();

        this.ts3Query.exit();

        this.logger.getLogger().info("Bot has been disabled thank you for using it!");
    }

    private void initConfiguration(){
        this.botConfiguration = ConfigUtil.loadConfig(new File("default-config.yml"), DefaultConfiguration.class);
        this.extendedConfiguration = ConfigUtil.loadConfig(new File("extended-config.yml"), ExtendedConfiguration.class);
    }

    private void initMiscellaneous(){
        this.serverHook = new TS3Server(this);
        this.ts3ClientManager = new ClientManagerImpl(this);
        this.channelsSectionManager = new PrivateChannelsManager(this);
        this.moduleManager = new ModuleManager();

        this.stringUtils = new StringUtils(this);
        this.fbotApi = new FireBotAPI(this);
    }

    private void initStorage(){
        try {
            switch (Objects.requireNonNull(StorageTypes.fromName(this.getBotConfiguration().databaseSection.storage.toString()))) {
                case MYSQL:
                    this.database = new MySQLStorageImpl(this);
                    this.database.open();
                    break;
                case SQLITE:
                    this.logger.getLogger().error("Storage: SQLITE is not supported yet, sorry :(");
                    System.exit(-1);
                    break;
            }
        } catch (Exception e){
            this.logger.getLogger().error("Error: check your database settings.", e);
        }
    }

    private void initQuery() {
        TS3Config queryConfig = this.botConfiguration.querySection.parse();

        this.ts3Query = new TS3Query(queryConfig);
        this.ts3Query.connect();

        this.ts3Api = this.ts3Query.getApi();
        this.ts3ApiAsync = this.ts3Query.getAsyncApi();

        this.ts3Api.selectVirtualServerById(this.botConfiguration.querySection.virtualServerId);
        this.ts3Api.login(this.botConfiguration.querySection.queryLogin, this.botConfiguration.querySection.password);
        this.ts3Api.setNickname(this.botConfiguration.querySection.botName);

        this.eventCaller = new EventCaller(this);

        this.ts3Api.registerAllEvents();
        this.ts3Api.addTS3Listeners(eventCaller);
    }

    private void registerModules(AbstractModule... abstractModules){
        Arrays.stream(abstractModules).forEach(abstractModule -> this.moduleManager.registerModule(abstractModule));
    }

    private void initModules(){
        AbstractModule versionAnalyzerModule = new VersionAnalyzerModuleImpl("VersionAnalyzerModule", this, this.extendedConfiguration);
        AbstractModule dataSaveModule = new DataSaveModuleImpl("DataSaveModule", this, this.extendedConfiguration);
        AbstractModule channelUpdaterModule = new ChannelUpdaterModuleImpl("ChannelUpdaterModule", this, this.extendedConfiguration);
        AbstractModule clientCheckModule = new ClientCheckModuleImpl("ClientCheckModule", this, this.extendedConfiguration);
        AbstractModule helpCenterModule = new HelpCenterModuleImpl("HelpCenterModule", this, this.extendedConfiguration);
        AbstractModule topStatisticsCollectorModule = new TopStatisticsCollectorModuleImpl("TopStatisticsCollectorModule", this, this.extendedConfiguration);
        AbstractModule topUpdaterModule = new TopUpdaterModuleImpl("TopUpdaterModule", this, this.extendedConfiguration);
        AbstractModule clientGroupUpdateSearcherModule = new AdvandedLogEntrySearcherModule("ClientGroupUpdateSearcherModule", this, this.extendedConfiguration);
        AbstractModule clanStatusUpdaterModule = new ClanStatusUpdaterModuleImpl("ClanStatusUpdaterModule", this, this.extendedConfiguration);
        AbstractModule privateSectionAnalyzerModule = new PrivateSectionAnalyzerModuleImpl("PrivateSectionAnalyzerModule", this, this.extendedConfiguration);

        this.registerModules(dataSaveModule, channelUpdaterModule,
                clientCheckModule, helpCenterModule, topStatisticsCollectorModule,
                topUpdaterModule, clientGroupUpdateSearcherModule, clanStatusUpdaterModule, privateSectionAnalyzerModule);

        this.moduleManager.getModules()
                .stream()
                .peek(AbstractModule::enable)
                .forEach(module -> this.logger.getLogger().info("Module: [" + module.getModuleName() + "] has been successfully enabled."));
    }

    @Override
    public TS3Api getUnsafeWrapper() {
        return this.ts3Api;
    }

    @Override
    public TS3ApiAsync getUnsafeAsyncWrapper() {
        return this.ts3ApiAsync;
    }

    @Override
    public Optional<MySQLStorage> getMainStorage() {
        return Optional.ofNullable(this.database);
    }

    @Override
    public FireBotAPI getFbotApi() {
        return this.fbotApi;
    }

    @Override
    public DefaultConfiguration getBotConfiguration() {
        return this.botConfiguration;
    }

    @Override
    public ExtendedConfiguration getExtendedConfiguration() {
        return this.extendedConfiguration;
    }

    @Override
    public PrivateChannelsManager getPrivateChannelsManager() {
        return this.channelsSectionManager;
    }

    @Override
    public BotLogger getBotLogger() {
        return this.logger;
    }

    @Override
    public ClientManagerImpl getClientManager() {
        return this.ts3ClientManager;
    }

    @Override
    public ModuleManager getIModuleManager() {
        return this.moduleManager;
    }

    @Override
    public TS3Server getServerHook(){
        return this.serverHook;
    }

    @Override
    public StringUtils getStringUtils() {
        return this.stringUtils;
    }

    @Override
    public EventCaller getEventCaller(){
        return this.eventCaller;
    }

}
