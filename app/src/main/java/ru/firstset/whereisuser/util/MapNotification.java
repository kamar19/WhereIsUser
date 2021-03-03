package ru.firstset.whereisuser.util;

import ru.firstset.whereisuser.location.Location;

interface Notifications {
    void initialize();
    void showNotification(Location location);
}
public class MapNotification implements Notifications {
    private  final String NEW_LOCATION = "User moved";
    private final  int REQUEST_CONTENT = 1;
    private final String LOCATION_TAG = "new_movie";

    @Override
    public void initialize() {
        if (notificationManagerCompat.getNotificationChannel(CHANNEL_NEW_MESSAGES) == null) {
            notificationManagerCompat.createNotificationChannel(
                    NotificationChannelCompat.Builder(CHANNEL_NEW_MESSAGES, IMPORTANCE_HIGH)
                            .setName(context.getString(R.string.channel_new_messages))
                            .setDescription(context.getString(R.string.channel_new_messages_description))
                            .build()
            )
        }
    }

    @Override
    public void showNotification(Location location) {

    }
}
