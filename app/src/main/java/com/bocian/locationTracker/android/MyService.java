package com.bocian.locationTracker.android;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MyService extends IntentService {
    private static PowerManager.WakeLock lockStatic=null;

    public MyService() {
        super("AppService");

        Log.d("MyApp", "Service created");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("MyApp", "Start intent");

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("MyApp", "Stop intent");




//        File log=new File(Environment.getExternalStorageDirectory(),
//                "AlarmLog.txt");
//
//        try {
//            BufferedWriter out=new BufferedWriter(
//                    new FileWriter(log.getAbsolutePath(),
//                            log.exists()));
//
//            out.write(new Date().toString());
//            out.write("\n");
//            out.close();
//        }
//        catch (IOException e) {
//            Log.e("AppService", "Exception appending to log file", e);
//        }
    }
    public static void acquireStaticLock(Context context) {
        getLock(context).acquire();
    }

    public static final String LOCK_NAME_STATIC="com.commonsware.android.syssvc.AppService.Static";


    synchronized private static PowerManager.WakeLock getLock(Context context) {
        if (lockStatic==null) {
            PowerManager mgr=(PowerManager)context.getSystemService(Context.POWER_SERVICE);

            lockStatic=mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    LOCK_NAME_STATIC);
            lockStatic.setReferenceCounted(true);
        }

        return(lockStatic);
    }
}