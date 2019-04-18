package com.gmail.luxdevpl.fbot.impl.module;

import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class AbstractModule {

    private String moduleName;

    private ExtendedConfiguration configuration;

    private IBotWrapper wrapper;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public AbstractModule(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        this.moduleName = name;

        this.configuration = configuration;

        this.wrapper = wrapper;
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
