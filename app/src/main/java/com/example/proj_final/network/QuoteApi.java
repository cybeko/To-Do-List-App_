package com.example.proj_final.network;

import androidx.annotation.NonNull;

import com.example.proj_final.data.quote.Quote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QuoteApi {

    public interface QuoteCallback {
        void onSuccess(Quote quote);
        void onError(Exception e);
    }
    public static void fetchRandomQuote(QuoteCallback callback) {
        new Thread(() -> {
            try {
                URL url = new URL("https://zenquotes.io/api/random");
                JSONArray arr = getJsonArray(url);
                JSONObject obj = arr.getJSONObject(0);

                String quote = obj.getString("q");
                String author = obj.getString("a");

                callback.onSuccess(new Quote(quote, author));

            } catch (Exception e) {
                callback.onError(e);
            }
        }).start();
    }

    @NonNull
    private static JSONArray getJsonArray(URL url) throws IOException, JSONException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        );

        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        reader.close();

        JSONArray arr = new JSONArray(json.toString());
        return arr;
    }
}
