package com.logcat.anilreddy.hikerswatch;

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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView Latitude,Longitude,Accuracy,Altitude,Address;

   public void updateLocationInfo(Location location){
       Log.i( "location", location.toString());

       Latitude = (TextView) findViewById(R.id.latitudeId);
       Longitude = (TextView) findViewById(R.id.longitudeId);
       Accuracy = (TextView) findViewById(R.id.accuracyId);
       Altitude  = (TextView) findViewById(R.id.altitudeId);
       Address = (TextView) findViewById(R.id.addressId);

       Latitude.setText("Location : " + location.getLatitude());
       Longitude.setText("Longitude : " + location.getLongitude());
       Accuracy.setText("Accuracy : " + location.getAccuracy());
       Altitude.setText("Altitude : " + location.getAltitude());

       Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

       try {

           String address = "Could not found Address";

           List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

           if (listAddress != null && listAddress.size()>0) {

               address = "Address : \n\n   ";

               if (listAddress.get(0).getThoroughfare() != null ){
                   address += listAddress.get(0).getThoroughfare() + "\n   ";
               }

               if (listAddress.get(0).getSubThoroughfare() != null ){
                   address += listAddress.get(0).getSubThoroughfare() + ", ";
               }

               if (listAddress.get(0).getLocality() != null ){
                   address += listAddress.get(0).getLocality() + "\n   ";
               }

               if (listAddress.get(0).getAdminArea() != null ){
                   address += listAddress.get(0).getAdminArea() + ", ";
               }

               if (listAddress.get(0).getCountryName() != null ){
                   address += listAddress.get(0).getCountryName() + "\n   ";
               }

               if (listAddress.get(0).getPostalCode() != null ){
                   address += listAddress.get(0).getPostalCode() + "\n";
               }
           }

           Address.setText(address);


       } catch (IOException e) {

           e.printStackTrace();

       }
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        if(Build.VERSION.SDK_INT < 23) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null){

                    updateLocationInfo(location);

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            }
        }
    }
}
