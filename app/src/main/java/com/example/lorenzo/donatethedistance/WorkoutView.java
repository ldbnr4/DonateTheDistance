package com.example.lorenzo.donatethedistance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class WorkoutView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_view);
        Intent intent = getIntent();
        String workout = intent.getStringExtra("Workout");
        System.out.println(workout);
    }
}
