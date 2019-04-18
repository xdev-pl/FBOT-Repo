package com.gmail.luxdevpl.fbot.manager;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.gmail.luxdevpl.fbot.api.FireBotAPI;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrivateChannelsManager {

    private final IBotWrapper botWrapper;

    private final ExtendedConfiguration configuration;

    public PrivateChannelsManager(IBotWrapper botWrapper){
        this.botWrapper = botWrapper;

        this.configuration = botWrapper.getExtendedConfiguration();
    }

    public void createFreeChannels(String name, int subChannels, int clientId, int clientDatabaseId) {
        this.checkFreeChannels();

        FireBotAPI fireApi = botWrapper.getFbotApi();

        Channel channel = this.getFreeChannel();

        String date = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(LocalDate.now());

        int number = Integer.valueOf(StringUtils.substringBefore(channel.getName(), "."));

        List<String> description = new ArrayList<>(botWrapper.getExtendedConfiguration().helpCenterModuleSettings.newCreatedPrivateChannelDescription);
        description = botWrapper.getStringUtils().findAndReplace(description, "%date", date);
        description = botWrapper.getStringUtils().findAndReplace(description, "%owner", name);

        Map<ChannelProperty, String> properties = new HashMap<>();
        properties.put(ChannelProperty.CHANNEL_FLAG_MAXCLIENTS_UNLIMITED, "1");
        properties.put(ChannelProperty.CHANNEL_TOPIC, date);
        properties.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
        properties.put(ChannelProperty.CHANNEL_NAME, number + ". " + name);
        properties.put(ChannelProperty.CHANNEL_DESCRIPTION, botWrapper.getStringUtils().parseMessage(description).toString());

        fireApi.editChannel(channel.getId(), properties);

        for (int x = 1; x < subChannels+1; x++) {
            Map<ChannelProperty, String> subChannelProperties = new HashMap<>();
            subChannelProperties.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
            subChannelProperties.put(ChannelProperty.CPID, String.valueOf(channel.getId()));

            fireApi.createChannel(x + ". " + configuration.helpCenterModuleSettings.subchannelStartWith, subChannelProperties);
        }

        botWrapper.getClientManager().getClient(clientId).ifPresent(client -> client.setPrivateChannel(channel.getId()));

        fireApi.moveClient(clientId, channel.getId());
        fireApi.setClientChannelGroup(configuration.helpCenterModuleSettings.channelAdminGroup, channel.getId(), clientDatabaseId);
    }

    private void checkFreeChannels() {
        if(this.getFreeChannel() == null) {
            int existingAmountOfFreeChannels = this.getNextIndexForNewChannel();

            for (int i = 1; i < 6; i++) {
                Map<ChannelProperty, String> properties = new HashMap<>();
                properties.put(ChannelProperty.CHANNEL_FLAG_PERMANENT, "1");
                properties.put(ChannelProperty.CPID, String.valueOf(configuration.helpCenterModuleSettings.startWithPrivateChannelsId));
                properties.put(ChannelProperty.CHANNEL_TOPIC, "free");
                properties.put(ChannelProperty.CHANNEL_MAXCLIENTS, String.valueOf(0));

                botWrapper.getFbotApi().createChannel(existingAmountOfFreeChannels + i + ". Prywatny wolny kanał.", properties);
            }
        }
    }

    private int getNextIndexForNewChannel() {
        return (int) botWrapper.getFbotApi().
                getChannels().
                stream().
                filter(channel -> channel.getParentChannelId() == configuration.helpCenterModuleSettings.startWithPrivateChannelsId).count();
    }

    private Channel getFreeChannel() {
        return botWrapper.getFbotApi().
                getChannels().
                stream().
                filter(channel -> channel.getParentChannelId() == configuration.helpCenterModuleSettings.startWithPrivateChannelsId).
                filter(channel -> channel.getTopic().equals("free")).
                findFirst().orElse(null);
    }

    private void fixOrdering() {
        int parentChannel = 30;

        Map<Integer, Channel> orderMap = botWrapper.getFbotApi().getChannels().stream()
                .filter(c -> c.getParentChannelId() == parentChannel)
                .collect(Collectors.toMap(Channel::getOrder, Function.identity()));

        List<Channel> privateChannels = new ArrayList<>(orderMap.size());

        int predecessor = 0;
        while (orderMap.containsKey(predecessor)) {
            Channel channel = orderMap.get(predecessor);

            privateChannels.add(channel);
            predecessor = channel.getId();
        }

        if (privateChannels.size() != orderMap.size()) {
            throw new IllegalStateException("Could not compute channel list - invalid TS3 channel order?");
        }
    }

}
