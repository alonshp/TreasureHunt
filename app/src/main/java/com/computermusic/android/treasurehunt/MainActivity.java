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
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.button;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private Location TreasureLocation;
    private boolean TreasureSet = false;
    private int counter;

    private PerfectLoopMediaPlayer perfectLoopMediaPlayer1 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer2 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer3 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!isLocationEnabled()) {
            showAlert();
        }

        perfectLoopMediaPlayer1 = PerfectLoopMediaPlayer.create(this, R.raw.percussion);
        perfectLoopMediaPlayer2 = PerfectLoopMediaPlayer.create(this, R.raw.piano);
        perfectLoopMediaPlayer3 = PerfectLoopMediaPlayer.create(this, R.raw.violin);
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

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
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

            locationManager.requestLocationUpdates(provider, 1000, 0, locationListenerBest);
        }

    }

    public void StartGame(View view) {
        if (!checkLocation())
            return;

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
            locationManager.requestLocationUpdates(provider, 1000, 0, locationListenerBest);

            // update UI
            findViewById(R.id.txt_play).setVisibility(View.INVISIBLE);
            findViewById(R.id.btn_play).setVisibility(View.INVISIBLE);
            findViewById(R.id.btn_stop).setVisibility(View.VISIBLE);

            perfectLoopMediaPlayer1.start();
            perfectLoopMediaPlayer2.start();
            perfectLoopMediaPlayer3.start();
            perfectLoopMediaPlayer2.setVolume(0, 0);
            perfectLoopMediaPlayer3.setVolume(0,0);
        }


    }

    public void stopGame(View view) {
        locationManager.removeUpdates(locationListenerBest);

        // stop music
        perfectLoopMediaPlayer1.stop();
        perfectLoopMediaPlayer2.stop();
        perfectLoopMediaPlayer3.stop();

        // update UI
        findViewById(R.id.btn_stop).setVisibility(View.INVISIBLE);
        findViewById(R.id.txt_set).setVisibility(View.VISIBLE);
        findViewById(R.id.btm_set).setVisibility(View.VISIBLE);
    }

    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(final Location location) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (TreasureSet) {
                        TreasureLocation = location;
                        counter++;
                        if (counter == 10) {
                            locationManager.removeUpdates(locationListenerBest);
                            TreasureSet = false;

                            // update UI
                            findViewById(R.id.txt_set).setVisibility(View.INVISIBLE);
                            findViewById(R.id.btm_set).setVisibility(View.INVISIBLE);
                            findViewById(R.id.txt_play).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_play).setVisibility(View.VISIBLE);
                        }
                    } else {
                        double currDistance = location.distanceTo(TreasureLocation);
                        ((TextView)findViewById(R.id.txt_additional)).setText(currDistance + "");
                        // todo update music
                        if (currDistance < 10){
                            perfectLoopMediaPlayer2.setVolume(1, 1);
                            perfectLoopMediaPlayer3.setVolume(1,1);
                        }
                        else if (currDistance < 20){
                            perfectLoopMediaPlayer2.setVolume(1, 1);
                            perfectLoopMediaPlayer3.setVolume(0,0);
                        }
                        else if (currDistance < 30){
                            perfectLoopMediaPlayer2.setVolume(0, 0);
                            perfectLoopMediaPlayer3.setVolume(0,0);
                        }
                        else if (currDistance < 40){

                        }
                        else if (currDistance < 50){

                        }
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
