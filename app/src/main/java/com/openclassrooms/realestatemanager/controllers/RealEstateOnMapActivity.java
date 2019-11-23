package com.openclassrooms.realestatemanager.controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.ConvertAddressesAsyncTask;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.openclassrooms.realestatemanager.utils.MyApp.getContext;

public class RealEstateOnMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        ConvertAddressesAsyncTask.Listeners,
        GoogleMap.OnMarkerClickListener {

    public static final String TAG = "DEBUG";
    private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private Boolean mLocationPermissionGranted = false;
    private Boolean isTwoPanes;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastKnownLocation;
    private LinkedHashMap<Long, String> mAddresses = new LinkedHashMap<>();
    private LinkedHashMap<Long, LatLng> mLatLngsAddresses = new LinkedHashMap<>();
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private List<Marker> mMarkerList = new ArrayList<>();

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_on_map);

        this.setGoogleApiClient();
        String str = getIntent().getStringExtra("addresses");
        isTwoPanes = getIntent().getBooleanExtra("isTwoPanes", false);
        Gson gson = new Gson();
        Type listType = new TypeToken<LinkedHashMap<Long, String>>() {
        }.getType();
        mAddresses = gson.fromJson(str, listType);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();
        new ConvertAddressesAsyncTask(this, this, mAddresses, getString(R.string.APIKEY)).execute();
    }


    // ------------------------------------ UI ------------------------------------

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    // ----------------------------------- UTILS -----------------------------------

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mLocationPermissionGranted = true;
            }
            updateLocationUI();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), 12));
                        } else {
                            Log.d("Error", "Current location is null. Using defaults.");
                            Log.e("Error", "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    //build google api client
    public void setGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void doInBackground() {

    }

    @Override
    public void onPostExecute() {
        Log.d(TAG, "onPostExecute: " + mLatLngsAddresses);
        for (Map.Entry entry : mLatLngsAddresses.entrySet()) {
            Marker tempMarker = mMap.addMarker(new MarkerOptions()
                    .position((LatLng) entry.getValue()));
            tempMarker.setTag(entry.getKey());
        }
        mMap.setOnMarkerClickListener(this);
    }

    public void setLatLngsAddresses(LinkedHashMap<Long, LatLng> latLngArrayList) {
        this.mLatLngsAddresses = latLngArrayList;
    }

    //send informations to mainactivity to open the clicked real estate
    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ComeFrom", "map");
        intent.putExtra("realEstateId", (Long) marker.getTag());
        intent.putExtra("realEstateIdToClick", (Long) marker.getTag());
        intent.putExtra("isTwoPanes", isTwoPanes);
        startActivity(intent);
        return false;
    }
}
