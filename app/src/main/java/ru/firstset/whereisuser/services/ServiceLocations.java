package ru.firstset.whereisuser.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Timer;

import ru.firstset.whereisuser.R;
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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("onStartCommand", "0");

        if ((flags & START_FLAG_RETRY) == 0) {
            if (timerLocation != null) {
                timerLocation.cancel();
                timerLocation = null;
            }
            // Новый таймер
            timerLocation = new Timer();
            locationTimerTask = new LocationTimerTask();
            timerLocation.schedule(locationTimerTask, 1000, TIMER_PERIOD);//минута
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.v("onStartCommand()", "true");
            NotificationLocation notificationLocation = new NotificationLocation(getApplicationContext());

            startForeground(idLocation, notificationLocation.notification);

        }
        else {
            Log.v("onStartCommand()", "false");
            startForeground(idLocation, new Notification());
        }
        return START_STICKY;
    }



//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void startForeground() {
////         NotificationLocation notification = new NotificationLocation(getApplicationContext());
//         NotificationLocation notification = new NotificationLocation(getApplication());
////        startForeground(idLocation2, notification.notification);











//
//
//
//
//
//        int channelId =
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createNotificationChannel("my_service", "My Background Service")
//        } else {
//            // If earlier version channel ID is not used
//            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
//            ""
//        }
//
//        val notificationBuilder = NotificationCompat.Builder(this, channelId )
//        val notification = notificationBuilder.setOngoing(true)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setPriority(PRIORITY_MIN)
//                .setCategory(Notification.CATEGORY_SERVICE)
//                .build()
//        startForeground(101, notification)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel(channelId: String, channelName: String): String{
//        val chan = NotificationChannel(channelId,
//                channelName, NotificationManager.IMPORTANCE_NONE)
//        chan.lightColor = Color.BLUE
//        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        service.createNotificationChannel(chan)
//        return channelId
//    }
//
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            this.notification = new NotificationLocation(getApplicationContext());
//
//            Log.v("startForeground", "start");
//
//            startForeground(idLocation, notification);
//
////            или
//
////            startForegroundService(idLocation, this.notification.notificationManagerCompat);
//        }
//
//
//
//        if ((flags & START_FLAG_RETRY) == 0) {
//            if (timerLocation != null) {
//                timerLocation.cancel();
//                timerLocation = null;
//            }
//            // Новый таймер
//            timerLocation = new Timer();
//            locationTimerTask = new LocationTimerTask();
//            timerLocation.schedule(locationTimerTask, 1000, TIMER_PERIOD);//минута
//        }
//        return Service.START_STICKY;
//    }
//


}
