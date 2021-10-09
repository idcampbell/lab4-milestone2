package com.example.lab4_milestone2;

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
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }
            @Override
            public void onProviderEnabled(String s) {

            }
            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (Build.VERSION.SDK_INT < 23) {
            startListening();
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    updateLocationInfo(location);
                }
            }
        }


    }

    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    public void updateLocationInfo(Location location) {
        Log.i("LocationInfo", location.toString());
        TextView latitudeText = (TextView) findViewById(R.id.latitudeText);
        TextView longitudeText = (TextView) findViewById(R.id.longitudeText);
        TextView altitudeText = (TextView) findViewById(R.id.altitudeText);
        TextView accuracyText = (TextView) findViewById(R.id.accuracyText);
        SpannableStringBuilder latitudeStr = new SpannableStringBuilder("Latitude: "+String.format("%.4f",location.getLatitude()));
        SpannableStringBuilder longitudeStr = new SpannableStringBuilder("Longitude: "+String.format("%.4f",location.getLongitude()));
        SpannableStringBuilder altitudeStr = new SpannableStringBuilder("Altitude: "+location.getAltitude());
        SpannableStringBuilder accuracyStr = new SpannableStringBuilder("Accuracy: "+location.getAccuracy());
        latitudeStr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        longitudeStr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        altitudeStr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        accuracyStr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latitudeStr.setSpan(new UnderlineSpan(), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        longitudeStr.setSpan(new UnderlineSpan(), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        altitudeStr.setSpan(new UnderlineSpan(), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        accuracyStr.setSpan(new UnderlineSpan(), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        latitudeText.setText(latitudeStr);
        longitudeText.setText(longitudeStr);
        altitudeText.setText(altitudeStr);
        accuracyText.setText(accuracyStr);

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            String address = "Could not find address";
            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (listAddresses != null && listAddresses.size()>0) {
                Log.i("PlaceInfo", listAddresses.get(0).toString());
                address = "Address: \n";
                if (listAddresses.get(0).getSubThoroughfare() != null) {
                    address += listAddresses.get(0).getSubThoroughfare() + " ";
                }
                if (listAddresses.get(0).getThoroughfare() != null) {
                    address += listAddresses.get(0).getThoroughfare() + " ";
                }
                if (listAddresses.get(0).getLocality() != null) {
                    address += listAddresses.get(0).getLocality() + " ";
                }
                if (listAddresses.get(0).getPostalCode() != null) {
                    address += listAddresses.get(0).getPostalCode() + " ";
                }
                if (listAddresses.get(0).getCountryName() != null) {
                    address += listAddresses.get(0).getCountryName() + " ";
                }
            }

            TextView addressText = (TextView) findViewById(R.id.addressText);
            SpannableStringBuilder addressStr = new SpannableStringBuilder(address);
            accuracyStr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            accuracyStr.setSpan(new UnderlineSpan(), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            addressText.setText(accuracyStr);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}