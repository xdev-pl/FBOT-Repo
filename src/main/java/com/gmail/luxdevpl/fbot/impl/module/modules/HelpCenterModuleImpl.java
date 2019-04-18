package com.gmail.luxdevpl.fbot.impl.module.modules;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HelpCenterModuleImpl extends AbstractModule implements Runnable {

    private final int CHANNEL_ID;
    private final List<Integer> ADMIN_GROUPID;

    private final String HELP_MESSAGE;

    public HelpCenterModuleImpl(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        super(name, wrapper, configuration);

        this.CHANNEL_ID = getWrapper().getExtendedConfiguration().helpCenterModuleSettings.helpCenterAdminChannelid;
        this.ADMIN_GROUPID = getWrapper().getExtendedConfiguration().helpCenterModuleSettings.administratorGroupId;

        this.HELP_MESSAGE = getWrapper().getExtendedConfiguration().helpCenterModuleSettings.someoneNeedHelp;
    }

    @Override
    public void performActions() {
        getWrapper().getAsyncWrapper().ifPresent(wrapper -> {
            try {
                if(getWrapper().getFbotApi().getChannelById(CHANNEL_ID).getTotalClients() > 0){

                    List<Client> clientList = wrapper.getClients().get().stream().filter(Client::isRegularClient).collect(Collectors.toList());

                    List<Client> waitingUsers = clientList.stream().filter(user -> user.getChannelId() == CHANNEL_ID).collect(Collectors.toList());
                    List<Client> admins = this.getAdmins(clientList);

                    if(admins.size() == 0){
                        waitingUsers.forEach(user -> wrapper.sendPrivateMessage(user.getId(), "Aktualnie nie ma administratora ktory moze Ci pomoc."));
                        return;
                    }

                    admins.forEach(admin -> wrapper.pokeClient(admin.getId(), waitingUsers.size() == 1 ? HELP_MESSAGE.replace("%nickname", "[b] [url=client://0/" + waitingUsers.get(0).getUniqueIdentifier() + "] [b]" + waitingUsers.get(0).getNickname() + "[/url]") : "[color=green] [b] Kilka osob oczekuje na twoja pomoc!"));

                    waitingUsers.forEach(user -> {
                        wrapper.sendPrivateMessage(user.getId(), "[b]Administracja zostala powiadomiona o twoim pobycie[/b]");
                        wrapper.sendPrivateMessage(user.getId(), "[color=green] [b] Do dyspozycji masz tych administratorów: [/color] [b]" + admins.stream().map(n -> "[url=client://0/" + n.getUniqueIdentifier() + "] [b]" + n.getNickname() + "[/url]").collect(Collectors.joining(", ")));
                    });

                    clientList.clear();
                    waitingUsers.clear();
                    admins.clear();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private List<Client> getAdmins(List<Client> clients) {
        List<Client> admins = new ArrayList<>();
        clients.forEach(client -> ADMIN_GROUPID.stream().filter(admin -> client.isInServerGroup(admin) && client.getChannelId() != CHANNEL_ID).map(admin -> client).forEach(admins::add));
        return admins;
    }

    @Override
    public void enable() {
        if(getConfiguration().helpCenterModuleSettings.helpCenterModuleStatus){
            super.getExecutorService().scheduleWithFixedDelay(this, 15,getConfiguration().helpCenterModuleSettings.helpCenterModuleInterval, TimeUnit.SECONDS);
        }
    }

    @Override
    public void disable() {
        super.getExecutorService().shutdown();
    }

    @Override
    public void run() {
        this.performActions();
    }

}
