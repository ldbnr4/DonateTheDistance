package com.example.lorenzo.donatethedistance;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;

public class WorkoutView extends AppCompatActivity implements SensorEventListener{

    private TextView textView;

    private SensorManager mSensorManager;

    private Sensor mStepCounterSensor;

    private Sensor mStepDetectorSensor;

    public int steps;
    public double milesWalked;
    public double moneyDonated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_view);
        Intent intent = getIntent();
        String workout = intent.getStringExtra("Workout");
        System.out.println(workout);
        textView = (TextView) findViewById(R.id.steps);

        mSensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            textView.setText("Step Counter Detected : " + value);
            /*int height = user.height_ft * 12 + user.height_in;
            double milesWalked = ((height * 0.413) * value)/ 63360;
            double caloriesBurnedPerMile = .53 * user.weight;
            double caloriesBurnedTotal = milesWalked * caloriesBurnedPerMile;
            this is why we need separate activities because we will have different result displays
            calories and distance can either be shown here or shown in the results page
            both would be easy to implement
            */

        } /*else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
            textView.setText("Step Detector Detected : " + value);
        }*/
    }

    protected void onResume() {

        super.onResume();

        mSensorManager.registerListener(this, mStepCounterSensor,

                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor,

                SensorManager.SENSOR_DELAY_FASTEST);

    }

    protected void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void stopWorkout(View view) {
        Intent intent1 = new Intent(this, WorkoutResultsView.class);
        intent1.putExtra("steps", steps);
        intent1.putExtra("money", moneyDonated);
        intent1.putExtra("distance", milesWalked);
        startActivity(intent1);
    }
}
