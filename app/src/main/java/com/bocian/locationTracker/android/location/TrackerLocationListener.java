package com.bocian.locationTracker.android.location;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by bocian on 08.12.14.
 */
public class TrackerLocationListener implements LocationListener{


    private Set<LocationUpdateListener> listeners = new HashSet<LocationUpdateListener>();


    public void addListener(LocationUpdateListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(Location location) {
        for (LocationUpdateListener listener : listeners) {
            listener.handle(location);
        }
    }

    @Override
    public synchronized void onLocationChanged(Location location) {
        Log.d("LocationTracker", "TrackerLocationListener.onLocationChanged: " + location);
        notifyListeners(location);
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

    public void removeListener(LocationUpdateListener locationUpdateListener) {
        listeners.remove(locationUpdateListener);

    }
}
