package com.bocian.locationTracker.android;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bocian on 14.12.14.
 */
public class LocationSeeder {

    public static List<Location> locations() {
        List<Location> locations = new ArrayList<Location>();

        for (int i = 0; i < 10; i++) {
            Location location = new Location("dupa");
            location.setAccuracy(20);
            location.setLatitude(200);
            location.setLongitude(399);
            location.setProvider("dupa3");
            location.setTime(2222222l);
            location.setSpeed(3333);

            locations.add(location);
        }

        return locations;
    }
}
