package com.gmail.luxdevpl.fbot.impl.manager;

import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.basic.enums.RegisterCause;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.manager.IClientManager;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManagerImpl implements IClientManager {

    private Map<Integer, IClient> ts3ClientMap = new ConcurrentHashMap<>(16, 0.9F, 1);

    private IBotWrapper botWrapper;

    public ClientManagerImpl(IBotWrapper botWrapper){
        this.botWrapper = botWrapper;
    }

    @Override
    public Optional<IClient> getClient(int clientId) {
        return Optional.ofNullable(this.ts3ClientMap.get(clientId));
    }

    @Override
    public Map<Integer, IClient> getClients() {
        return this.ts3ClientMap;
    }


    @Override
    public IClient register(IClient client, RegisterCause registerCause) {
        this.ts3ClientMap.put(client.getClientId(), client);

        botWrapper.getMainStorage().ifPresent(storage -> storage.loadClientInfo(client));

        client.handleLogin(registerCause);

        return client;
    }

    @Override
    public void unregister(IClient client) {
        this.ts3ClientMap.remove(client.getClientId());
    }

}
