package com.gmail.luxdevpl.fbot.impl.module.modules.internal;

import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.utils.IOUtils;

import java.util.concurrent.TimeUnit;

public class VersionAnalyzerModuleImpl extends AbstractModule implements Runnable {

    private final String URL = "https://raw.githubusercontent.com/xdev-pl/FBOT/master/updater.txt";

    public VersionAnalyzerModuleImpl(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        super(name, wrapper, configuration);
    }

    @Override
    public void enable() {
        super.getExecutorService().scheduleWithFixedDelay(this, 10,10, TimeUnit.MINUTES);
    }

    @Override
    public void disable() {
        super.getExecutorService().shutdown();
    }

    @Override
    public void performActions(){
        String latest = IOUtils.getContent(URL);

        if (latest != null && !latest.equalsIgnoreCase(getWrapper().getVersion())){
            getWrapper().getUnsafeAsyncWrapper().sendServerMessage("Nowa wersja bota jest dostepna!");
            getWrapper().getUnsafeAsyncWrapper().sendServerMessage("Aktualna: " + getWrapper().getVersion());
            getWrapper().getUnsafeAsyncWrapper().sendServerMessage("Najnowsza: " + latest);
            getWrapper().getUnsafeAsyncWrapper().sendServerMessage("Zalecamy aktualizacje moze ona zawierac powazne fixy blędów!");
            getWrapper().getUnsafeAsyncWrapper().sendServerMessage("https://tsforum.pl/forum/313-aktualizacje/");
        }
    }

    @Override
    public void run(){
        this.performActions();
    }

}
