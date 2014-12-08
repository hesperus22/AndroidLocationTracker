package com.bocian.locationTracker.android;

import android.content.Context;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by bocian on 08.12.14.
 */
public class TrackerLocationManager {

    private TrackerLocationManager() {

    }

    public static void addListener(Context ctx) {
        LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        // only if GPS
        TrackerLocationListener listener = new TrackerLocationListener();
        locationManager.addGpsStatusListener(listener);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1, 1, listener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, listener);
    }


}
