package com.bocian.locationTracker.android.communication;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class CommunicationService extends IntentService {

    public static final String LOCK_NAME_STATIC = "com.bocian.locationTracker.android.STATIC_LOCK";
    private static PowerManager.WakeLock lockStatic = null;


    public CommunicationService() {
        super("CommunicationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("LocationTracker", "CommunicationService: onHandleIntent start");

    }

    public static void acquireStaticLock(Context context) {
        getLock(context).acquire();
    }


    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic == null) {
            PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            lockStatic = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }

        return (lockStatic);
    }
}