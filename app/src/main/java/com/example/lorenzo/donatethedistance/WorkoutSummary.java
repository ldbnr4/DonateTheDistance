package com.example.lorenzo.donatethedistance;

/**
 * Created by Lorenzo on 4/11/2016.
 *
 */
public abstract class WorkoutSummary {
    float caloriesBurned;
    String type;
    String charity;
    float donationAmnt;

    public WorkoutSummary(float caloriesBurned, String type, String charity, float donationAmnt) {
        this.caloriesBurned = caloriesBurned;
        this.type = type;
        this.charity = charity;
        this.donationAmnt = donationAmnt;
    }


}
