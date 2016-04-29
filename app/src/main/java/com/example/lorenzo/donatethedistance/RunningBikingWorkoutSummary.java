package com.example.lorenzo.donatethedistance;

import java.util.ArrayList;

/**
 * Created by Lorenzo on 4/11/2016.
 *
 */
public class RunningBikingWorkoutSummary extends WorkoutSummary {
    public ArrayList<LocationStat> locations;
    public float avePace;
    public float lowSpeed;
    public float midSpeed;

    public RunningBikingWorkoutSummary(ArrayList<LocationStat> locations, float duration, String date, float caloriesBurned, float distance,
                                       String charity, String type, float donationAmnt) {
        super(caloriesBurned, type, charity, donationAmnt, date, duration, distance);
        this.locations = locations;
        setAvePace(locations);
    }

    private void setAvePace(ArrayList<LocationStat> locations) {
        float max = 0;
        float min = 999999999;
        if (locations.size() == 0) {
            this.avePace = 0;
            return;
        }
        float paceTtl = 0;
        float speed;
        for (LocationStat location : locations) {
            speed = location.getSpeed();
            if (speed < min) {
                min = speed;
            } else if (speed > max) {
                max = speed;
            }
            paceTtl += speed;
        }
        this.avePace = 26.8224f / (paceTtl / locations.size());
        float range = (max - min) / 3;
        lowSpeed = min + range;
        midSpeed = lowSpeed + range;

    }
}
