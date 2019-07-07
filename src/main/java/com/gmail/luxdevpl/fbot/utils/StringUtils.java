package com.gmail.luxdevpl.fbot.utils;

import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.gmail.luxdevpl.fbot.configuration.ExtendedConfiguration;
import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class StringUtils {

    private IBotWrapper bot;

    private ExtendedConfiguration settings;

    public StringUtils(IBotWrapper bot){
        this.bot = bot;

        this.settings = bot.getExtendedConfiguration();
    }

    public List<String> getUsersRecord(){
        List<String> description = settings.channelUpdaterModuleSettings.newRecordChannelDescription;

        description = findAndReplace(description, "%record", String.valueOf(bot.getServerHook().getOnlineRecord()));
        description = findAndReplace(description, "%date", bot.getServerHook().getOnlineRecordTimestamp());

        return description;
    }

    public List<String> getNewUsers(){
        List<String> newUsers = bot.getMainStorage().get().getNewUsers(settings.channelUpdaterModuleSettings.newUsersAmountIndex);
        List<String> description = new ArrayList<>(settings.channelUpdaterModuleSettings.newUsersChannelDescription);

        if(newUsers.size() == 0) {
            return findAndReplace(description, "%newUsers", "[b]Brak nowych uzytkownikow.[/b]");
        }

        description = findAndReplace(description, "%newUsers", parseMessage(newUsers).toString());

        return description;
    }

    public String getFriendlyDate(Calendar date, boolean verbose){
        int year       = date.get(Calendar.YEAR);
        int month      = date.get(Calendar.MONTH);
        int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek  = date.get(Calendar.DAY_OF_WEEK);

        String friendly;

        switch (dayOfWeek) {
            case 1:
                friendly = "Niedziela";
                break;
            case 2:
                friendly = "Poniedziałek";
                break;
            case 3:
                friendly = "Wtorek";
                break;
            case 4:
                friendly = "Środa";
                break;
            case 5:
                friendly = "Czwartek";
                break;
            case 6:
                friendly = "Piątek";
                break;
            case 7:
                friendly = "Sobota";
                break;
            default:
                friendly = "[Blad] Argument nieprawidlowy.";
                break;
        }

        if (verbose) {
            friendly += " dnia " + dayOfMonth;
        } else {
            friendly += ", " + dayOfMonth;
        }

        if (verbose) {
            friendly += " w miesiącu ";
        }

        switch (month) {
            case 0:
                friendly += "Styczn";
                break;
            case 1:
                friendly += "Luty";
                break;
            case 2:
                friendly += "Marzec";
                break;
            case 3:
                friendly += "Kwiecien";
                break;
            case 4:
                friendly += "Maj";
                break;
            case 5:
                friendly += "Czerwiec";
                break;
            case 6:
                friendly += "Lipiec";
                break;
            case 7:
                friendly += "Sierpień";
                break;
            case 8:
                friendly += "Wrzesień";
                break;
            case 9:
                friendly += "Październik";
                break;
            case 10:
                friendly += "Listopad";
                break;
            case 11:
                friendly += "Grudzień";
                break;
            default:
                friendly += "[Blad] Argument nieprawidlowy.";
                break;
        }

        friendly += " " + year;

        return friendly;
    }

    public List<String> findAndReplace(List<String> source, List<String> searchStrings, Object... replacementStrings){
        IntStream.range(0, source.size()).forEach(i -> {
            String sourceLine = source.get(i);
            if (sourceLine.equalsIgnoreCase(searchStrings.get(i))) {
                source.set(i, String.valueOf(replacementStrings[i]));
            }
        });
        return source;
    }

    public List<String> findAndReplace(List<String> list, String searchString, String replacementString){
        int bound = list.size();
        for (int i = 0; i < bound; i++) {
            if (list.get(i).contains(searchString)) {
                list.set(i, list.get(i).replace(searchString, replacementString));
            }
        }
        return list;
    }

    public StringBuilder parseMessage(List<String> strings) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append("\n");
            builder.append(string);
        }
        return builder;
    }

    public String basicVariables(String content){
        try {
            content = content.replaceAll("%online", String.valueOf(bot.getUnsafeWrapper().getClients().size()));
            content = content.replaceAll("%bans", String.valueOf(bot.getUnsafeWrapper().getBans().size()));
            content = content.replaceAll("%channels", String.valueOf(bot.getUnsafeWrapper().getChannels().size()));
            return content;
        } catch (Exception ex) {
            return content;
        }
    }

    public String userVariables(ClientInfo client, String content) {
        try {
            content = content.replaceAll("%totalConnections", String.valueOf(client.getTotalConnections()));
            content = content.replaceAll("%firstConnection", client.getCreatedDate().toString());
            content = content.replaceAll("%lastConnection", client.getLastConnectedDate().toString());
            content = content.replaceAll("%clientIp", String.valueOf(client.getIp()));
            content = content.replaceAll("%clientName", client.getNickname());
            content = content.replaceAll("%clientUid", client.getUniqueIdentifier());
            content = content.replaceAll("%clientCountry", client.getCountry());
            return content;
        } catch (Exception ex) {
            return content;
        }
    }

    public String getDurationBreakdown(long millis){

        if(millis == 0) return "[Blad] Brak danych.";

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        if(days > 0) millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        if(hours > 0) millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

        StringBuilder sb = new StringBuilder();
        if(days > 0){
            sb.append(days);
            long i = days % 10;
            if(i == 1) sb.append(" dzien ");
            else sb.append(" dni ");
        }
        if(hours > 0){
            sb.append(hours);
            sb.append(" godz. ");
        }
        if(minutes > 0){
            sb.append(minutes);
            sb.append(" min ");
        }
        return(sb.toString());
    }

}
