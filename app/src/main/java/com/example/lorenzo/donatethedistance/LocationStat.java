package com.example.lorenzo.donatethedistance;

/**
 * Created by Lorenzo on 4/21/2016.
 *
 */
public class LocationStat {
    double latitude;
    double longitude;
    float speed;

    public LocationStat(double latitude, double longitude, float speed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
