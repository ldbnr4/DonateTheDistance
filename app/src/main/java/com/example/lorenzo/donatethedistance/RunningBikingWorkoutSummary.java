package com.example.lorenzo.donatethedistance;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Lorenzo on 4/11/2016.
 */
public class RunningBikingWorkoutSummary extends WorkoutSummary {
    ArrayList<Location> locations;
    float duration;
    Date date;

    public RunningBikingWorkoutSummary(ArrayList<Location> locations,
                                       float duration, Date date, float caloriesBurned) {
        super(caloriesBurned);
        this.locations = locations;
        this.duration = duration;
        this.date = date;
    }
}
