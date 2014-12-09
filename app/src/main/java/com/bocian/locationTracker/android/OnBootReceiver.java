package com.bocian.locationTracker.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {
    private static final int PERIOD = 5000;  // 5 minutes

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LocationTracker", "On boot receiver");
        Intent i = new Intent(context, OnAlarmRecceiver.class);

        TimerManager.addIntentToAlarmMgr(i, context, 5000);
        TimerManager.removeIntentFromAlarmMgr(i, context);


        Intent serviceIntent = new Intent(context,TrackerLocationService.class);
        context.startService(serviceIntent);

    }


}

