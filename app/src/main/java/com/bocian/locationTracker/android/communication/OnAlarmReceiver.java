package com.bocian.locationTracker.android.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bocian.locationTracker.android.location.TrackerLocationService;

public class OnAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CommunicationService.acquireStaticLock(context);
        Log.d("LocationTracker", "OnAlarmReceiver.onReceive");
        if (TrackerLocationService.IS_RUNNING)
            context.startService(new Intent(context, CommunicationService.class));
    }
}