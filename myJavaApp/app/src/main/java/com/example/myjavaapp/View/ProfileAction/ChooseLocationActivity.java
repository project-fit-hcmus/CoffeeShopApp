package com.example.myjavaapp.View.ProfileAction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;

import com.google.android.gms.location.LocationRequest;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myjavaapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener {
    //declaration of Google API Client
    private GoogleApiClient mGoogleApiClient;
    private int LOCATION_PERMISSION_REQUEST = 2;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;

    private LocationRequest mLocationRequest;
    private Location mLastLocation;


    private Marker mCurrLocationMarker;
    private MarkerOptions markerOptions;
    private AppCompatButton btnConfirm, btnCancel;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location_content_main);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
        btnConfirm = (AppCompatButton) findViewById(R.id.btnConfirmPosition);
        btnCancel = (AppCompatButton) findViewById(R.id.btnCancelPosition);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ChooseLocationActivity.this, "Location Permission granted!!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChooseLocationActivity.this, "Location Permission NOT granted!!!", Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST);
        }
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        //create makerOptions at current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(location.getLatitude(),location.getLongitude()));
                    mMap.addMarker(markerOptions);
                }
            }
        });


        //initial Google Play service
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }else{
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        //set click action
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(getCompleteAddress(latLng.latitude,latLng.longitude));
                mMap.clear();
                mMap.addMarker(markerOptions);

            }
        });


    }

    protected  synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // Update every 10 seconds
        mLocationRequest.setFastestInterval(5000); // Allow updates up to every 5 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        } else {
            // Handle permission denial case (optional)
            // You might want to request permission or inform the user
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        if(mCurrLocationMarker != null){
            mCurrLocationMarker.remove();
        }
        //showing current location marker on map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Toast.makeText(ChooseLocationActivity.this,"On location Changed",Toast.LENGTH_SHORT).show();
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(),true);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if(null != locations && null != providerList && providerList.size() > 0){
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try{
                List<Address> listAddress = geocoder.getFromLocation(latitude,longitude,1);
                if(null != listAddress && listAddress.size() > 0){
                    String state = listAddress.get(0).getAdminArea();
                    String country = listAddress.get(0).getCountryName();
                    String subLocality = listAddress.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state + "," + country );
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if(mGoogleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public String getCompleteAddress(double latitude, double longitude){
        String addr = "";
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude,1);
            if(addresses != null){
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for(int i =0 ; i <= returnedAddress.getMaxAddressLineIndex(); ++i){
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                addr = strReturnedAddress.toString();
                Log.w("My Current Location address",strReturnedAddress.toString());
            }else{
                Log.w("My Current Location Address","No address returned!");
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.w("My Current Location address","Cannot get address!");
        }

        return  addr;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnConfirmPosition){
            // to do something
            Intent response = new Intent();
            Bundle data = new Bundle();
            LatLng position = markerOptions.getPosition();
            data.putString("address",getCompleteAddress(position.latitude,position.longitude));
            data.putDouble("latitude", position.latitude);
            data.putDouble("longitude",position.longitude);
            response.putExtra("data",data);
            setResult(RESULT_OK, response);
            finish();
        }
        if(v.getId() == R.id.btnCancelPosition){
            Intent response = new Intent();
            setResult(RESULT_CANCELED,response);
            finish();
        }
    }
}

