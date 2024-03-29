package com.gmail.luxdevpl.fbot.impl.module.modules;

import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.gmail.luxdevpl.fbot.basic.IClient;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PrivateSectionAnalyzerModuleImpl extends AbstractModule implements Runnable {

    public PrivateSectionAnalyzerModuleImpl(String name, IBotWrapper wrapper, ExtendedConfiguration configuration) {
        super(name, wrapper, configuration);
    }

    @Override
    public void performActions() {
        List<Channel> privateChannelList = getWrapper().getFbotApi().getChannels()
                .stream()
                .filter(channel -> channel.getParentChannelId() == getWrapper().getExtendedConfiguration().helpCenterModuleSettings.privateChannelAssignerModule.startWithPrivateChannelsId)
                .filter(channel -> !channel.getTopic().equals("free"))
                .collect(Collectors.toList());

        privateChannelList.forEach(key -> {
            LocalDate date = LocalDate.parse(key.getTopic() , DateTimeFormatter.ofPattern("dd-MM-yyyy"));

            long daysBetween = ChronoUnit.DAYS.between(date, LocalDate.now());

            int number = Integer.parseInt(StringUtils.substringBefore(key.getName(), "."));

            if(daysBetween >= 4 && daysBetween < 7) {
                getWrapper().getFbotApi().editChannelName(key.getId(), number + ". " + "Kanał do zwolnienia.");
                return;
            }

            if(daysBetween >= 7) {
                getWrapper().getFbotApi().editChannelName(key.getId(), number + ". Prywatny wolny kanał");
                getWrapper().getFbotApi().editChannelTopic(key.getId(), "free");
                getWrapper().getFbotApi().editChannel(key.getId(), Collections.singletonMap(ChannelProperty.CHANNEL_MAXCLIENTS, "0"));
                getWrapper().getFbotApi().removeSubchannels(key);
                getWrapper().getFbotApi().removeChannelPermissions(key.getId());
                getWrapper().getFbotApi().editChannelDescription(key.getId(), "");

            }
        });
    }

    @Override
    public void enable() {
        super.getExecutorService().scheduleWithFixedDelay(this, 1, 5, TimeUnit.MINUTES);
    }

    @Override
    public void disable() {
        super.getExecutorService().shutdown();
    }

    @Override
    public void run() {
        try {
            this.performActions();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private int getHoursUntilTarget(int targetHour) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour < targetHour ? targetHour - hour : targetHour - hour + 24;
    }
}