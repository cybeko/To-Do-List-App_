package com.example.proj_final.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.proj_final.R;

public class QuoteNotifier {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "todo_reminder_channel";

    public static void showQuoteNotification(Context context, String quote, String author) {
        Intent intent = new Intent(context, context.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_quote)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText("“" + quote + "” — " + author)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("“" + quote + "”\n— " + author))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build());
    }
}