package com.example.lorenzo.donatethedistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static java.lang.Thread.sleep;

public class SponsorView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_view);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
                startActivity(new Intent(SponsorView.this, WorkoutResultsView.class));
            }
        }).start();
    }
}
