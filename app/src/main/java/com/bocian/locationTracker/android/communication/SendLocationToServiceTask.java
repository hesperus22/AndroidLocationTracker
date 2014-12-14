package com.bocian.locationTracker.android.communication;

import android.os.AsyncTask;
import android.util.Log;

import com.bocian.locationTracker.android.location.LocationStore;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bocian on 14.12.14.
 */
public class SendLocationToServiceTask extends AsyncTask<Void, Void, Boolean> {

    private LocationStore store;
    private String serverUrl;

    SendLocationToServiceTask(LocationStore store, String serverUrl) {
        this.store = store;
        this.serverUrl = serverUrl;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Log.d("LocationTracker", "SendLocationToServiceTask locking");

            store.lock();
            Log.d("LocationTracker", "SendLocationToServiceTask sending " + store.getAll().size());

            String serializedLocations = serialize();

            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(serializedLocations.getBytes().length);
            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
            outputStream.write(serializedLocations.getBytes());
            outputStream.close();
        } catch (IOException e) {
            Log.e("LocationTracker", e.toString());
            store.unlock();
            return false;
        }
        store.clear();
        store.unlock();
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        super.onPostExecute(aVoid);
    }

    private String serialize() {
        Gson gson = new Gson();
        return gson.toJson(store.getAll());
    }
}
