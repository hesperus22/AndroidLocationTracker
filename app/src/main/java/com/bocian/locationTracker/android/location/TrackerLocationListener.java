package com.bocian.locationTracker.android.location;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bocian on 08.12.14.
 */
public class TrackerLocationListener implements LocationListener, GpsStatus.Listener {

    private List<Location> locations = new ArrayList<Location>();
    private boolean flagGps;


    @Override
    public synchronized void onGpsStatusChanged(int event) {
        Log.d("LocationTracker", "TrackerLocationListener.onGpsStatusChanged: " + event);
        if (event == GpsStatus.GPS_EVENT_STOPPED) {
            flagGps = false;
        }
        if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {
            flagGps = true;
        }
        Log.d("LocationTracker", "TrackerLocationListener.onGpsStatusChanged: flagGps=" + flagGps);

    }

    @Override
    public synchronized void onLocationChanged(Location location) {
        Log.d("LocationTracker", "TrackerLocationListener.onLocationChanged: " + location);

        if (flagGps && !location.getProvider().equals("gps")) {
            Log.d("LocationTracker", "TrackerLocationListener.onLocationChanged: location from GPS, other is not needed");
            return;
        }
        locations.add(location);
    }

    @Override
    public synchronized void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("LocationTracker", "TrackerLocationListener.onStatusChanged: " + provider + " " + status);

    }

    @Override
    public synchronized void onProviderEnabled(String provider) {
        Log.d("LocationTracker", "TrackerLocationListener.onProviderEnabled: " + provider);

    }

    @Override
    public synchronized void onProviderDisabled(String provider) {
        Log.d("LocationTracker", "TrackerLocationListener.onProviderDisabled: " + provider);

    }

    public List<Location> getLocations() {
        return locations;
    }


}
