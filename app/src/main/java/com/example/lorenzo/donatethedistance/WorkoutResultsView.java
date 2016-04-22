package com.example.lorenzo.donatethedistance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

public class WorkoutResultsView extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {

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

        Cursor resultSet = donateDB.rawQuery("SELECT * FROM Workouts ORDER BY date DESC LIMIT 1", null);
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
        lbl_amntDonated.setText(String.valueOf(workoutSummary.donationAmnt));
        lbl_distance.setText(String.valueOf(workoutSummary.distance));
        lbl_duration.setText(String.valueOf(workoutSummary.duration));
        lbl_calBurned.setText(String.valueOf(workoutSummary.caloriesBurned));
        lbl_avePace.setText(String.valueOf(workoutSummary.avePace));
        lbl_date.setText(workoutSummary.date);

        resultSet.close();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setOnCameraChangeListener(this);
        map = googleMap;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        int i = 0;
        Location prevLoc;
        for (Location m : workoutSummary.locations) {
            builder.include(new LatLng(m.getLatitude(), m.getLongitude()));
            if (i > 0) {
                prevLoc = workoutSummary.locations.get(i - 1);
                PolylineOptions options = new PolylineOptions().add(new LatLng(prevLoc.getLatitude(),
                        prevLoc.getLongitude()), new LatLng(m.getLatitude(), m.getLongitude()));
                if (m.getSpeed() >= workoutSummary.range * 2) {
                    map.addPolyline(options).setColor(Color.GREEN);
                } else if (m.getSpeed() < workoutSummary.range) {
                    map.addPolyline(options).setColor(Color.RED);
                } else {
                    map.addPolyline(options).setColor(Color.YELLOW);
                }

            }
            i++;
        }
        LatLngBounds bounds = builder.build();

        int padding = 15; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        map.animateCamera(cu);
    }
}
