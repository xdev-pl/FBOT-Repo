package com.gmail.luxdevpl.fbot.impl.module.modules.internal;

import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;

import java.util.concurrent.TimeUnit;

public class DataSaveModuleImpl extends AbstractModule implements Runnable {

    public DataSaveModuleImpl(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        super(name, wrapper, configuration);
    }

    @Override
    public void performActions() {
        getWrapper().getBotLogger().getLogger().info("Saving user & server data..");
        getWrapper().getClientManager().getClients().values().forEach(client -> getWrapper().getMainStorage().ifPresent(database -> database.saveClientInfo(client)));
        getWrapper().getServerHook().save();
        getWrapper().getBotLogger().getLogger().info("Done.");
    }

    @Override
    public void enable() {
        super.getExecutorService().scheduleWithFixedDelay(this, 5,10, TimeUnit.MINUTES);
    }

    @Override
    public void disable() {
        super.getExecutorService().shutdown();
    }

    @Override
    public void run() {
        this.performActions();
    }

}
