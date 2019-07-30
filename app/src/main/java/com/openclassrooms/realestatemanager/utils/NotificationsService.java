package com.openclassrooms.realestatemanager.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.MainActivity;

public class NotificationsService {
    private static final int NOTIFICATION_ID = 007;
    private static String CHANNEL_ID = "addRealEstate";

    public static void sendNotification() {

        createNotificationChannel();

        Intent notificationIntent = new Intent(MyApp.getContext(), MainActivity.class);
        notificationIntent.putExtra("ComeFrom", "Notification");
        PendingIntent notificationPendingIntent =
                PendingIntent.getBroadcast(MyApp.getContext(), 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApp.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_add_24)
                .setContentTitle(MyApp.getContext().getString(R.string.notification_title))
                .setContentText(MyApp.getContext().getString(R.string.notification_message))
                .setContentIntent(notificationPendingIntent)
                .setTimeoutAfter(5 * 60 * 1000)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyApp.getContext());

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    public static void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = MyApp.getContext().getResources().getString(R.string.notification_name);
            String description = MyApp.getContext().getResources().getString(R.string.notification_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =
                    MyApp.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
