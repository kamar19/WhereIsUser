package ru.firstset.whereisuser.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ru.firstset.whereisuser.R;
import ru.firstset.whereisuser.location.Location;
import ru.firstset.whereisuser.services.ServiceLocations;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class NotificationLocation extends Notification {
    private final String CHANNEL_NAME = "whereisuser";
    private final int REQUEST_CONTENT = 1;
    private final String LOCATION_TAG = "new_location";
    private final String UNIQUE_ID_CHANNEL = "ru.firstset.whereisuser";
    public Notification notification;

    //    public NotificationCompat.Builder notificationBuilder;
    public NotificationManager notificationManager;
    private NotificationChannel notificationChannel;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)

    public NotificationLocation(Context context) {
        this.context = context;
        initializeNotificationChannel();
        initializeNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)

    public void initializeNotificationChannel() {
        Log.v("initialize", "0");

        notificationChannel = null;
//        if (notificationManager.getNotificationChannel(CHANNEL_NAME) == null) {
        notificationChannel = new NotificationChannel(UNIQUE_ID_CHANNEL, CHANNEL_NAME, IMPORTANCE_HIGH);
        Log.v("initialize", "1");


        notificationManager = (NotificationManager) context.getSystemService(String.valueOf(context));
        Log.v("initialize", "2");

        notificationManager.createNotificationChannel(notificationChannel);
//            notificationChannel.setName(context.getString(R.string.channel_new_messages));
//            notificationChannel.setDescription(context.getString(R.string.channel_new_messages_description));
    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel(channelId: String, channelName: String): String{
////        val chan = NotificationChannel(channelId,
////                channelName, NotificationManager.IMPORTANCE_NONE)
//        chan.lightColor = Color.BLUE
//        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
//        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        service.createNotificationChannel(chan)
//        return channelId
//    }

    public void initializeNotification() {
//        Uri contentUri = Uri.parse("whereisuser://ru.firstset.whereisuser/location/${location.id}");
//        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_NEW_MESSAGES);
//        notification = notificationBuilder.setOngoing(true)
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_NAME);
        notification = notificationBuilder.setOngoing(true)
                .setContentTitle(context.getString(R.string.content_title))
                .setSmallIcon(R.drawable.ic_message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

//        notificationManager.notify(LOCATION_TAG, location.id, notificationBuilder.build());
    }
}
