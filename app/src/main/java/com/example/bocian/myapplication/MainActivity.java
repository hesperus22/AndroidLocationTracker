package com.example.bocian.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.bocian.myapplication.FAKE_BROADCAST");
                sendBroadcast(intent);
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener listner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Locationd", location.getProvider());
                Log.d("Locationd", String.valueOf(location.getLatitude()));
                Log.d("Locationd", String.valueOf(location.getLongitude()));
                Log.d("Locationd", String.valueOf(location.getAccuracy()));

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Locationd", provider);

            }

            @Override
            public void onProviderEnabled(String provider) {

                Log.d("Locationd", provider + " enabled");

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Locationd", provider + " disabled");

            }
        };

        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1, 1, listner);


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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
