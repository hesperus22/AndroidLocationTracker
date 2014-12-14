package com.bocian.locationTracker.android.communication;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.bocian.locationTracker.android.LocalBinder;
import com.bocian.locationTracker.android.location.TrackerLocationService;
import com.google.gson.Gson;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class CommunicationService extends IntentService {

    public static final String LOCK_NAME_STATIC = "com.bocian.locationTracker.android.STATIC_LOCK";
    private static PowerManager.WakeLock lockStatic = null;
    private TrackerLocationServiceConnection conn;

    private TrackerLocationService trackerLocationService;


    public CommunicationService() {
        super("CommunicationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LocationTracker", "CommunicationService.onCreate");

        bindToTrackerLocationService();

        Log.d("LocationTracker", "CommunicationService.onCreate end");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    Semaphore s = new Semaphore(1);

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("LocationTracker", "CommunicationService: onHandleIntent");


        Log.d("LocationTracker", "CommunicationService: onHandleIntent end");

//        lockStatic.release();
    }

    public static void acquireStaticLock(Context context) {
        getLock(context).acquire();
    }

    private void bindToTrackerLocationService() {


        Log.d("LocationTracker", "CommunicationService.bindToTrackerLocationService");
        conn = new TrackerLocationServiceConnection();
        Intent intent = new Intent(this, TrackerLocationService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Log.d("LocationTracker", "CommunicationService.bindToTrackerLocationService end");
    }

    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic == null) {
            PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }

        return (lockStatic);
    }


    class TrackerLocationServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("LocationTracker", "TrackerLocationServiceConnection: onServiceConnected");
            trackerLocationService = ((LocalBinder<TrackerLocationService>) iBinder).getService();

            List<Location> locations = new ArrayList<Location>();

            LinkedBlockingQueue<Location> queue = trackerLocationService.getQueue();
            while (!queue.isEmpty()) {
                Location poll = queue.poll();
                locations.add(poll);
                Log.d("LocationTracker", "CommunicationService: onHandleIntent polled:" + poll);
            }

            if (!locations.isEmpty()) {
                Gson gson = new Gson();
                final String toJson = gson.toJson(locations);
                Log.d("LocationTracker", "TrackerLocationServiceConnection: " + toJson);

                AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            Log.e("LocationTracker", "sending.......................");

                            URL url = new URL("http://192.168.43.50:1337/");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestProperty("Accept", "application/json");
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            connection.setFixedLengthStreamingMode(toJson.getBytes("UTF8").length);
                            OutputStream outputStream = new BufferedOutputStream(connection.getOutputStream());
                            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                            writer.write(toJson);
                            writer.flush();
                            writer.close();
                        } catch (IOException e) {
                            Log.e("LocationTracker", e.toString());
                        }
                        return null;
                    }
                };
                task.execute();
            }

            Log.d("LocationTracker", "TrackerLocationServiceConnection: onServiceConnected end");

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("LocationTracker", "TrackerLocationServiceConnection: onServiceDisconnected");
            trackerLocationService = null;
        }
    }
}