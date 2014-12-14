package com.bocian.locationTracker.android;

import android.app.Service;
import android.os.Binder;

public class LocalBinder<E extends Service> extends Binder {
    private E service;

    public LocalBinder(E service) {

        this.service = service;
    }

    public E getService() {
        return service;
    }
}