package ru.firstset.whereisuser.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import ru.firstset.whereisuser.R;
import static android.app.NotificationManager.IMPORTANCE_HIGH;

public class NotificationLocation extends Notification {
    private final String CHANNEL_NAME = "whereisuser";
    private final String UNIQUE_ID_CHANNEL = "ru.firstset.whereisuser";
    public Notification notification;
    public NotificationManager notificationManager;
    public NotificationChannel notificationChannel;

    @RequiresApi(api = Build.VERSION_CODES.O)

    public NotificationLocation(Context context) {
        initNotification(context);
    }

    @SuppressLint("ServiceCast")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String initNotificationChannel(Context context) {
        notificationChannel = new NotificationChannel(UNIQUE_ID_CHANNEL, CHANNEL_NAME, IMPORTANCE_HIGH);
        notificationChannel.setLightColor( Color.BLUE);
        notificationChannel.setLockscreenVisibility( Notification.VISIBILITY_PRIVATE);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        return UNIQUE_ID_CHANNEL;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initNotification(Context context) {
        String nameChannel = initNotificationChannel(context);
        notification = new NotificationCompat.Builder(context, nameChannel )
                .setPriority(PRIORITY_MAX)
                .setContentTitle(UNIQUE_ID_CHANNEL)
                .setContentText(context.getText(R.string.channel_new_messages))
                .setCategory(Notification.CATEGORY_NAVIGATION)
                .setSmallIcon(R.drawable.ic_message)
                .build();
    }
}


