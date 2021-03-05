package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Location lastLocation;
    TextView latitude;
    TextView longitude;
    TextView accuracy;
    TextView altitude;
    TextView Address;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startListening();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitude = findViewById(R.id.Latitude);
        longitude = findViewById(R.id.Longitude);
        accuracy = findViewById(R.id.Accuracy);
        altitude = findViewById(R.id.Altitude);
        Address = findViewById(R.id.Address);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                updateLocationInfo(location);

            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle){
            }
            @Override
            public void onProviderEnabled(String s){
            }
            @Override
            public void onProviderDisabled(String s){
            }
        };

        // if device is running on a device with sdk less than 23

        if (Build.VERSION.SDK_INT < 23) {
            startListening();

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                // ask for permission

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            } else {
                // we have permission
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                        locationListener);
                lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    updateLocationInfo(lastLocation);
                }
            }
        }
    }


    public void startListening() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }

        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastLocation != null) {
            updateLocationInfo(lastLocation);
        }
    }

    public void updateLocationInfo(Location location) {

        Log.i("Location", location.toString());

        latitude.setText("Latitude: "+ location.getLatitude());
        latitude.setText("Longitude: "+ location.getLongitude());
        latitude.setText("Altitude: "+ location.getAltitude());
        latitude.setText("Accuracy: "+ location.getAccuracy());
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            String address = "Could not find";
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
                    1);
            if (addressList != null && addressList.size() > 0) {

                Log.i("Place info" , addressList.get(0).toString());
                address = "Adress\n";
                if(addressList.get(0).getSubThoroughfare() != null){
                    address += addressList.get(0).getThoroughfare() + "\n";
                }
                if(addressList.get(0).getSubThoroughfare() != null){
                    address += addressList.get(0).getThoroughfare() + "\n";
                }
                if(addressList.get(0).getLocality() != null){
                    address += addressList.get(0).getLocality() + "\n";
                }
                if(addressList.get(0).getPostalCode() != null){
                    address += addressList.get(0).getPostalCode() + "\n";
                }
                if(addressList.get(0).getCountryName() != null){
                    address += addressList.get(0).getCountryName() + "\n";
                }

                Address.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}