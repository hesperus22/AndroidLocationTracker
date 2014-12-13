package com.bocian.locationTracker.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bocian.locationTracker.android.communication.CommunicationAlarmService;
import com.bocian.locationTracker.android.location.TrackerLocationService;
import com.bocian.locationTracker.android.settings.SettingsActivity;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button startall = (Button) findViewById(R.id.startAll);
        startall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.bocian.locationTracker.android.FAKE_BROADCAST");
                sendBroadcast(intent);
            }
        });

        Button stopall = (Button) findViewById(R.id.stopAll);
        stopall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.bocian.locationTracker.android.FAKE_BROADCAST");
                Intent communicationAlarmService2 = new Intent(MainActivity.this
                        , TrackerLocationService.class);
                stopService(communicationAlarmService2);

                Intent intent2 = new Intent(MainActivity.this, CommunicationAlarmService.class);
                stopService(intent2);
            }
        });

        Button button2 = (Button) findViewById(R.id.startTrack);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LocationTracker", "TrackerLocationService start service");

                Intent communicationAlarmService2 = new Intent(MainActivity.this
                        , TrackerLocationService.class);
                startService(communicationAlarmService2);
            }
        });
        Button button4 = (Button) findViewById(R.id.stopTrack);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LocationTracker", "TrackerLocationService Killing service");

                Intent communicationAlarmService2 = new Intent(MainActivity.this
                        , TrackerLocationService.class);
                stopService(communicationAlarmService2);
            }
        });


        Button button3 = (Button) findViewById(R.id.startComm);
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("LocationTracker", "CommunicationAlarmService start service");


                Intent intent2 = new Intent(MainActivity.this, CommunicationAlarmService.class);
                startService(intent2);
            }
        });

        Button button5 = (Button) findViewById(R.id.stopComm);
        button5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("LocationTracker", "CommunicationAlarmService Killing service");


                Intent intent2 = new Intent(MainActivity.this, CommunicationAlarmService.class);
                stopService(intent2);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
