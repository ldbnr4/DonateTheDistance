package com.example.lorenzo.donatethedistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WorkoutResultsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_results_view);
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        int steps = Integer.parseInt(extras.getString("steps"));
        double distance = Integer.parseInt(extras.getString("steps"));
        double money = Integer.parseInt(extras.getString("money"));
        //totalSteps.setText(steps);
        //totalDistance.setText(distance);
        //totalMoney.setText(money);


    }
}
