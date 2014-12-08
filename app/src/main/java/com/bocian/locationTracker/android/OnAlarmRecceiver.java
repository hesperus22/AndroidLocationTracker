package com.bocian.locationTracker.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnAlarmRecceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LocationService.acquireStaticLock(context);

        context.startService(new Intent(context, LocationService.class));
    }
}