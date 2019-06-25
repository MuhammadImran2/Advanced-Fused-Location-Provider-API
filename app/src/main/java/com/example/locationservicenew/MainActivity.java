package com.example.locationservicenew;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity {

    private TextView Latitude;
    private TextView Longitudes;
    private TextView Altitudes;
    private TextView Accuracy;
    private TextView Speed;
    private TextView Sensor;
    private TextView Update;
    private ToggleButton toggleButton1;
    private ToggleButton toggleButton2;


    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;


    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean updateOn = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initilize view
        Latitude = findViewById(R.id.tvLatitude);
        Longitudes = findViewById(R.id.tvLongitudes);
        Altitudes = findViewById(R.id.tvAltitudes);
        Accuracy = findViewById(R.id.tvAccuracy);
        Speed = findViewById(R.id.tvSpeed);
        Sensor = findViewById(R.id.tvSensor);
        Update = findViewById(R.id.tvUpdate);
        //GPS
        toggleButton1 = findViewById(R.id.toggleButton1);
        //Location
        toggleButton2 = findViewById(R.id.toggleButton2);


        locationRequest = new LocationRequest();
        locationRequest.setInterval(7500); // use a value about 10 to 15 second for a real  app
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        //gps button
        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (toggleButton1.isChecked()) {
                    //Using GPS Only
                    Sensor.setText("GPS");
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


                } else {
                    //Using Balanced power accuracy
                    Sensor.setText("Cell Tower and Wifi");
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

                }

            }
        });

        //Location
        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Location
                if (toggleButton2.isChecked()) {
                    //location update on
                    Update.setText("ON");
                    updateOn = true;
                    startLocationUpdate();
                } else {
                    //location update of
                    Update.setText("OFF");
                    updateOn = false;
                    stopLocationUpdate();
                }

            }
        });


        //Understanding
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // return;
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    //location not equal null
                    if (location != null) {
                        Latitude.setText(String.valueOf(location.getLatitude()));
                        Longitudes.setText(String.valueOf(location.getLongitude()));
                        Accuracy.setText(String.valueOf(location.getAccuracy()));
                        if (location.hasAltitude()) {
                            Altitudes.setText(String.valueOf(location.getAltitude()));
                        } else {
                            Altitudes.setText("no Altitudes available ");
                        }

                        if (location.hasSpeed()) {

                            Speed.setText(String.valueOf(location.getSpeed() + "m/s"));

                        } else {
                            Speed.setText("No speed available");
                        }

                    }

                }
            });

        } else {
            //request permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }


        locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                for (Location location : locationResult.getLocations()) {
                    //Update user Interface  with location data


                    //location not equal null
                    if (location != null) {
                        Latitude.setText(String.valueOf(location.getLatitude()));
                        Longitudes.setText(String.valueOf(location.getLongitude()));
                        Accuracy.setText(String.valueOf(location.getAccuracy()));
                        if (location.hasAltitude()) {
                            Altitudes.setText(String.valueOf(location.getAltitude()));
                        } else {
                            Altitudes.setText("no Altitudes available ");
                        }

                        if (location.hasSpeed()) {

                            Speed.setText(String.valueOf(location.getSpeed() + "m/s"));

                        } else {
                            Speed.setText("No speed available");
                        }

                    }


                }
            }
        };


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted do nothing and carry on
                } else {
                    Toast.makeText(this, "This app requires location permission to the granted", Toast.LENGTH_SHORT).show();
                    finish();
                }


                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (updateOn) startLocationUpdate();

    }

    private void startLocationUpdate() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }

        }


        }


    @Override
    protected void onPause() {
        super.onPause();

        stopLocationUpdate();

    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }
}
