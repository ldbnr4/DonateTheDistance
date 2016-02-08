package com.example.lorenzo.donatethedistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

public class RegistrationView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_view);

        NumberPicker np = (NumberPicker) findViewById(R.id.np_feet);
        np.setMaxValue(9);
        np.setMinValue(1);

        np = (NumberPicker) findViewById(R.id.np_inches);
        np.setMinValue(0);
        np.setMaxValue(11);
    }
}
