package com.gmail.luxdevpl.fbot.impl.event.events;

public class ClientAddedToServerGroupEvent {

    private int receiverId;
    private int adminId;
    private int groupId;

    public ClientAddedToServerGroupEvent(int receiverId, int adminId, int groupId){
        this.receiverId = receiverId;
        this.adminId = adminId;
        this.groupId = groupId;
    }

    public int getAdminId() {
        return adminId;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getReceiverId() {
        return receiverId;
    }
}
