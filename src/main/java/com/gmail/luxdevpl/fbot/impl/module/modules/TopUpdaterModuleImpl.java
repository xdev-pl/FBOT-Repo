package com.gmail.luxdevpl.fbot.impl.module.modules;

import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.impl.module.AbstractModule;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;
import com.gmail.luxdevpl.fbot.basic.enums.TopTypes;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TopUpdaterModuleImpl extends AbstractModule implements Runnable {

    private ExtendedConfiguration.TopUpdaterModuleSettings settings;

    public TopUpdaterModuleImpl(String name, IBotWrapper wrapper, ExtendedConfiguration configuration){
        super(name, wrapper, configuration);

        this.settings = getConfiguration().topUpdaterModuleSettings;
    }

    @Override
    public void performActions() {
        this.updateTop(TopTypes.CONNECTIONS, settings.topUpdaterModuleTopIndexAmount);
        this.updateTop(TopTypes.LONGEST_CONNECTION, settings.topUpdaterModuleTopIndexAmount);
        this.updateTop(TopTypes.TIME_SPENT, settings.topUpdaterModuleTopIndexAmount);
    }

    @Override
    public void enable() {
        if(getConfiguration().topUpdaterModuleSettings.topUpdaterModuleStatus){
            super.getExecutorService().scheduleWithFixedDelay(this, 10, settings.topUpdaterModuleInterval, TimeUnit.SECONDS);
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

    private void updateTop(TopTypes topTypes, int indexAmount) {
        StringBuilder stringBuilder = new StringBuilder();

        long startTime = System.currentTimeMillis();

        switch (topTypes) {
            case CONNECTIONS:
                stringBuilder.append("[hr] \n");
                stringBuilder.append("[center][size=14][b]Najwięcej połączeń z Serwerem[/b][/size][/center] \n");
                stringBuilder.append("[hr] \n");

                Map<String, Long> connectionsMap = getWrapper().getMainStorage().get().getTopValues(TopTypes.CONNECTIONS, indexAmount);

                int connectionsIndex = 0;
                for (Map.Entry<String, Long> entry : connectionsMap.entrySet()) {
                    connectionsIndex++;

                    stringBuilder.append("[size=15] ").append(connectionsIndex).append(". [/size] [size=9][img]http://i.imgur.com/FzkNey3.png[/img][b]  Nick: ").append(entry.getKey()).append("[img]http://i.imgur.com/KHZh8nt.png[/img]→ ").append(entry.getValue()).append(" połączeń[/size] \n");
                }
                stringBuilder.append("[hr] \n");
                stringBuilder.append("[right] Wygenerowano w ").append(System.currentTimeMillis() - startTime).append(" ms");

                getWrapper().getFbotApi().editChannelDescription(settings.topUpdaterModuleTopConnectionsChannelid, stringBuilder);
                break;
            case TIME_SPENT:
                stringBuilder.append("[hr] \n");
                stringBuilder.append("[center][size=14][b]Spędzony czas na Serwerze[/b][/size][/center]");
                stringBuilder.append("[hr] \n");

                Map<String, Long> timeSpentMap = getWrapper().getMainStorage().get().getTopValues(TopTypes.TIME_SPENT, indexAmount);

                int timeSpentIndex = 0;
                for (Map.Entry<String, Long> entry : timeSpentMap.entrySet()) {
                    timeSpentIndex++;

                    stringBuilder.append("[size=15] ").append(timeSpentIndex).append(". [/size] [size=9][img]http://i.imgur.com/YDjcxh7.png[/img][b] Nick: ").append(entry.getKey()).append(" [img]http://i.imgur.com/MPmXDnL.png[/img]→ ").append(getWrapper().getStringUtils().getDurationBreakdown(entry.getValue())).append("[/size] \n");
                }
                stringBuilder.append("[hr] \n");
                stringBuilder.append("[right] Wygenerowano w ").append(System.currentTimeMillis() - startTime).append(" ms");

                getWrapper().getFbotApi().editChannelDescription(settings.getTopUpdaterModuleTopTimeSpentChannelid, stringBuilder);
                break;
            case LONGEST_CONNECTION:
                stringBuilder.append("[hr] \n");
                stringBuilder.append("[center][size=14][b]Najdłuższych połączeń z Serwerem[/b][/size][/center]");
                stringBuilder.append("[hr] \n");

                Map<String, Long> map = getWrapper().getMainStorage().get().getTopValues(TopTypes.LONGEST_CONNECTION, indexAmount);

                int longestConnectionIndex = 0;
                for (Map.Entry<String, Long> entry : map.entrySet()) {
                    longestConnectionIndex++;

                    stringBuilder.append("[size=15] ").append(longestConnectionIndex).append(". [/size] [size=9][img]http://i.imgur.com/MKtg445.png[/img][b] Nick: ").append(entry.getKey()).append(" [img]http://i.imgur.com/KHZh8nt.png[/img]→  ").append(getWrapper().getStringUtils().getDurationBreakdown(entry.getValue())).append("[/size] \n");
                }
                stringBuilder.append("[hr] \n");
                stringBuilder.append("[right] Wygenerowano w ").append(System.currentTimeMillis() - startTime).append(" ms");

                getWrapper().getFbotApi().editChannelDescription(settings.topUpdaterModuleTopLongestConnectionsChannelid, stringBuilder);
                break;
        }
    }

}
