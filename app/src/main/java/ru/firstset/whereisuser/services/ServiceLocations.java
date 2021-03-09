package ru.firstset.whereisuser.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;

import ru.firstset.whereisuser.util.UtilSharedPreferences;
import ru.firstset.whereisuser.util.NotificationLocation;

public class ServiceLocations extends Service {
    private static final int idLocation = 505050;

    public ServiceLocations() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        UtilSharedPreferences utilSharedPreferences = new UtilSharedPreferences(sharedPreferences);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationLocation notificationLocation = new NotificationLocation(getApplicationContext());
            startForeground(idLocation, notificationLocation.notification);
        } else {
            startForeground(idLocation, new Notification());
        }
        return START_STICKY;
    }

}
