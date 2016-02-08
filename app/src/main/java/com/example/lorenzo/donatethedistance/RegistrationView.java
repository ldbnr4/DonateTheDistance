package com.example.lorenzo.donatethedistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

public class RegistrationView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_view);

        String[] feetArray = new String[9];
        for (int i = 0; i < feetArray.length; i++) {
            feetArray[i] = Integer.toString(i + 1) + "'";
        }

        NumberPicker np = (NumberPicker) findViewById(R.id.np_feet);
        np.setMaxValue(feetArray.length);
        np.setMinValue(1);
        np.setDisplayedValues(feetArray);

        String[] inchesArray = new String[12];
        for (int i = 0; i < inchesArray.length; i++) {
            inchesArray[i] = Integer.toString(i) + '"';
        }
        np = (NumberPicker) findViewById(R.id.np_inches);
        np.setMinValue(0);
        np.setMaxValue(inchesArray.length - 1);
        np.setDisplayedValues(inchesArray);
    }
}
