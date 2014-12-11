package com.bocian.locationTracker.android.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by bocian on 08.12.14.
 */
public class TrackerLocationService extends Service {


    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener;
    private TrackerLocationListener listener = new TrackerLocationListener();

    private IBinder binder = new LocalBinder();


    public class LocalBinder extends Binder {
        public TrackerLocationService getService() {
            return TrackerLocationService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d("LocationTracker", "TrackerLocationService: created");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals("forceGps")) {
                    Log.d("LocationTracker", "sharedPreferenceChangeListener: forceGps changed: " + sharedPreferences.getBoolean(s, false));
                    removeLocationUpdatesListener();
                    addLocationListener(sharedPreferences.getBoolean(s, false));
                }
            }
        };

        preferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        addLocationListener(preferences.getBoolean("forceGps", false));
        registerGpsStatusListener();
    }

    @Override
    public void onDestroy() {
        Log.d("LocationTracker", "TrackerLocationService: destroyed");

        // cleaning up
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.unregisterOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeGpsStatusListener(listener);
        locationManager.removeUpdates(listener);
    }

    private void registerGpsStatusListener() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.addGpsStatusListener(listener);
    }

    private void removeLocationUpdatesListener() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.removeUpdates(listener);
    }

    private void addLocationListener(boolean isGpsActive) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (isGpsActive) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, listener);
        }
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1, 1, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, listener);

    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;

    }
}
