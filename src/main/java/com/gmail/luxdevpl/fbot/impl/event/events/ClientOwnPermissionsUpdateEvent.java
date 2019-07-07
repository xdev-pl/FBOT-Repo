package com.gmail.luxdevpl.fbot.impl.event.events;

public class ClientOwnPermissionsUpdateEvent {

    private int receiverId;
    private int adminId;

    private String permissionName;

    public ClientOwnPermissionsUpdateEvent(int receiverId, int adminId, String permissionName){
        this.receiverId = receiverId;
        this.adminId = adminId;
        this.permissionName = permissionName;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public int getReceiverId() {
        return receiverId;
    }

}
