package com.example.lorenzo.donatethedistance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.Locale;

public class WorkoutResultsView extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    SQLiteDatabase donateDB;
    Gson gson = new Gson();
    TextView lbl_charity, lbl_amntDonated, lbl_distance, lbl_duration, lbl_calBurned, lbl_avePace, lbl_date;
    RunningBikingWorkoutSummary workoutSummary;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_results_view);

        donateDB = openOrCreateDatabase("DonateTheDistance", MODE_PRIVATE, null);

        Cursor resultSet = donateDB.rawQuery("SELECT * FROM Workouts ORDER BY datetime(date) DESC LIMIT 1", null);
        resultSet.moveToFirst();

        workoutSummary = gson.fromJson(resultSet.getString(0), RunningBikingWorkoutSummary.class);

        lbl_charity = (TextView) findViewById(R.id.lbl_charity);
        lbl_amntDonated = (TextView) findViewById(R.id.lbl_amntDonated);
        lbl_distance = (TextView) findViewById(R.id.lbl_distance);
        lbl_duration = (TextView) findViewById(R.id.lbl_duration);
        lbl_calBurned = (TextView) findViewById(R.id.lbl_calBurned);
        lbl_avePace = (TextView) findViewById(R.id.lbl_avePace);
        lbl_date = (TextView) findViewById(R.id.lbl_date);

        lbl_charity.setText(workoutSummary.charity);
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(Locale.US);
        String format = currencyInstance.format(workoutSummary.donationAmnt);
        lbl_amntDonated.setText(format);
        lbl_distance.setText(String.format(Locale.US, "%.1f", workoutSummary.distance));
        int hours = (int) (workoutSummary.duration / 3600);
        int mins = (int) (workoutSummary.duration / 60);
        int secs = (int) (workoutSummary.duration % 60);
        float millSecs = workoutSummary.duration - ((int) workoutSummary.duration);
        lbl_duration.setText(
                String.format("%s:%s:%s:%s", String.format(Locale.US, "%02d", hours), String.format(Locale.US, "%02d", mins),
                        String.format(Locale.US, "%02d", secs), String.format(Locale.US, "%03d", (int) (millSecs * 100)))

        );
        lbl_calBurned.setText(String.valueOf((int) workoutSummary.caloriesBurned));
        lbl_avePace.setText(String.format(Locale.US, "%.2f", workoutSummary.avePace));
        lbl_date.setText(workoutSummary.date);

        Button vpBtn = (Button) findViewById(R.id.btnVp);
        assert vpBtn != null;
        vpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(WorkoutResultsView.this, ProfilePageView.class));
            }
        });

        resultSet.close();
        donateDB.close();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnMapLoadedCallback(this);
        map = googleMap;
    }

    @Override
    public void onMapLoaded() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int i = 0;
        LocationStat prevLoc;
        for (LocationStat m : workoutSummary.locations) {
            builder.include(new LatLng(m.getLatitude(), m.getLongitude()));
            if (i > 0) {
                prevLoc = workoutSummary.locations.get(i - 1);
                PolylineOptions options = new PolylineOptions().add(new LatLng(prevLoc.getLatitude(),
                        prevLoc.getLongitude()), new LatLng(m.getLatitude(), m.getLongitude()));
                if (m.getSpeed() <= workoutSummary.lowSpeed) {
                    map.addPolyline(options).setColor(Color.RED);
                } else if (m.getSpeed() <= workoutSummary.midSpeed) {
                    map.addPolyline(options).setColor(Color.YELLOW);
                } else {
                    map.addPolyline(options).setColor(Color.GREEN);
                }
                if ((i + 1) == workoutSummary.locations.size()) {
                    MarkerOptions mo = new MarkerOptions();
                    mo.title("End");
                    mo.draggable(false);
                    mo.position(new LatLng(m.getLatitude(), m.getLongitude()));
                    mo.visible(true);
                    map.addMarker(mo);
                }
            } else {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("Start");
                markerOptions.draggable(false);
                markerOptions.position(new LatLng(m.getLatitude(), m.getLongitude()));
                markerOptions.visible(true);
                map.addMarker(markerOptions);
            }
            i++;
        }
        LatLngBounds bounds = builder.build();

        int padding = 15; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        map.animateCamera(cu);
    }
}
