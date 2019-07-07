package com.gmail.luxdevpl.fbot.impl.event;

import java.util.HashSet;
import java.util.Set;

import com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.PrivilegeKeyUsedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.event.IEventHandler;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.listeners.*;
import com.gmail.luxdevpl.fbot.listeners.internal.ClientAddedToServerGroupListener;
import com.gmail.luxdevpl.fbot.listeners.internal.ClientOwnPermissionUpdateListener;
import javafx.event.EventType;

public class EventCaller implements TS3Listener {

    private final IBotWrapper bot;

    private final Set<IEventHandler> eventHandlers;

    public EventCaller(IBotWrapper bot) {
        this.bot = bot;

        this.eventHandlers = new HashSet<>();

        this.initModules();
    }

    private void initModules() {
        this.eventHandlers.add(new ClientJoinServerListener());
        this.eventHandlers.add(new ClientQuitServerListener());
        this.eventHandlers.add(new ClientTextMessageListener());
        this.eventHandlers.add(new ClientChannelJoinListener());
        this.eventHandlers.add(new ChannelDescriptionChangedListener());

        //Internal
        this.eventHandlers.add(new ClientAddedToServerGroupListener());
        this.eventHandlers.add(new ClientOwnPermissionUpdateListener());
    }

    private void handle(Object event, EventTypes type) {
        this.eventHandlers.stream().filter(e -> e.getEventType() == type).forEach(e -> {
            e.handleEvent(event, this.bot);
        });
    }

    public void fireInternalEvent(EventTypes eventEnum, Object event){
        this.handle(event, eventEnum);
    }

    @Override
    public void onTextMessage(TextMessageEvent e) {
        this.handle(e, EventTypes.TEXT_MESSAGE_EVENT);
    }

    @Override
    public void onClientJoin(ClientJoinEvent e) {
        this.handle(e, EventTypes.CLIENT_JOIN_EVENT);
    }

    @Override
    public void onClientLeave(ClientLeaveEvent e) {
        this.handle(e, EventTypes.CLIENT_LEAVE_EVENT);
    }

    @Override
    public void onServerEdit(ServerEditedEvent e) {
        this.handle(e, EventTypes.SERVER_EDITED_EVENT);
    }

    @Override
    public void onChannelEdit(ChannelEditedEvent e) {
        this.handle(e, EventTypes.CHANNEL_EDITED_EVENT);
    }

    @Override
    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
        this.handle(e, EventTypes.CHANNEL_DESCRIPTION_EDITED_EVENT);
    }

    @Override
    public void onClientMoved(ClientMovedEvent e) {
        this.handle(e, EventTypes.CLIENT_MOVED_EVENT);
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent e) {
        this.handle(e, EventTypes.CHANNEL_CREATE_EVENT);
    }

    @Override
    public void onChannelDeleted(ChannelDeletedEvent e) {
        this.handle(e, EventTypes.CHANNEL_DELETED_EVENT);
    }

    @Override
    public void onChannelMoved(ChannelMovedEvent e) {
        this.handle(e, EventTypes.CHANNEL_MOVED_EVENT);
    }

    @Override
    public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
        this.handle(e, EventTypes.CHANNEL_PASSWORD_CHANGED_EVENT);
    }

    @Override
    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {
        this.handle(e, EventTypes.PRIVILEGE_KEY_USED_EVENT);
    }

}