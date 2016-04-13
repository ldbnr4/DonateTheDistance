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
        donateDB.execSQL("CREATE TABLE IF NOT EXISTS User(" +
                "firstName VARCHAR," +
                "lastName VARCHAR," +
                "heightFT INT," +
                "heightIN INT," +
                "weightLbs INT);");

        Cursor resultSet = donateDB.rawQuery("Select * from User", null);
        if (resultSet.getCount() > 0) {
            resultSet.close();
            donateDB.close();
            finish();
            startActivity(new Intent(this, CharitySelectionView.class));
        }
        resultSet.close();

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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void saveData(View view) {
        String first_name = ((EditText) findViewById(R.id.first_name)).getText().toString();
        String last_name = ((EditText) findViewById(R.id.last_name)).getText().toString();
        int height_ft = ((NumberPicker) findViewById(R.id.np_feet)).getValue();
        int height_in = ((NumberPicker) findViewById(R.id.np_inches)).getValue();
        int weight = Integer.parseInt(((EditText) findViewById(R.id.weight)).getText().toString());

        donateDB.execSQL("INSERT INTO User VALUES('" + first_name + "','" + last_name + "'," +
                "'" + height_ft + "','" + height_in + "','" + weight + "');");
        donateDB.close();

        /*FileOutputStream fos = null;
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
        }*/

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
