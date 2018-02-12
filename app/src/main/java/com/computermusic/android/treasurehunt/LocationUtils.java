package com.computermusic.android.treasurehunt;

import android.location.Location;
import android.location.LocationManager;


public class LocationUtils {

    private static LocationManager locationManager;
    private static Location TreasureLocation;

    public static void setTreasureLocation(Location treasureLocation){
        TreasureLocation = treasureLocation;
    }

    public static Location getTreasureLocation(){
        return TreasureLocation;
    }

    public static LocationManager getLocationManager() {
        return locationManager;
    }

    public static void setLocationManager(LocationManager locationManager) {
        LocationUtils.locationManager = locationManager;
    }
}
