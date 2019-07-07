package com.gmail.luxdevpl.fbot.manager;

import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.basic.enums.RegisterCause;

import java.util.Map;
import java.util.Optional;

public interface IClientManager {

    Optional<IClient> getClient(int clientId);

    Map<Integer, IClient> getClients();

    IClient register(IClient client, RegisterCause registerCause);

    void unregister(IClient client);

}
