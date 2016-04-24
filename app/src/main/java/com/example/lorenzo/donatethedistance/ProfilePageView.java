package com.example.lorenzo.donatethedistance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProfilePageView extends AppCompatActivity {

    private TextView distance;
    private TextView money;
    private TextView percentage;
    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_view);
        distance = (TextView) findViewById(R.id.totalDistance);
        money = (TextView) findViewById(R.id.totalDonated);
        percentage = (TextView) findViewById(R.id.percentageTraveled);
        name = (TextView) findViewById(R.id.name);
        name.setText(R.id.first_name + R.id.last_name);
        /*
        distance.setText(R.id.distance);
        money.setText(R.id.donated);
        percentage.setText(2680/R.id.distance);
        set graph to imageView
         */

    }
}
