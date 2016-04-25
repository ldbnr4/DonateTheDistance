package com.example.lorenzo.donatethedistance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegistrationView extends AppCompatActivity {

    SQLiteDatabase donateDB;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        donateDB = openOrCreateDatabase("DonateTheDistance", MODE_PRIVATE, null);
        //donateDB.execSQL("DROP TABLE IF EXISTS User;");
        //donateDB.execSQL("DROP TABLE IF EXISTS Workouts");
        donateDB.execSQL("CREATE TABLE IF NOT EXISTS User(" +
                "firstName VARCHAR," +
                "lastName VARCHAR," +
                "heightFT INT," +
                "heightIN INT," +
                "weightLbs INT," +
                "date DATE);");

        Cursor resultSet = donateDB.rawQuery("Select * from User;", null);
        if (resultSet.getCount() > 0) {
            resultSet.close();
            donateDB.close();
            finish();
            startActivity(new Intent(this, CharitySelectionView.class));
        }
        resultSet.close();

        String[] feetArray = new String[9];
        for (int i = 0; i < feetArray.length; i++) {
            feetArray[i] = Integer.toString(i + 1) + "'";
        }

        setContentView(R.layout.activity_registration_view);

        NumberPicker np = (NumberPicker) findViewById(R.id.np_feet);
        assert np != null;
        np.setMaxValue(feetArray.length);
        np.setMinValue(1);
        np.setDisplayedValues(feetArray);

        String[] inchesArray = new String[12];
        for (int i = 0; i < inchesArray.length; i++) {
            inchesArray[i] = Integer.toString(i) + '"';
        }
        np = (NumberPicker) findViewById(R.id.np_inches);
        assert np != null;
        np.setMinValue(0);
        np.setMaxValue(inchesArray.length - 1);
        np.setDisplayedValues(inchesArray);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void saveData(View view) {
        View firstNameView = findViewById(R.id.first_name);
        assert firstNameView != null;
        String first_name = ((EditText) firstNameView).getText().toString();

        View lastNameView = findViewById(R.id.last_name);
        assert lastNameView != null;
        String last_name = ((EditText) lastNameView).getText().toString();

        View npFeetView = findViewById(R.id.np_feet);
        assert npFeetView != null;
        int height_ft = ((NumberPicker) npFeetView).getValue();

        View npInchesView = findViewById(R.id.np_inches);
        assert npInchesView != null;
        int height_in = ((NumberPicker) npInchesView).getValue();

        View weightView = findViewById(R.id.weight);
        assert weightView != null;
        int weight = Integer.parseInt(((EditText) weightView).getText().toString());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-d H:m", Locale.US);
        String date = df.format(new Date());

        donateDB.execSQL("INSERT INTO User VALUES('" + first_name + "','" + last_name + "'," +
                "'" + height_ft + "','" + height_in + "','" + weight + "','" + date + "');");
        donateDB.close();

        finish();
        startActivity(new Intent(this, CharitySelectionView.class));
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RegistrationView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lorenzo.donatethedistance/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RegistrationView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lorenzo.donatethedistance/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
