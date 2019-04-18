package com.gmail.luxdevpl.fbot.listeners.internal;

import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.event.IEventHandler;
import com.gmail.luxdevpl.fbot.impl.event.events.ClientOwnPermissionsUpdateEvent;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import javafx.event.EventHandler;

public class ClientOwnPermissionUpdateListener implements IEventHandler<ClientOwnPermissionsUpdateEvent> {

    @Override
    public void handleEvent(ClientOwnPermissionsUpdateEvent e, IBotWrapper bot){
        bot.getFbotApi().getClientIdByDatabaseId(e.getReceiverId()).ifPresent(receiver -> {
            bot.getFbotApi().kickUser(receiver.getClientId(), "Na serwerze jest zakaz uprawnien na klienta!", true, false);
        });

        bot.getUnsafeAsyncWrapper().deleteClientPermission(e.getReceiverId(), e.getPermissionName());
    }

    @Override
    public EventTypes getEventType(){
        return EventTypes.CLIENT_OWN_PERMISSIONS_EDITED;
    }

}
