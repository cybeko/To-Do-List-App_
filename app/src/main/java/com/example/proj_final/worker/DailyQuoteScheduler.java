package com.example.proj_final.worker;

import android.content.Context;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.ExistingWorkPolicy;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;
public class DailyQuoteScheduler {
    public static void scheduleQuotes(Context context, int hour, int minute, String workName) {
        Calendar now = Calendar.getInstance();
        Calendar due = Calendar.getInstance();
        due.set(Calendar.HOUR_OF_DAY, hour);
        due.set(Calendar.MINUTE, minute);
        due.set(Calendar.SECOND, 0);

        long initialDelay = due.getTimeInMillis() - now.getTimeInMillis();
        if (initialDelay < 0) {
            initialDelay += TimeUnit.DAYS.toMillis(1);
        }

        OneTimeWorkRequest quoteWork = new OneTimeWorkRequest.Builder(DailyQuoteWorker.class)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(context)
                .enqueueUniqueWork(workName, ExistingWorkPolicy.REPLACE, quoteWork);
    }
}
