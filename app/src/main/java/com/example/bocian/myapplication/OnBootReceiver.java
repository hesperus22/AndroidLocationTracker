package com.example.bocian.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {
    private static final int PERIOD=5000;  // 5 minutes

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyApp","On boot receiver");
        AlarmManager mgr=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(context, OnAlarmRecceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(context, 0,
                i, 0);

        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime()+5000,
                PERIOD,
                pi);
    }
}