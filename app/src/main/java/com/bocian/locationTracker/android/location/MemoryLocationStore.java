package com.bocian.locationTracker.android.location;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by bocian on 14.12.14.
 */
public class MemoryLocationStore implements LocationStore {

    public Lock lock = new ReentrantLock();

    List<Location> locations = new ArrayList<Location>();

    public void lock() {
        Log.d("LocationTracker", "MemoryLocationStore lock");
        lock.lock();
    }

    public void unlock() {
        Log.d("LocationTracker", "MemoryLocationStore unlock");
        lock.unlock();
    }

    @Override
    public void add(Location location) {
        lock.lock();
        Log.d("LocationTracker", "MemoryLocationStore.add");
        locations.add(location);
        lock.unlock();
    }

    @Override
    public List<Location> getAll() {
        lock.lock();
        Log.d("LocationTracker", "MemoryLocationStore.getAll");
        List<Location> locationList = Collections.unmodifiableList(locations);
        lock.unlock();
        return locationList;
    }

    @Override
    public boolean isEmpty() {
        lock.lock();
        Log.d("LocationTracker", "MemoryLocationStore.isEmpty");
        boolean isEmpty = locations.isEmpty();
        lock.unlock();
        return isEmpty;
    }

    @Override
    public void clear() {
        lock.lock();
        Log.d("LocationTracker", "MemoryLocationStore.clear");
        locations.clear();
        lock.unlock();
    }
}
