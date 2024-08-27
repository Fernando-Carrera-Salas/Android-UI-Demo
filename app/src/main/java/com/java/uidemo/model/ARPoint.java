package com.java.uidemo.model;

import android.location.Location;

public class ARPoint
{
    private final Location location;
    private final String name;

    public ARPoint(String name, double latitude, double longitude, double altitude)
    {
        this.name = name;
        location = new Location("ARPoint");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAltitude(altitude);
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
