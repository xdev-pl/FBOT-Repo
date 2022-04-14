package com.gmail.luxdevpl.fbot.impl.module;

import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class AbstractModule {

    private final String moduleName;

    private final ExtendedConfiguration configuration;

    private final IBotWrapper wrapper;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public AbstractModule(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        this.moduleName = name;

        this.configuration = configuration;

        this.wrapper = wrapper;
    }

    public final void greet(){
        this.wrapper.getBotLogger().getLogger().info("Module: " + this.moduleName + " has been enabled.");
    }

    public abstract void performActions();

    public abstract void enable();

    public abstract void disable();

    public final void restart(){
        this.disable();
        this.enable();
    }

    public String getModuleName(){
        return moduleName;
    }

    public ExtendedConfiguration getConfiguration(){
        return configuration;
    }

    protected IBotWrapper getWrapper() {
        return wrapper;
    }

    protected ScheduledExecutorService getExecutorService() {
        return executorService;
    }
}
