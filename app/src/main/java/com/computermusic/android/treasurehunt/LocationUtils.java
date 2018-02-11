package com.computermusic.android.treasurehunt;

import android.location.Location;


public class LocationUtils {

    private static Location TreasureLocation;

    public static void setTreasureLocation(Location treasureLocation){
        TreasureLocation = treasureLocation;
    }

    public static Location getTreasureLocation(){
        return TreasureLocation;
    }

}
