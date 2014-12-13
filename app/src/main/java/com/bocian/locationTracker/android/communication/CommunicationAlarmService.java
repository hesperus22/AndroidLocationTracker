package com.bocian.locationTracker.android.communication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bocian.locationTracker.android.location.TrackerLocationService;


public class CommunicationAlarmService extends Service {


    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferencesChangeListener;

    private TrackerLocationService trackerLocationService;
    private TrackerLocationServiceConnection conn;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("LocationTracker", "CommunicationAlarmService: created");
        removeIntentFromAlarmMgr();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        sharedPreferencesChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if (s.equals("updateInterval")) {
                    Log.d("LocationTracker", "sharedPreferenceChangeListener: updateInterval changed: " + sharedPreferences.getString(s, "1000"));
                    removeIntentFromAlarmMgr();
                    addIntentToAlarmMgr(Long.parseLong(sharedPreferences.getString(s, "1000")));
                }
            }
        };
        conn = new TrackerLocationServiceConnection();

        bindToTrackerLocationService();
        preferences.registerOnSharedPreferenceChangeListener(sharedPreferencesChangeListener);


        addIntentToAlarmMgr(Long.parseLong(preferences.getString("updateInterval", "1000")));
        Log.d("LocationTracker", "CommunicationAlarmService: endOfCreated");

    }

    private void bindToTrackerLocationService() {
        Intent intent = new Intent(this, TrackerLocationService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private void unbindToTrackerLocationService() {
        unbindService(conn);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        removeIntentFromAlarmMgr();
        unbindToTrackerLocationService();
    }

    private void addIntentToAlarmMgr(long period) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, OnAlarmReceiver.class)
                , 0);

        Log.d("LocationTracker", "CommunicationAlarmService: addIntentToAlarmMgr");
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000,
                period,
                pendingIntent);
    }

    private void removeIntentFromAlarmMgr() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, OnAlarmReceiver.class), 0);

        Log.d("LocationTracker", "CommunicationAlarmService: removeIntentFromAlarmMgr");
        alarmManager.cancel(pendingIntent);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class TrackerLocationServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("LocationTracker", "TrackerLocationServiceConnection: onServiceConnected");
            ((TrackerLocationService.LocalBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("LocationTracker", "TrackerLocationServiceConnection: onServiceDisconnected");
            trackerLocationService = null;
        }
    }
}
