package com.bocian.locationTracker.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bocian.locationTracker.android.MyService;

public class OnAlarmRecceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MyService.acquireStaticLock(context);

        context.startService(new Intent(context, MyService.class));
    }
}