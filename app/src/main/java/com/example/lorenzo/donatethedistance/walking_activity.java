package com.example.lorenzo.donatethedistance;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class walking_activity extends AppCompatActivity {

    private TextView textView;

    private SensorManager mSensorManager;

    private Sensor mStepCounterSensor;

    private Sensor mStepDetectorSensor;
    SQLiteDatabase donateDB;
    Gson gson = new Gson();
    float calories;
    double money;
    double distance;


    private SensorEventListener listener;

    int value = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking_activity);
        textView = (TextView) findViewById(R.id.steps);
        donateDB = openOrCreateDatabase("DonateTheDistance", MODE_PRIVATE, null);

        mSensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);
        mStepCounterSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepDetectorSensor = mSensorManager
                .getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        mSensorManager.registerListener(listener, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(listener, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }

    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;


        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            textView.setText("Step Counter Detected : " + value);
        } else if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            // For test only. Only allowed value is 1.0 i.e. for step taken
            textView.setText("Step Detector Detected : " + value);
        }
    }

    /*protected void onResume() {

        super.onResume();

        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELA.Y_FASTEST);
        mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);

    }*/

    public void onStop() {
        super.onStop();
        mSensorManager.unregisterListener(listener, mStepCounterSensor);
        mSensorManager.unregisterListener(listener, mStepDetectorSensor);
        donateDB.execSQL("CREATE TABLE IF NOT EXISTS Workouts(workout BLOB, date DATE);");
        Intent intent = getIntent();
        Cursor resultSet = donateDB.rawQuery("Select * from User", null);
        resultSet.moveToFirst();
        float weight = resultSet.getInt(4);
        resultSet.close();
        calories = 0.53f * weight;
        float htFt = resultSet.getInt(3);
        float htIn = resultSet.getInt(5);
        float height = (htFt * 12) + htIn;
        double stride = height * 0.413f;
        distance = (stride * value)/63360;
        money = distance / 4;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm MMM dd, ''yy", Locale.US);
        String date = df.format(new Date());
        float floatMoney = ((float) money);
        float floatDistance = ((float) distance);
        donateDB.execSQL("INSERT INTO Workouts VALUES('" + gson.toJson(new WalkingWorkoutSummary(calories, "Walk", intent.getStringExtra(ActivitySelectionView.SELECTED_CHARITY), floatMoney, date, value, floatDistance)));
        donateDB.close();
        Intent intent1 = new Intent(this, RunWorkoutView.class);
        intent1.putExtra("steps", value);
        startActivity(intent);
    }


    }
