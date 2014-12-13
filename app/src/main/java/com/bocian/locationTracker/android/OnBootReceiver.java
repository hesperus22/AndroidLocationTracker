package com.bocian.locationTracker.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bocian.locationTracker.android.communication.CommunicationAlarmService;
import com.bocian.locationTracker.android.location.TrackerLocationService;

public class OnBootReceiver extends BroadcastReceiver {
    private static final int PERIOD = 5000;  // 5 minutes

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("LocationTracker", "On boot receiver");




        Intent trackerLocationService = new Intent(context, TrackerLocationService.class);
        context.startService(trackerLocationService);


        Intent communicationAlarmService = new Intent(context, CommunicationAlarmService.class);
        context.startService(communicationAlarmService);


    }


}

