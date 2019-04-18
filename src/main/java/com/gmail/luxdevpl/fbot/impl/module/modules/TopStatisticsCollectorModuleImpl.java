package com.gmail.luxdevpl.fbot.impl.module.modules;

import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.concurrent.TimeUnit;

public class TopStatisticsCollectorModuleImpl extends AbstractModule implements Runnable {

    public TopStatisticsCollectorModuleImpl(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        super(name, wrapper, configuration);
    }

    @Override
    public void performActions() {
        super.getWrapper().getClientManager().getClients().values().forEach(IClient::recalculateStatictics);
    }

    @Override
    public void enable() {
        super.getExecutorService().scheduleWithFixedDelay(this, 1,1, TimeUnit.MINUTES);
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