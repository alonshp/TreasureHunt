package com.computermusic.android.treasurehunt;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;


public class GameActivity extends Activity implements SensorEventListener {

    // define the display assembly compass picture
    private ImageView image;

    // record the compass picture angle turned
    private float currentDegree = 0f;

    // device sensor manager
    private SensorManager mSensorManager;


    private PerfectLoopMediaPlayer perfectLoopMediaPlayer1 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer2 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer3 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer4 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer5 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer6 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer7 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer8 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer9 = null;
    private PerfectLoopMediaPlayer perfectLoopMediaPlayer10 = null;

    private LocationManager locationManager;
    private Location TreasureLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // our compass image
        image = (ImageView) findViewById(R.id.compass);

        // initialize your android device sensor capabilities
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        perfectLoopMediaPlayer1 = PerfectLoopMediaPlayer.create(this, R.raw.basicbeat1);
        perfectLoopMediaPlayer2 = PerfectLoopMediaPlayer.create(this, R.raw.piano2);
        perfectLoopMediaPlayer3 = PerfectLoopMediaPlayer.create(this, R.raw.stringslow3);
        perfectLoopMediaPlayer4 = PerfectLoopMediaPlayer.create(this, R.raw.stringshigh4);
        perfectLoopMediaPlayer5 = PerfectLoopMediaPlayer.create(this, R.raw.drums5);
        perfectLoopMediaPlayer6 = PerfectLoopMediaPlayer.create(this, R.raw.cellos6);
        perfectLoopMediaPlayer7 = PerfectLoopMediaPlayer.create(this, R.raw.brass7);
        perfectLoopMediaPlayer8 = PerfectLoopMediaPlayer.create(this, R.raw.crash8);
        perfectLoopMediaPlayer9 = PerfectLoopMediaPlayer.create(this, R.raw.vocal9);
        perfectLoopMediaPlayer10 = PerfectLoopMediaPlayer.create(this, R.raw.guitars10);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!isLocationEnabled()) {
            showAlert();
        }

        TreasureLocation = LocationUtils.getTreasureLocation();

        startGame();
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

    private void startGame() {
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
            perfectLoopMediaPlayer1.start();
            perfectLoopMediaPlayer2.start();
            perfectLoopMediaPlayer3.start();
            perfectLoopMediaPlayer4.start();
            perfectLoopMediaPlayer5.start();
            perfectLoopMediaPlayer6.start();
            perfectLoopMediaPlayer7.start();
            perfectLoopMediaPlayer8.start();
            perfectLoopMediaPlayer9.start();
            perfectLoopMediaPlayer10.start();

            perfectLoopMediaPlayer2.setVolume(0, 0);
            perfectLoopMediaPlayer3.setVolume(0, 0);
            perfectLoopMediaPlayer4.setVolume(0, 0);
            perfectLoopMediaPlayer5.setVolume(0, 0);
            perfectLoopMediaPlayer6.setVolume(0, 0);
            perfectLoopMediaPlayer7.setVolume(0, 0);
            perfectLoopMediaPlayer8.setVolume(0, 0);
            perfectLoopMediaPlayer9.setVolume(0, 0);
            perfectLoopMediaPlayer10.setVolume(0, 0);


            locationManager.requestLocationUpdates(provider, 1000, 0, locationListenerBest);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // to stop the listener and save battery
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // get the angle around the z-axis rotated
        float degree = Math.round(event.values[0]);

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(210);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        image.startAnimation(ra);
        currentDegree = -degree;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }

    public void stopGame(View view) {
        locationManager.removeUpdates(locationListenerBest);

        // stop music
        perfectLoopMediaPlayer1.stop();
        perfectLoopMediaPlayer2.stop();
        perfectLoopMediaPlayer3.stop();
        perfectLoopMediaPlayer4.stop();
        perfectLoopMediaPlayer5.stop();
        perfectLoopMediaPlayer6.stop();
        perfectLoopMediaPlayer7.stop();
        perfectLoopMediaPlayer8.stop();
        perfectLoopMediaPlayer9.stop();
        perfectLoopMediaPlayer10.stop();


        // update UI
        findViewById(R.id.btn_stop).setVisibility(View.INVISIBLE);
    }


    private final LocationListener locationListenerBest = new LocationListener() {
        public void onLocationChanged(final Location location) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    double currDistance = location.distanceTo(TreasureLocation);

                    if (currDistance < 10) {
                        perfectLoopMediaPlayer1.stop();
                        perfectLoopMediaPlayer2.stop();
                        perfectLoopMediaPlayer3.stop();
                        perfectLoopMediaPlayer4.stop();
                        perfectLoopMediaPlayer5.stop();
                        perfectLoopMediaPlayer6.stop();
                        perfectLoopMediaPlayer7.stop();
                        perfectLoopMediaPlayer8.stop();
                        perfectLoopMediaPlayer9.stop();
                        perfectLoopMediaPlayer10.stop();

                        Intent myIntent = new Intent(GameActivity.this, EndGameActivity.class);
                        GameActivity.this.startActivity(myIntent);

                        locationManager.removeUpdates(locationListenerBest);

                    } else if (currDistance < 20) {
                        perfectLoopMediaPlayer2.setVolume(1, 1);
                        perfectLoopMediaPlayer3.setVolume(1, 1);
                        perfectLoopMediaPlayer4.setVolume(1, 1);
                        perfectLoopMediaPlayer5.setVolume(1, 1);
                        perfectLoopMediaPlayer6.setVolume(1, 1);
                        perfectLoopMediaPlayer7.setVolume(0.5f, 0.5f);
                        perfectLoopMediaPlayer8.setVolume(1, 1);
                        perfectLoopMediaPlayer9.setVolume(1, 1);
                        perfectLoopMediaPlayer10.setVolume(1, 1);
                    } else if (currDistance < 30) {
                        perfectLoopMediaPlayer2.setVolume(1, 1);
                        perfectLoopMediaPlayer3.setVolume(1, 1);
                        perfectLoopMediaPlayer4.setVolume(1, 1);
                        perfectLoopMediaPlayer5.setVolume(1, 1);
                        perfectLoopMediaPlayer6.setVolume(1, 1);
                        perfectLoopMediaPlayer7.setVolume(0.5f, 0.5f);
                        perfectLoopMediaPlayer8.setVolume(1, 1);
                        perfectLoopMediaPlayer9.setVolume(1, 1);
                        perfectLoopMediaPlayer10.setVolume(0, 0);
                    } else if (currDistance < 40) {
                        perfectLoopMediaPlayer2.setVolume(1, 1);
                        perfectLoopMediaPlayer3.setVolume(1, 1);
                        perfectLoopMediaPlayer4.setVolume(1, 1);
                        perfectLoopMediaPlayer5.setVolume(1, 1);
                        perfectLoopMediaPlayer6.setVolume(1, 1);
                        perfectLoopMediaPlayer7.setVolume(0.5f, 0.5f);
                        perfectLoopMediaPlayer8.setVolume(1, 1);
                        perfectLoopMediaPlayer9.setVolume(0, 0);
                        perfectLoopMediaPlayer10.setVolume(0, 0);
                    } else if (currDistance < 50) {
                        perfectLoopMediaPlayer2.setVolume(1, 1);
                        perfectLoopMediaPlayer3.setVolume(1, 1);
                        perfectLoopMediaPlayer4.setVolume(1, 1);
                        perfectLoopMediaPlayer5.setVolume(1, 1);
                        perfectLoopMediaPlayer6.setVolume(1, 1);
                        perfectLoopMediaPlayer7.setVolume(0.5f, 0.5f);
                        perfectLoopMediaPlayer8.setVolume(0, 0);
                        perfectLoopMediaPlayer9.setVolume(0, 0);
                        perfectLoopMediaPlayer10.setVolume(0, 0);
                    } else if (currDistance < 60) {
                        perfectLoopMediaPlayer2.setVolume(1, 1);
                        perfectLoopMediaPlayer3.setVolume(1, 1);
                        perfectLoopMediaPlayer4.setVolume(1, 1);
                        perfectLoopMediaPlayer5.setVolume(1, 1);
                        perfectLoopMediaPlayer6.setVolume(1, 1);
                        perfectLoopMediaPlayer7.setVolume(0, 0);
                        perfectLoopMediaPlayer8.setVolume(0, 0);
                        perfectLoopMediaPlayer9.setVolume(0, 0);
                        perfectLoopMediaPlayer10.setVolume(0, 0);
                    } else if (currDistance < 70) {
                        perfectLoopMediaPlayer2.setVolume(1, 1);
                        perfectLoopMediaPlayer3.setVolume(1, 1);
                        perfectLoopMediaPlayer4.setVolume(1, 1);
                        perfectLoopMediaPlayer5.setVolume(1, 1);
                        perfectLoopMediaPlayer6.setVolume(0, 0);
                        perfectLoopMediaPlayer7.setVolume(0, 0);
                        perfectLoopMediaPlayer8.setVolume(0, 0);
                        perfectLoopMediaPlayer9.setVolume(0, 0);
                        perfectLoopMediaPlayer10.setVolume(0, 0);
                    } else if (currDistance < 80) {
                        perfectLoopMediaPlayer2.setVolume(1, 1);
                        perfectLoopMediaPlayer3.setVolume(1, 1);
                        perfectLoopMediaPlayer4.setVolume(1, 1);
                        perfectLoopMediaPlayer5.setVolume(0, 0);
                        perfectLoopMediaPlayer6.setVolume(0, 0);
                        perfectLoopMediaPlayer7.setVolume(0, 0);
                        perfectLoopMediaPlayer8.setVolume(0, 0);
                        perfectLoopMediaPlayer9.setVolume(0, 0);
                        perfectLoopMediaPlayer10.setVolume(0, 0);
                    } else if (currDistance < 90) {
                        perfectLoopMediaPlayer2.setVolume(1, 1);
                        perfectLoopMediaPlayer3.setVolume(1, 1);
                        perfectLoopMediaPlayer4.setVolume(0, 0);
                        perfectLoopMediaPlayer5.setVolume(0, 0);
                        perfectLoopMediaPlayer6.setVolume(0, 0);
                        perfectLoopMediaPlayer7.setVolume(0, 0);
                        perfectLoopMediaPlayer8.setVolume(0, 0);
                        perfectLoopMediaPlayer9.setVolume(0, 0);
                        perfectLoopMediaPlayer10.setVolume(0, 0);
                    } else if (currDistance < 100) {
                        perfectLoopMediaPlayer2.setVolume(1, 1);
                        perfectLoopMediaPlayer3.setVolume(0, 0);
                        perfectLoopMediaPlayer4.setVolume(0, 0);
                        perfectLoopMediaPlayer5.setVolume(0, 0);
                        perfectLoopMediaPlayer6.setVolume(0, 0);
                        perfectLoopMediaPlayer7.setVolume(0, 0);
                        perfectLoopMediaPlayer8.setVolume(0, 0);
                        perfectLoopMediaPlayer9.setVolume(0, 0);
                        perfectLoopMediaPlayer10.setVolume(0, 0);
                    } else if (currDistance < 110) {
                        perfectLoopMediaPlayer2.setVolume(0, 0);
                        perfectLoopMediaPlayer3.setVolume(0, 0);
                        perfectLoopMediaPlayer4.setVolume(0, 0);
                        perfectLoopMediaPlayer5.setVolume(0, 0);
                        perfectLoopMediaPlayer6.setVolume(0, 0);
                        perfectLoopMediaPlayer7.setVolume(0, 0);
                        perfectLoopMediaPlayer8.setVolume(0, 0);
                        perfectLoopMediaPlayer9.setVolume(0, 0);
                        perfectLoopMediaPlayer10.setVolume(0, 0);
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