package com.bocian.locationTracker.android.location;

import android.location.Location;

import java.util.List;

/**
 * Created by bocian on 14.12.14.
 */
public interface LocationStore {
    void add(Location location);

    List<Location> getAll();

    boolean isEmpty();

    void clear();

    void lock();

    void unlock();
}
