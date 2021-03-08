package ru.firstset.whereisuser.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
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
        Log.v("checkPermission", "0");
        Log.v("checkPermission", String.valueOf(permissions.size()));
        boolean boolPermissions = true;
        for (int i = 0; i < permissions.size() - 1; i++) {
            Log.v("permissionName", permissions.get(i).permissionName);
            Log.v("this.activity", permissions.get(i).permissionName);

            if (checkSelfPermission(this.activity, permissions.get(i).permissionName) != PermissionChecker.PERMISSION_GRANTED)
                boolPermissions = false;
        }
        return boolPermissions;
    }

    public void requestPermission() {
        Log.v("requestPermission", String.valueOf(permissions.size()));

        for (int i = 0; i < permissions.size() - 1; i++) {
            ActivityCompat.requestPermissions(this.activity,
                    new String[]{permissions.get(i).permissionName},
                    permissions.get(i).requestCode);
        }
    }
}
