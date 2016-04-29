package com.example.lorenzo.donatethedistance;

/**
 * Created by Lorenzo on 4/11/2016.
 *
 */
public abstract class WorkoutSummary {
    public float caloriesBurned;
    public String type;
    public String charity;
    public float donationAmnt;
    public String date;
    public float duration;
    public float distance;

    public WorkoutSummary(float caloriesBurned, String type, String charity, float donationAmnt, String date, float duration, float distance) {
        this.caloriesBurned = caloriesBurned;
        this.type = type;
        this.charity = charity;
        this.donationAmnt = donationAmnt;
        this.date = date;
        this.duration = duration;
        this.distance = distance;
    }
}
