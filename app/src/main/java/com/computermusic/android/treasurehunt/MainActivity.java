package com.computermusic.android.treasurehunt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private Location TreasureLocation;
    private boolean TreasureSet = false;
    private int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationUtils.setLocationManager(locationManager);

        if (!isLocationEnabled()) {
            showAlert();
        }
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public void setTreasure(View view) {
        TreasureSet = true;
        counter = 0;
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = locationManager.getBestProvider(criteria, true);
        if (provider != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showAlert();
                return;
            }
            findViewById(R.id.btm_set).setVisibility(View.INVISIBLE);
            ((TextView)findViewById(R.id.txt_set)).setText("Loading...");

            locationManager.requestLocationUpdates(provider, 1000, 0, locationListenerBest);
        } else {
            Toast.makeText(this,"Please enable location in permissions",Toast.LENGTH_SHORT).show();
        }

    }

    public void StartGame(View view) {

        LocationUtils.setTreasureLocation(TreasureLocation);
        Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(final Location location) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (TreasureSet) {
                        TreasureLocation = location;
                        counter++;
                        ((TextView)findViewById(R.id.txt_set)).setText(counter*10 + "%");
                        if (counter == 10) {
                            locationManager.removeUpdates(locationListenerBest);
                            TreasureSet = false;

                            // update UI
                            findViewById(R.id.txt_set).setVisibility(View.INVISIBLE);
                            findViewById(R.id.txt_play).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_play).setVisibility(View.VISIBLE);
                        }
                    } else {
                        locationManager.removeUpdates(locationListenerBest);
                    }
                }
            });
        }



        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
