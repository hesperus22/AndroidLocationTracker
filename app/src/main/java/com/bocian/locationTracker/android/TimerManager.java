package com.bocian.locationTracker.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by bocian on 08.12.14.
 */
public final class TimerManager {

    private TimerManager() {
        throw new AssertionError();
    }

    public static final void addIntentToAlarmMgr(Intent intent, Context ctx, long period) {
        AlarmManager mgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000,
                period,
                pendingIntent);
    }

    public static final void removeIntentFromAlarmMgr(Intent intent, Context ctx, long period) {
        AlarmManager mgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);

        mgr.cancel(pendingIntent);

    }

}
