package ru.firstset.whereisuser.permission;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class RequestPermissions {
    private ArrayList<AppPermission> permissions = new ArrayList<AppPermission>();
    private Activity activity;

    public RequestPermissions(Activity activity) {
        this.activity = activity;
        this.permissions.add(new AppPermission(Manifest.permission.ACCESS_COARSE_LOCATION, 100));
        this.permissions.add(new AppPermission(Manifest.permission.ACCESS_FINE_LOCATION, 200));
    }

    public Boolean checkPermission() {
        boolean boolPermissions = true;
        for (int i = 0; i < permissions.size(); i++) {
            if (checkSelfPermission(this.activity, permissions.get(i).permissionName) != PermissionChecker.PERMISSION_GRANTED) {
                Log.v("checkSelfPermission", permissions.get(i).permissionName);
                boolPermissions = false;
            }
        }
        return boolPermissions;
    }

    public void requestPermission() {
        if (checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        if (checkSelfPermission(this.activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PermissionChecker.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }

    }
}
