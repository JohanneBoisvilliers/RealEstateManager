package com.openclassrooms.realestatemanager.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.login.LoginActivity;

public class NotificationsService {
    private static final int NOTIFICATION_ID = 007;
    private static String CHANNEL_ID = "addRealEstate";

    public static void sendNotification() {

        createNotificationChannel();

        Intent snoozeIntent = new Intent(MyApp.getContext(), LoginActivity.class);
        snoozeIntent.putExtra("snooze", 0);
        PendingIntent snoozePendingIntent =
                PendingIntent.getBroadcast(MyApp.getContext(), 0, snoozeIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApp.getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_add_24)
                .setContentTitle("Real Estate Manager")
                .setContentText("You've added a new real estate in database !")
                .setContentIntent(snoozePendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyApp.getContext());

// notificationId is a unique int for each notification that you must define
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
