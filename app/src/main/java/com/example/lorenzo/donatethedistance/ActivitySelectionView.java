package com.example.lorenzo.donatethedistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivitySelectionView extends AppCompatActivity {

    String charity = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_selection_view);
    }

    public void sendWalk(View view) {
        Intent intent1 = new Intent(this, WorkoutView.class);
        intent1.putExtra("Workout", "Walk");
        startActivity(intent1);
    }
    public void sendRun(View view) {
        Intent intent2 = new Intent(this, WorkoutView.class);
        intent2.putExtra("Workout", "Run");
        startActivity(intent2);
    }
    public void sendBike(View view) {
        Intent intent3 = new Intent(this, WorkoutView.class);
        intent3.putExtra("Workout", "Bike");
        startActivity(intent3);
    }

    public void sendCharity(View view) {
        Intent intent4 = new Intent(this, CharityDetailView.class);
        intent4.putExtra("Charity", charity);
        startActivity(intent4);
    }

}
