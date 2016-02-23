package com.example.lorenzo.donatethedistance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RegistrationView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File dir = getFilesDir();
        File file = new File(dir, "userInfo");
        if (file.exists()) {
            startActivity(new Intent(this, CharitySelectionView.class));
        }

        //file.delete();

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


    public void saveData(View view) {
        String first_name = ((EditText) findViewById(R.id.first_name)).getText().toString();
        String last_name = ((EditText) findViewById(R.id.last_name)).getText().toString();
        int height_ft = ((NumberPicker) findViewById(R.id.np_feet)).getValue();
        int height_in = ((NumberPicker) findViewById(R.id.np_inches)).getValue();
        int weight = Integer.parseInt(((EditText) findViewById(R.id.weight)).getText().toString());

        FileOutputStream fos = null;
        try {
            fos = openFileOutput("userInfo", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fos != null;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(new User(first_name, last_name, height_ft, height_in, weight));
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, CharitySelectionView.class));


        /*try {
            FileInputStream input  = openFileInput("userInfo");
            ObjectInputStream objectInput = new ObjectInputStream(input);
            try {
                User fileUser = (User) objectInput.readObject();
                System.out.println(fileUser.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
