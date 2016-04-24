package com.example.lorenzo.donatethedistance;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProfilePageView extends AppCompatActivity {

    public static ArrayList<String> range(int min, int max) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page_view);

        TextView lbl_name = (TextView) findViewById(R.id.lbl_name);
        TextView lbl_member = (TextView) findViewById(R.id.lbl_memberSince);
        TextView lbl_TtlMiles = (TextView) findViewById(R.id.lbl_TtlMiles);
        TextView lbl_TtlCal = (TextView) findViewById(R.id.lbl_TtlCalories);
        TextView lbl_date = (TextView) findViewById(R.id.lbl_lastDate);
        TextView lbl_type = (TextView) findViewById(R.id.lbl_lastType);
        TextView lbl_charity = (TextView) findViewById(R.id.lbl_lastCharity);
        TextView lbl_donation = (TextView) findViewById(R.id.lbl_lastDonation);
        TextView lbl_cal = (TextView) findViewById(R.id.lbl_lastCalories);
        LineChart chart = (LineChart) findViewById(R.id.chart);
        ListView listView = (ListView) findViewById(R.id.listView);

        SQLiteDatabase donateDB = openOrCreateDatabase("DonateTheDistance", MODE_PRIVATE, null);
        Cursor resultSet = donateDB.rawQuery("Select * from User", null);
        resultSet.moveToFirst();
        String userName = resultSet.getString(0) + " " + resultSet.getString(1);
        String memberDate = resultSet.getString(5);
        resultSet.close();

        resultSet = donateDB.rawQuery("SELECT * FROM Workouts ORDER BY datetime(date) DESC LIMIT 1", null);
        resultSet.moveToFirst();
        Gson gson = new Gson();
        RunningBikingWorkoutSummary workoutSummary = gson.fromJson(resultSet.getString(0), RunningBikingWorkoutSummary.class);
        resultSet.close();

        ArrayList<WorkoutSummary> list = new ArrayList<>();
        resultSet = donateDB.rawQuery("SELECT * FROM Workouts", null);
        resultSet.moveToFirst();
        while (!resultSet.isAfterLast()) {
            list.add(gson.fromJson(resultSet.getString(0), WorkoutSummary.class));
            resultSet.moveToNext();
        }
        resultSet.close();
        donateDB.close();
        float allMiles = 0, allCals = 0, yMax = 0;
        ArrayList<Entry> distances = new ArrayList<>();
        ArrayList<Entry> donations = new ArrayList<>();
        int x = 0;
        ArrayList<String> dates = new ArrayList<>(), types = new ArrayList<>(), charities = new ArrayList<>();
        ArrayList<Float> dons = new ArrayList<>(), cals = new ArrayList<>();
        for (WorkoutSummary summary : list) {
            distances.add(new Entry(summary.distance, x));
            donations.add(new Entry(summary.donationAmnt, x));
            allMiles += summary.distance;
            allCals += summary.caloriesBurned;
            if (summary.distance > yMax) {
                yMax = summary.distance;
            }
            dates.add(summary.date);
            types.add(summary.type);
            charities.add(summary.charity);
            dons.add(summary.donationAmnt);
            cals.add(summary.caloriesBurned);
            x++;
        }
        LineDataSet setDist = new LineDataSet(distances, "Distances");
        setDist.setAxisDependency(YAxis.AxisDependency.LEFT);
        setDist.setColor(Color.GREEN);
        setDist.setDrawValues(false);
        LineDataSet setDon = new LineDataSet(donations, "Donations");
        setDon.setColor(Color.BLUE);
        setDon.setAxisDependency(YAxis.AxisDependency.LEFT);
        setDon.setDrawValues(false);
        setDon.enableDashedLine(10, 5, 5);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(setDist);
        dataSets.add(setDon);
        ArrayList<String> xVals = range(1, list.size());
        LineData data = new LineData(xVals, dataSets);

        assert lbl_name != null;
        lbl_name.setText(userName);
        assert lbl_member != null;
        lbl_member.setText(memberDate);
        assert lbl_date != null;
        lbl_date.setText(workoutSummary.date);
        assert lbl_type != null;
        lbl_type.setText(workoutSummary.type);
        assert lbl_charity != null;
        lbl_charity.setText(workoutSummary.charity);
        assert lbl_donation != null;
        lbl_donation.setText(NumberFormat.getCurrencyInstance(Locale.US).format(workoutSummary.donationAmnt));
        assert lbl_cal != null;
        lbl_cal.setText(String.valueOf((int) workoutSummary.caloriesBurned));
        assert lbl_TtlMiles != null;
        lbl_TtlMiles.setText(String.format(Locale.US, "%.1f", allMiles));
        assert lbl_TtlCal != null;
        lbl_TtlCal.setText(String.valueOf(allCals));
        assert chart != null;
        chart.setData(data);
        chart.getAxisRight().setEnabled(false);
        YAxis leftAxis = chart.getAxisLeft();
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        leftAxis.setSpaceTop(100);
        leftAxis.setSpaceBottom(10);
        chart.setDrawBorders(true);
        chart.setDescription("");
        chart.invalidate(); // refresh
        CustomAdapter adapter = new CustomAdapter(this, dates, types, charities, dons, cals);
        assert listView != null;
        listView.setAdapter(adapter);


    }
}
