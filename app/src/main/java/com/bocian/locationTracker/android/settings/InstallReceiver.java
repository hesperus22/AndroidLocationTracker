package com.bocian.locationTracker.android.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bocian.locationTracker.android.communication.CommunicationAlarmService;
import com.bocian.locationTracker.android.communication.CommunicationService;
import com.bocian.locationTracker.android.location.TrackerLocationService;

public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        CommunicationService.acquireStaticLock(context);
        Log.d("InstallReceiver", "OnAlarmReceiver.onReceive");
        context.stopService(new Intent(context, TrackerLocationService.class));
        context.stopService(new Intent(context, CommunicationAlarmService.class));
        context.stopService(new Intent(context, CommunicationService.class));
    }
}