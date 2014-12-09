package com.bocian.locationTracker.android;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by bocian on 08.12.14.
 */
public class TrackerLocationService extends Service{


    @Override
    public void onCreate() {
        Log.d("LocationTracker", "TrackerLocationService: created");
    }

    @Override
    public void onDestroy() {
        Log.d("LocationTracker", "TrackerLocationService: destroyed");
    }




    public static void addListener(Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        // only if GPS
        TrackerLocationListener listener = new TrackerLocationListener();
        locationManager.addGpsStatusListener(listener);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1, 1, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, listener);

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
