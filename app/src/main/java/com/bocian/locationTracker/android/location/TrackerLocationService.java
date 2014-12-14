package com.bocian.locationTracker.android.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bocian.locationTracker.android.LocalBinder;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by bocian on 08.12.14.
 */
public class TrackerLocationService extends Service {


    public static boolean IS_RUNNING = false;

    public static final String FORCE_GPS_PREF_KEY = "forceGps";

    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    private TrackerLocationListener trackerLocationListener;
    private LocalBinder<TrackerLocationService> binder;
    private QueuedLocationUpdateListener queuedLocationUpdateListener;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("LocationTracker", "TrackerLocationService.onCreate");

        registerPreferenceListener();

        trackerLocationListener = new TrackerLocationListener();
        binder = new LocalBinder<TrackerLocationService>(this);
        queuedLocationUpdateListener = new QueuedLocationUpdateListener();

        addLocationListener(queuedLocationUpdateListener);
        startListening(isGpsEnabled());

        Log.d("LocationTracker", "TrackerLocationService.onCreate end");
        IS_RUNNING = true;
    }

    public LinkedBlockingQueue<Location> getQueue() {
        return queuedLocationUpdateListener.queue;
    }

    private void registerPreferenceListener() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals(FORCE_GPS_PREF_KEY)) {
                    Log.d("LocationTracker", "sharedPreferenceChangeListener: forceGps changed: " + sharedPreferences.getBoolean(s, false));
                    removeLocationUpdatesListener();
                    startListening(isGpsEnabled());
                }
            }
        };
        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IS_RUNNING = false;

        Log.d("LocationTracker", "TrackerLocationService: destroyed");

        unregisterPreferenceListener();
        removeLocationListener(queuedLocationUpdateListener);
        removeLocationUpdatesListener();
    }

    private void unregisterPreferenceListener() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    private void removeLocationUpdatesListener() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(trackerLocationListener);
    }

    private void startListening(boolean isGpsActive) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (isGpsActive) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, trackerLocationListener);
        }
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1, 1, trackerLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, trackerLocationListener);
    }


    private boolean isGpsEnabled() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean(FORCE_GPS_PREF_KEY, false);
    }

    @Override
    public LocalBinder<TrackerLocationService> onBind(Intent intent) {
        return binder;
    }


    private void addLocationListener(LocationUpdateListener locationUpdateListener) {
        trackerLocationListener.addListener(locationUpdateListener);
    }


    private void removeLocationListener(LocationUpdateListener locationUpdateListener) {
        trackerLocationListener.removeListener(locationUpdateListener);
    }


    private class QueuedLocationUpdateListener implements LocationUpdateListener {

        private LinkedBlockingQueue<Location> queue;

        private Location lastLocation;

        private QueuedLocationUpdateListener() {
            queue = new LinkedBlockingQueue<Location>();
        }

        @Override
        public void handle(Location location) {
            if (lastLocation == null || lastLocation.getTime() != location.getTime()) {
                queue.offer(location);
                lastLocation = location;
            }
        }
    }
}
