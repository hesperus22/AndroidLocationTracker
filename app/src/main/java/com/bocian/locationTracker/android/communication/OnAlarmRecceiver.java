package com.bocian.locationTracker.android.communication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnAlarmRecceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CommunicationService.acquireStaticLock(context);

        Log.d("LocationTracker", "OnAlarmRecceiver: onReceive");
    }
}