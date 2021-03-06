package ru.firstset.whereisuser.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import java.util.Timer;
import ru.firstset.whereisuser.util.UtilSharedPreferences;
import ru.firstset.whereisuser.util.NotificationLocation;

public class ServiceLocations extends Service {
    private static final int idLocation = 505050;
    private static final int idLocation2= 505052;
    private static final int TIMER_PERIOD = 60000;
    public static final String NAME_SERVICE = "ServiceLocation";
    private Timer timerLocation;
    LocationTimerTask locationTimerTask;

    public ServiceLocations() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ((flags & START_FLAG_RETRY) == 0) {
            if (timerLocation != null) {
                timerLocation.cancel();
                timerLocation = null;
            }
            timerLocation = new Timer();
            locationTimerTask = new LocationTimerTask();
            timerLocation.schedule(locationTimerTask, 1000, TIMER_PERIOD);//минута
        }
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
