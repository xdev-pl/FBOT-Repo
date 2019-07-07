package com.gmail.luxdevpl.fbot.listeners.internal;

import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.event.IEventHandler;
import com.gmail.luxdevpl.fbot.impl.event.events.ClientAddedToServerGroupEvent;
import com.gmail.luxdevpl.fbot.impl.event.events.ClientOwnPermissionsUpdateEvent;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.ArrayList;
import java.util.Map;

public class ClientAddedToServerGroupListener implements IEventHandler<ClientAddedToServerGroupEvent> {

    @Override
    public void handleEvent(ClientAddedToServerGroupEvent e, IBotWrapper bot){
        bot.getFbotApi().getClientIdByDatabaseId(e.getReceiverId()).ifPresent(receiver -> {
            if (bot.getExtendedConfiguration().clientAddedToServerGroupListener.securedGroups.contains(e.getGroupId())) {
                if (!this.isAuthorized(receiver.getUniqueIdentifier(), e.getGroupId(), bot.getExtendedConfiguration())) {
                    bot.getFbotApi().removeClientFromServerGroup(e.getReceiverId(), e.getGroupId());
                    bot.getFbotApi().kickUser(receiver.getClientId(), "Nie mozesz posiadac tej rangi!", true, false);
                }
            }
        });
    }

    private boolean isAuthorized(String uniqueId, int rankId, ExtendedConfiguration configuration){
        return configuration.clientAddedToServerGroupListener.securedGroupsUids
                .entrySet()
                .stream()
                .anyMatch(entries -> entries.getKey().equals(uniqueId) && entries.getValue().contains(rankId));
    }

    @Override
    public EventTypes getEventType(){
        return EventTypes.CLIENT_ADDED_TO_SERVERGROUP;
    }
}
