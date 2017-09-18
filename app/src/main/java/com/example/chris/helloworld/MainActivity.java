package com.example.chris.helloworld;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.location.LocationManager;

public class MainActivity extends AppCompatActivity {
    //private GeoDataClient mGeoDataClient;

    private Location mLastKnownLocation;
    private boolean mLocationPermissionGranted;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String KEY_LOCATION = "location";
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private LocationRequest mLocationRequest;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Prompt the user for permission
        getLocationPermission();


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        final Button button = (Button)findViewById(R.id.to_second_screen_button_id);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getAndDisplayLocation(savedInstanceState);
            }
        });
    }

    private void getAndDisplayLocation(Bundle savedInstanceState){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(2000);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        getLocationPermission();
        TextView text1 = (TextView)findViewById(R.id.location_display_id);
        ///mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        /*try{
            if(mLocationPermissionGranted) {
                mFusedLocationProviderClient.getLastLocation()
                        .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastKnownLocation = task.getResult();
                        }
                    }
                });
            }
        } catch (SecurityException e) {

            Log.e("Exception: %s", e.getMessage());
        }*/
        String longitude = "";
        String latitude = "";
        try{
            if(mLocationPermissionGranted) {
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                longitude = Double.toString(location.getLongitude());
                latitude = Double.toString(location.getLatitude());
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

        if(!(longitude.isEmpty()) && !(latitude.isEmpty())) {
            text1.setText("<"+longitude+","+latitude+">");
        } else {
            text1.setText("It's null bruh");
        }
        //Intent intent = new Intent(this,TellMeMore.class);
        //startActivity(intent);


    }

    private void getLocationPermission(){
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }




}
