package com.example.lorenzo.donatethedistance;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by Lorenzo on 4/11/2016.
 *
 */
public class RunningBikingWorkoutSummary extends WorkoutSummary {
    ArrayList<Location> locations;
    float duration;
    String date;
    float distance;

    public RunningBikingWorkoutSummary(ArrayList<Location> locations,
                                       float duration, String date, float caloriesBurned, float distance, String charity, String type) {
        super(caloriesBurned, type, charity);
        this.locations = locations;
        this.duration = duration;
        this.date = date;
        this.distance = distance;
    }
}
