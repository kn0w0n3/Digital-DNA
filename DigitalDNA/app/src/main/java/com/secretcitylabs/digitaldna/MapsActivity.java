package com.secretcitylabs.digitaldna;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.secretcitylabs.digitaldna.databinding.ActivityMapsBinding;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int Request_Code = 101;
    private double lat, lng;
    int counter = 0;
    //public String[] dbLatCoordinates;
   // ArrayList<String> dbLatCoordinates;
    ArrayList<String> dbLonCoordinates;
    DatabaseHelper mapsDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());
        mapsDatabaseHelper = new DatabaseHelper(this);
        ArrayList<String> dbLatCoordinates = new ArrayList<>();

        //Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //plotSpecimenLocations();
        getCurrentLocation();
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION
            }, Request_Code);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(60000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(5000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //Toast.makeText(getApplicationContext(), " location result is  " + locationResult, Toast.LENGTH_LONG).show();

                if (locationResult == null) {
                    Toast.makeText(getApplicationContext(), "current location is null ", Toast.LENGTH_LONG).show();

                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //Toast.makeText(getApplicationContext(), "current location is " + location.getLongitude(), Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "current elevation....... " + location.getAltitude(), Toast.LENGTH_LONG).show();
                        //TODO: UI updates.
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    LatLng latlng = new LatLng(lat,lng);
                    mMap.addMarker(new MarkerOptions().position(latlng).title("Current Location"));
                    plotSpecimenLocations();

                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
                    //plotSpecimenLocations();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
                }
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (Request_Code) {
            case Request_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                break;
        }
    }

    public void plotSpecimenLocations(){
        Cursor resLat = mapsDatabaseHelper.getAllData();

        if(resLat.getCount() == 0){
            //Show message
            //showMessage("Error", "No data to display");
            Toast.makeText(getApplicationContext(), "No lat coordinates", Toast.LENGTH_LONG).show();
            return;
        }
       // ArrayList<String> dbLatCoordinates = new ArrayList<>();
        ArrayList<String> listLat = new ArrayList<String>();
        while(resLat.moveToNext()){
            //String lAT =
            listLat.add(counter, resLat.getString(27));

            //dbLatCoordinates.add(resLat.getString(27));
           // Toast.makeText(getApplicationContext(), " lat coordinate is: " + resLat.getString(27), Toast.LENGTH_LONG).show();
            counter++;
        }
        counter = 0;

        Cursor resLon = mapsDatabaseHelper.getAllData();
        if(resLon.getCount() == 0){
            //Show message
            //showMessage("Error", "No data to display");
            //Toast.makeText(getApplicationContext(), "No lon coordinates", Toast.LENGTH_LONG).show();
            return;
        }
        // ArrayList<String> dbLatCoordinates = new ArrayList<>();
        ArrayList<String> listLon = new ArrayList<String>();
        while(resLon.moveToNext()){
            //String lAT =
          listLon.add(counter, resLon.getString(28));

            //dbLatCoordinates.add(resLat.getString(27));
            //Toast.makeText(getApplicationContext(), " lon coordinate is: " + resLon.getString(28), Toast.LENGTH_LONG).show();
            counter++;
            //Toast.makeText(getApplicationContext(), " The counter is: " + counter, Toast.LENGTH_LONG).show();
        }
        counter = 0;


        Cursor resSpecimenId = mapsDatabaseHelper.getAllData();
        if(resSpecimenId.getCount() == 0){
            //Show message
            //showMessage("Error", "No data to display");
            //Toast.makeText(getApplicationContext(), "No lon coordinates", Toast.LENGTH_LONG).show();
            return;
        }
        // ArrayList<String> dbLatCoordinates = new ArrayList<>();
        ArrayList<String> specimenIds = new ArrayList<String>();
        while(resSpecimenId.moveToNext()){
            //String lAT =
            specimenIds.add(counter, resSpecimenId.getString(1));

            //dbLatCoordinates.add(resLat.getString(27));
            //Toast.makeText(getApplicationContext(), " lon coordinate is: " + resLon.getString(28), Toast.LENGTH_LONG).show();
            counter++;
            //Toast.makeText(getApplicationContext(), " The counter is: " + counter, Toast.LENGTH_LONG).show();
        }
        counter = 0;


        for (String s: listLat) {
            if(s.equals("")){
                //Toast.makeText(getApplicationContext(), "The text is: " + s, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), "The text was empty: " + counter, Toast.LENGTH_LONG).show();
                counter++;
            }
            else {
                //Toast.makeText(getApplicationContext(), "Trying to add marker: " + counter, Toast.LENGTH_LONG).show();
                LatLng latlng = new LatLng(Double.parseDouble(s), Double.parseDouble(listLon.get(counter)));
                mMap.addMarker(new MarkerOptions().position(latlng).title("Specimen ID: " + specimenIds.get(counter)));
                counter++;
            }
        }
        counter = 0;

            //String d_Lat = s;
            //Toast.makeText(getApplicationContext(), "DLat cord is: " + s, Toast.LENGTH_LONG).show();
            //String d_Lon = listLon.get(counter);
           // Toast.makeText(getApplicationContext(), "DLat cord is: " + listLon.get(counter), Toast.LENGTH_LONG).show();
            //String dynamicString = "dynamicString" + String.valueOf(counter);
           //
            //mMap.addMarker(new MarkerOptions().position(latlng).title("Specimen ID"));
           // counter++;
            //Toast.makeText(getApplicationContext(), "Counter number is : " + counter, Toast.LENGTH_LONG).show();

            // Toast.makeText(getApplicationContext(), "Lat cord is: " + s, Toast.LENGTH_LONG).show();

           // }
        //counter = 0;
        //counter = 0;
        /*
        Cursor resLon = mapsDatabaseHelper.getAllLongitudeCords();
        if(resLon.getCount() == 0){
            //Show message
            //showMessage("Error", "No data to display");
            Toast.makeText(getApplicationContext(), "No lat coordinates", Toast.LENGTH_LONG).show();
            return;
        }
        //ArrayList<String> dbLonCoordinates = new ArrayList<>();
        while(resLon.moveToNext()){
            dbLonCoordinates.add(resLon.getString(27));

        }

         */
        //for (String s: dbLatCoordinates) {

            //String d_Lat = s;
           // String d_Lon = dbLonCoordinates.get(counter);
            //LatLng latlng = new LatLng(Double.parseDouble(d_Lat), Double.parseDouble(d_Lon));
           // mMap.addMarker(new MarkerOptions().position(latlng).title("Specimen ID"));
           // counter++;
           // Toast.makeText(getApplicationContext(), "Lat cord is: " + s, Toast.LENGTH_LONG).show();

        //}
        //counter = 0;
        //mMap.addMarker(new MarkerOptions().position(latlng).title("Current Location"));
    }
}
