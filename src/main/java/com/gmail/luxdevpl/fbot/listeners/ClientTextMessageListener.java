package com.gmail.luxdevpl.fbot.listeners;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.gmail.luxdevpl.fbot.api.FireBotAPI;
import com.gmail.luxdevpl.fbot.basic.enums.EventTypes;
import com.gmail.luxdevpl.fbot.event.IEventHandler;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ClientTextMessageListener implements IEventHandler<TextMessageEvent> {

    @Override
    public void handleEvent(TextMessageEvent event, IBotWrapper bot) {
        if (event.getTargetMode() == TextMessageTargetMode.CLIENT) {
            String message = event.getMessage();

            if (message.equalsIgnoreCase("memdump")) {
                bot.getAsyncWrapper().ifPresent(wrapper -> {
                    wrapper.sendPrivateMessage(event.getInvokerId(), "##### Heap utilization statistics [MB] #####");
                    int mb = 1024 * 1024;

                    Runtime instance = Runtime.getRuntime();
                    wrapper.sendPrivateMessage(event.getInvokerId(), "Total memory: " + instance.totalMemory() / mb + " MB");
                    wrapper.sendPrivateMessage(event.getInvokerId(), "Free memory: " + instance.freeMemory() / mb + " MB");
                    wrapper.sendPrivateMessage(event.getInvokerId(), "Used memory: " + (instance.totalMemory() - instance.freeMemory()) / mb + " MB");
                    wrapper.sendPrivateMessage(event.getInvokerId(), "Max Memory: " + instance.maxMemory() / mb + " MB");
                    wrapper.sendPrivateMessage(event.getInvokerId(), " ");
                    wrapper.sendPrivateMessage(event.getInvokerId(), "W razie problemow skontaktuj sie z autorem.");
                });
            }

            if(message.startsWith("module")){
                ClientInfo clientInfo = bot.getFbotApi().getClientByClientId(event.getInvokerId());

                if(clientInfo.isInServerGroup(532)) {
                    bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), "module <module> <list|reload|start|disable|performActions>");

                    String[] args = event.getMessage().split(" ");

                    if (args[1].equalsIgnoreCase("list")) {
                        bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), "Lista modułow");
                        bot.getIModuleManager().getModules().forEach(module -> bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), module.getModuleName()));
                        return;
                    }

                    if (args[1].equalsIgnoreCase("disable")) {
                        AbstractModule module = bot.getIModuleManager().getModuleByName(args[2]).get();

                        module.disable();
                        bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), "wylaczono");
                    }

                    if (args[1].equalsIgnoreCase("enable")) {
                        AbstractModule module = bot.getIModuleManager().getModuleByName(args[2]).get();

                        module.enable();
                        bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), "wylaczono");
                    }

                    if (args[1].equalsIgnoreCase("restart")) {
                        AbstractModule module = bot.getIModuleManager().getModuleByName(args[2]).get();

                        module.restart();
                        bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), "wylaczono");
                    }

                    if (args[1].equalsIgnoreCase("performActions")) {
                        AbstractModule module = bot.getIModuleManager().getModuleByName(args[2]).get();

                        module.performActions();

                        bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), "Wykonano fukcje.");
                    }
                }
            }

            if(message.startsWith("friends")){
                bot.getClientManager().getClient(event.getInvokerId()).ifPresent(client -> {
                    for(Map.Entry<String, Long> friendsMap : client.getBestFriends().entrySet()){
                        bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), "> " + friendsMap.getKey() + " czas: " + bot.getStringUtils().getDurationBreakdown(friendsMap.getValue()));
                    }
                });
            }

            if (message.startsWith("!teleport")) {
                String[] args = event.getMessage().split(" ");

                if(args[1] != null){
                    if(bot.getExtendedConfiguration().clanTeleportFunctionSettings.clansMap.get(args[1]) != null){
                        bot.getFbotApi().moveClient(event.getInvokerId(), bot.getExtendedConfiguration().clanTeleportFunctionSettings.clansMap.get(args[1]));
                    } else {
                        bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), "Nie ma takiego klanu, sprawdz czy na pewno dobrze napisales.");
                    }
                } else {
                    bot.getUnsafeAsyncWrapper().sendPrivateMessage(event.getInvokerId(), "Nie podales nazwy klanu!");
                }
            }
        }
    }


    @Override
    public EventTypes getEventType() {
        return EventTypes.TEXT_MESSAGE_EVENT;
    }

}
