package com.gmail.luxdevpl.fbot.impl.module.modules;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.event.events.ClientAddedToServerGroupEvent;
import com.gmail.luxdevpl.fbot.impl.event.events.ClientOwnPermissionsUpdateEvent;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class AdvandedLogEntrySearcherModule extends AbstractModule implements Runnable {

    private Map<String, String> stringMap = new HashMap<>();

    public AdvandedLogEntrySearcherModule(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        super(name, wrapper, configuration);
    }

    @Override
    public void enable() {
        super.getExecutorService().scheduleWithFixedDelay(this, 7,1, TimeUnit.SECONDS);
    }

    @Override
    public void disable() {
        super.getExecutorService().shutdown();
    }

    @Override
    public void performActions(){
        try {
            List<String> logs = getWrapper().getFbotApi().getVirtualServerLogEntries(5);

            logs.forEach(entry -> {
                if(getConfiguration().clientOwnPermissionUpdateListener.clientOwnPermissionUpdateListenerStatus){
                    if(Stream.of("|permission", "was added by", "to client").allMatch(entry::contains)) {
                        String date = entry.substring(0, 19);

                        if (stringMap.containsKey(date)) {
                            return;
                        }

                        stringMap.put(date, entry);

                        String[] base = StringUtils.substringsBetween(entry, "(id:", ")");

                        ClientOwnPermissionsUpdateEvent clientOwnPermissionsUpdateEvent = new ClientOwnPermissionsUpdateEvent(Integer.parseInt(base[2]), Integer.parseInt(base[1]), StringUtils.substringBetween(entry, "'", "'"));
                        getWrapper().getEventCaller().fireInternalEvent(EventTypes.CLIENT_OWN_PERMISSIONS_EDITED, clientOwnPermissionsUpdateEvent);
                    }
                }

                if (entry.contains("was added to servergroup")) {
                    if (getConfiguration().clientAddedToServerGroupListener.listenerStatus) {
                        String date = entry.substring(0, 19);

                        if (stringMap.containsKey(date)) {
                            return;
                        }

                        stringMap.put(date, entry);

                        String[] base = StringUtils.substringsBetween(entry, "(id:", ")");

                        ClientAddedToServerGroupEvent clientOwnPermissionsUpdateEvent = new ClientAddedToServerGroupEvent(Integer.valueOf(base[0]), Integer.valueOf(base[2]), Integer.parseInt(base[1]));
                        getWrapper().getEventCaller().fireInternalEvent(EventTypes.CLIENT_ADDED_TO_SERVERGROUP, clientOwnPermissionsUpdateEvent);
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        this.performActions();
    }

}
