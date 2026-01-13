package com.example.proj_final.notifications;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.proj_final.MainActivity;
import com.example.proj_final.R;
public class QuoteNotifier {

    private static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID = "todo_reminder_channel";

    public static void showQuoteNotification(Context context, String quote, String author) {
        createNotificationChannel(context);

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_quote)
                        .setContentTitle(context.getString(R.string.notification_title))
                        .setContentText("“" + quote + "” — " + author)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("“" + quote + "”\n— " + author))
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationManagerCompat
                .from(context)
                .notify(NOTIFICATION_ID, builder.build());
    }
    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Quotes & Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Quote notifications and reminders");

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
