package com.example.lorenzo.donatethedistance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RunWorkoutView extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    // LogCat tag
    private static final String TAG = RunWorkoutView.class.getSimpleName();
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    protected static Random random = new Random();
    ArrayList<LocationStat> positions = new ArrayList<>();
    TextView lblTime;
    TextView lblDistance;
    TextView lblPace;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int hrs = 0;
    int milliseconds = 0;
    String workoutType;
    Handler handler = new Handler();
    public Runnable updateTimer = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            hrs = mins / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            lblTime.setText("" + hrs + ":" + String.format(Locale.US, "%02d", mins) + ":" + String.format(Locale.US, "%02d", secs) + ":"
                    + String.format(Locale.US, "%03d", milliseconds));
            lblTime.setTextColor(Color.RED);
            handler.postDelayed(this, 0);
        }
    };
    float[] distances = new float[1];
    float ttlDistance = 0;
    SQLiteDatabase donateDB;
    Gson gson = new Gson();
    private Location mLastLocation;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    // UI elements
    private Button btnStart;
    private GoogleMap mGoogleMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static void displayPromptForEnablingGPS(
            final Activity activity) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(activity);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        final String message = "Enable either GPS or any other location"
                + " service to find current location.  Click OK to go to"
                + " location services settings to let you do so.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                activity.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }

    public static float randomInRange() {
        float min = 2.88f;
        float max = 4.33f;
        float range = max - min;
        float scaled = random.nextFloat() * range;
        return scaled + min;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_workout_view);

        btnStart = (Button) findViewById(R.id.btnStart);
        lblTime = (TextView) findViewById(R.id.lblTimer);
        lblDistance = (TextView) findViewById(R.id.lblDistance);
        lblPace = (TextView) findViewById(R.id.lblPace);

        donateDB = openOrCreateDatabase("DonateTheDistance", MODE_PRIVATE, null);

        // First we need to check availability of play services
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }

        // Toggling the periodic location updates
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (t == 1) {
                    togglePeriodicLocationUpdates();
                    btnStart.setText("Stop");
                    starttime = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimer, 0);
                    t = 0;
                } else {
                    togglePeriodicLocationUpdates();
                    btnStart.setText("Start");
                    lblTime.setTextColor(Color.BLUE);
                    timeSwapBuff += timeInMilliseconds;
                    handler.removeCallbacks(updateTimer);
                    t = 1;

                    float duration = 0;
                    //String[] split = String.valueOf(lblTime.getText()).split(":");
                    duration += hrs * 3600;
                    duration += mins * 60;
                    duration += secs;
                    duration += milliseconds * 0.001;

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-d H:m", Locale.US);
                    String date = df.format(new Date());

                    float calsBurned = calculateCalBurned();
                    donateDB.execSQL("CREATE TABLE IF NOT EXISTS Workouts(workout BLOB, date DATE);");
                    Intent intent = getIntent();
                    workoutType = intent.getStringExtra(ActivitySelectionView.SELECTED_WORKOUT);
                    donateDB.execSQL("INSERT INTO Workouts VALUES('" + gson.toJson(new RunningBikingWorkoutSummary(positions, duration, date,
                            calsBurned, (ttlDistance * 0.00062137f), intent.getStringExtra(ActivitySelectionView.SELECTED_CHARITY),
                            workoutType, calculateDonation())) + "','" + date + "');");
                    donateDB.close();
                    finish();
                    startActivity(new Intent(RunWorkoutView.this, SponsorView.class));
                }
            }
        });

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            displayPromptForEnablingGPS(this);
        }

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        int UPDATE_INTERVAL = 10000;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        int FATEST_INTERVAL = 1000;
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int DISPLACEMENT = 5;
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Method to display the location on UI
     */
    @SuppressLint("SetTextI18n")
    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                System.out.println("NOOO");
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                System.out.println("NOOOElse");

                int MY_PERMISSIONS_REQUEST_LOCATION = 5;
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RunWorkoutView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lorenzo.donatethedistance/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    /**
     * Method to toggle periodic location updates
     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RunWorkoutView Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.lorenzo.donatethedistance/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLastLocation == null) {
            mLastLocation = location;
        } else if (mLastLocation.equals(location)) {
            return;
        }
        location.setSpeed(randomInRange());
        LocationStat stat = new LocationStat(location.getLatitude(), location.getLongitude(), location.getSpeed());
        positions.add(stat);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LocationStat m : positions) {
            builder.include(new LatLng(m.getLatitude(), m.getLongitude()));
        }
        LatLngBounds bounds = builder.build();

        int padding = 15; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mGoogleMap.animateCamera(cu);

        //Toast.makeText(getApplicationContext(), "Location changed!", Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
        if (positions.size() >= 2) {
            LocationStat prevLoc = positions.get(positions.size() - 2);

            mGoogleMap.addPolyline(new PolylineOptions().add(new LatLng(prevLoc.getLatitude(),
                    prevLoc.getLongitude()), new LatLng(location.getLatitude(),
                    location.getLongitude()))).setColor(Color.BLUE);

            Location.distanceBetween(prevLoc.getLatitude(), prevLoc.getLongitude(),
                    location.getLatitude(), location.getLongitude(), distances);

            ttlDistance += distances[0];

            lblDistance.setText(String.format(Locale.US, "%.2f", (ttlDistance * 0.00062137)));
        }
        lblPace.setText(String.format(Locale.US, "%.3f", (26.8224 / location.getSpeed())) + " min/mi");

        // Assign the new location
        mLastLocation = location;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }

    private float calculateCalBurned() {
        Cursor resultSet = donateDB.rawQuery("Select * from User", null);
        if (resultSet.getCount() == 0) {
            donateDB.close();
            resultSet.close();
            return 0;
        }
        resultSet.moveToFirst();
        float weightInKG = resultSet.getInt(4) * 2.2046226218f;
        resultSet.close();
        return (weightInKG * .0175f * Consts.RUN_MET);
    }

    private float calculateDonation() {
        if (workoutType.equals(getString(R.string.run_txt))) {
            return (ttlDistance * 0.00062137f) * .50f;
        } else if (workoutType.equals(getString(R.string.bike_txt))) {
            return (ttlDistance * 0.00062137f) * .10f;
        }
        return 0;
    }
}
