package com.example.proj_final.worker;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.proj_final.data.Quote;
import com.example.proj_final.network.QuoteApi;
import com.example.proj_final.ui.QuoteNotifier;

public class DailyQuoteWorker extends Worker {
    public DailyQuoteWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }
    @NonNull
    @Override
    public Result doWork() {
        QuoteApi.fetchRandomQuote(new QuoteApi.QuoteCallback() {
            @Override
            public void onSuccess(Quote quote) {
                QuoteNotifier.showQuoteNotification(getApplicationContext(), quote.text, quote.author);
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
        return Result.success();
    }
}