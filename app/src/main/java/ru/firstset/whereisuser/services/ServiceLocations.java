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
    private static final int TIMER_PERIOD = 60000;
    public static final String NAME_SERVICE = "ServiceLocation";
    private Timer timerLocation;
    LocationTimerTask locationTimerTask;

    public ServiceLocations() {
        super();
    }

    private NotificationLocation notification;
    private static final String intentExtraNameLatitude = "latitude";
    private static final String intentExtraNamelongitude = "longitude";
    private final Double defaultLocationLatitude = -33.8523341;
    private final Double defaultLocationLongitude = 151.2106085;

//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        assert intent != null;
//        Double latitude = intent.getDoubleExtra(intentExtraNameLatitude, defaultLocationLatitude);
//        Double longitude = intent.getDoubleExtra(intentExtraNamelongitude, defaultLocationLongitude);
//        Log.v(NAME_SERVICE, getString(R.string.latitude_messages) + latitude + getString(R.string.longitude_messages) + longitude);
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForeground();
        else
            startForeground(505050, new Notification());
        return START_NOT_STICKY;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForeground() {
         NotificationLocation notification = new NotificationLocation(getApplicationContext());
        startForeground(5050502, notification.notification);
    }
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
