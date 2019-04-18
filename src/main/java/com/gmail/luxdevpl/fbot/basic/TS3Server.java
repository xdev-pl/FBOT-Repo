package com.gmail.luxdevpl.fbot.basic;

import com.gmail.luxdevpl.fbot.launcher.IBotWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class TS3Server {

    private final IBotWrapper botWrapper;

    private int registredClientAmount;
    private int onlineRecord;

    private String onlineRecordTimestamp;

    public TS3Server(IBotWrapper botWrapper){
        this.botWrapper = botWrapper;
    }

    public void load(){
        this.botWrapper.getMainStorage().ifPresent(storage -> {
            storage.loadServerInfo(this);
        });
    }

    public void save(){
        this.botWrapper.getMainStorage().ifPresent(storage -> {
            storage.saveServerInfo(this);
        });
    }

    public void deserialize(ResultSet resultSet){
        try {
            this.registredClientAmount = resultSet.getInt("amountOfClients");
            this.onlineRecord = resultSet.getInt("onlineRecord");

            this.onlineRecordTimestamp = resultSet.getString("onlineRecordDate");

            if(registredClientAmount == 0){
                this.registredClientAmount = botWrapper.getFbotApi().getDatabaseClients().size();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateStatistics(){
        int online = botWrapper.getFbotApi().getOnlineClients();

        if(online > this.onlineRecord){
            this.onlineRecord = online;

            this.onlineRecordTimestamp = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDate.now());

            if(botWrapper.getExtendedConfiguration().channelUpdaterModuleSettings.usersRecordChannelStatus) {
                botWrapper.getFbotApi().editChannelDescription(botWrapper.getExtendedConfiguration().channelUpdaterModuleSettings.newRecordChannelId, botWrapper.getStringUtils().getUsersRecord());
            }
        }
    }

    public int getRegistredClientAmount() {
        return registredClientAmount;
    }

    public int getOnlineRecord() {
        return onlineRecord;
    }

    public void addNewRegistredUser() {
        this.registredClientAmount++;
    }

    public String getOnlineRecordTimestamp() {
        return onlineRecordTimestamp;
    }
}
