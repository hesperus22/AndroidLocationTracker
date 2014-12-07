package com.example.bocian.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnAlarmRecceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MyService.acquireStaticLock(context);

        context.startService(new Intent(context, MyService.class));
    }
}