package ru.firstset.whereisuser.permission;

public class AppPermission {
    public AppPermission(String permissionName, int requestCode) {
        this.permissionName = permissionName;
        this.requestCode = requestCode;
    }
    String permissionName;
    int requestCode;
}



