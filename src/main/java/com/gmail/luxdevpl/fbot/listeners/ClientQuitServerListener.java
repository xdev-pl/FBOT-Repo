package com.gmail.luxdevpl.fbot.listeners;

import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.gmail.luxdevpl.fbot.event.IEventHandler;
import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

public class ClientQuitServerListener implements IEventHandler<ClientLeaveEvent> {

    @Override
    public void handleEvent(ClientLeaveEvent event, IBotWrapper bot) {
        bot.getClientManager().getClient(event.getClientId()).ifPresent(client -> {
            if (bot.getExtendedConfiguration().adminUpdaterModuleSettings.staffUids.containsKey(client.getUniqueIdentifier())) {
                bot.getFbotApi().updateAdministrativeChannel(client.getUniqueIdentifier());
            }

            client.handleDisconnect();

            bot.getMainStorage().ifPresent(database -> database.saveClientInfo(client));

            bot.getClientManager().unregister(client);
        });
    }

    @Override
    public EventTypes getEventType() {
        return EventTypes.CLIENT_LEAVE_EVENT;
    }
}
