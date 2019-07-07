package com.gmail.luxdevpl.fbot.manager;

import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuleManager {

    private List<AbstractModule> modules = new ArrayList<>();

    public void registerModule(AbstractModule simpleModule){
        this.modules.add(simpleModule);
    }

    public void unregisterModule(AbstractModule simpleModule){
        this.modules.remove(simpleModule);
    }

    public List<AbstractModule> getModules(){
        return modules;
    }

    public Optional<AbstractModule> getModuleByName(String name){
        return modules.stream().filter(module -> module.getModuleName().equals(name)).findFirst();
    }

}
