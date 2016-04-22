package com.example.lorenzo.donatethedistance;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Lorenzo on 4/11/2016.
 *
 */
public class RunningBikingWorkoutSummary extends WorkoutSummary {
    public ArrayList<Location> locations;
    public float duration;
    public String date;
    public float distance;
    public float avePace;
    public float range;

    public RunningBikingWorkoutSummary(ArrayList<Location> locations, float duration, String date, float caloriesBurned, float distance,
                                       String charity, String type, float donationAmnt) {
        super(caloriesBurned, type, charity, donationAmnt);
        this.locations = locations;
        this.duration = duration;
        this.date = date;
        this.distance = distance;
        setAvePace(locations);
    }

    private void setAvePace(ArrayList<Location> locations) {
        float max = 0;
        float min = 999999999;
        if (locations.size() == 0) {
            this.avePace = 0;
            return;
        }
        float paceTtl = 0;
        float speed = 0;
        for (Location location : locations) {
            speed = location.getSpeed();
            if (speed < min) {
                min = speed;
            } else if (speed > max) {
                max = speed;
            }
            paceTtl += speed;
        }
        this.avePace = paceTtl / locations.size();
        this.range = (max - min) / 3;
    }
}
