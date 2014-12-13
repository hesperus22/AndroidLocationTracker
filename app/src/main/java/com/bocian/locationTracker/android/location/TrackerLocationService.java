package com.bocian.locationTracker.android.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by bocian on 08.12.14.
 */
public class TrackerLocationService extends Service {


    public static boolean IS_RUNNING = false;

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    private TrackerLocationListener listener;

    private IBinder binder = new LocalBinder();
    private GpsStatus.Listener gpsStatusListener;
    private LocationUpdateListener addLocationListener;

    public void addLocationListener(LocationUpdateListener locationUpdateListener) {
        listener.addListener(locationUpdateListener);
    }

    public void removeLocationListener(LocationUpdateListener locationUpdateListener) {
        listener.removeListener(locationUpdateListener);
    }


    public class LocalBinder extends Binder {
        public TrackerLocationService getService() {
            return TrackerLocationService.this;
        }
    }

    LinkedBlockingQueue<Location> queue;

    public LinkedBlockingQueue<Location> getQueue() {
        return queue;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LocationTracker", "TrackerLocationService.onCreate");


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//                if (s.equals("forceGps")) {
//                    Log.d("LocationTracker", "sharedPreferenceChangeListener: forceGps changed: " + sharedPreferences.getBoolean(s, false));
//                    removeLocationUpdatesListener();
//                    addLocationListener(sharedPreferences.getBoolean(s, false));
//                }
            }
        };

        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        listener = new TrackerLocationListener();
        gpsStatusListener = new GpsStatusListener();


        queue = new LinkedBlockingQueue<Location>();

        addLocationListener= new MyLocationUpdateListener();
        addLocationListener(addLocationListener);

        addLocationListener(true);
//        registerGpsStatusListener();

        Log.d("LocationTracker", "TrackerLocationService.onCreate end");
        IS_RUNNING = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_RUNNING = false;

        Log.d("LocationTracker", "TrackerLocationService: destroyed");

        removeLocationListener(addLocationListener);

        // cleaning up
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeGpsStatusListener(gpsStatusListener);
        locationManager.removeUpdates(listener);
    }

    private void registerGpsStatusListener() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.addGpsStatusListener(gpsStatusListener);
    }

    private void removeLocationUpdatesListener() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.removeUpdates(listener);
    }

    private void addLocationListener(boolean isGpsActive) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (isGpsActive) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, listener);
        } else {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1, 1, listener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, listener);
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class GpsStatusListener implements GpsStatus.Listener {

        @Override
        public void onGpsStatusChanged(int event) {
            Log.d("LocationTracker", "TrackerLocationListener.onGpsStatusChanged: " + event);
            if (event == GpsStatus.GPS_EVENT_STOPPED) {
                removeLocationUpdatesListener();
                addLocationListener(false);
            }
            if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {
                removeLocationUpdatesListener();
                addLocationListener(true);
            }
        }
    }

    class MyLocationUpdateListener implements LocationUpdateListener {
        @Override
        public void handle(Location location) {
            queue.offer(location);
        }
    }
}
