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
import android.provider.Settings;
import android.text.TextPaint;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ChooseLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    //declaration of Google API Client
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private int LOCATION_PERMISSION_REQUEST = 2;
    private int FINE_PERMISSION_CODE = 3;
    private FloatingActionButton fabPickPlace;
    private TextView tvPlaceDetails;

    //new
    private GoogleMap mMap;
//    private SupportMapFragment mapFragment;
    Location currentLocation = new Location("USA");
    FusedLocationProviderClient fusedLocationProviderClient;
    private LocationManager locationManager;

    private LocationRequest mLocationRequest;

    private Location mLastLocation;

    private Marker mCurrLocationMarker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location_content_main);
        //ON TEST
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        getLastLocation();
        //ON TEST
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(ChooseLocationActivity.this, "Location Permission granted!!!", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(ChooseLocationActivity.this, "Location Permission NOT granted!!!", Toast.LENGTH_SHORT).show();
//
//            }
//        }
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(ChooseLocationActivity.this, "REQUEST permission!!!",Toast.LENGTH_SHORT).show();
                getLastLocation();
            }else{
                Toast.makeText(ChooseLocationActivity.this,"Location Permission is denied!!!",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //ON TEST
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);
            return;
        }
        Toast.makeText(ChooseLocationActivity.this,"On getLastLocation function!!!",Toast.LENGTH_SHORT).show();
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        Toast.makeText(ChooseLocationActivity.this,"On getLastLocation function 2!!!",Toast.LENGTH_SHORT).show();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null){
                    currentLocation = location;
                    Toast.makeText(ChooseLocationActivity.this, "GET current Location!!!",Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map_fragment);
                    mapFragment.getMapAsync(ChooseLocationActivity.this);
                }
                else{
                    Toast.makeText(ChooseLocationActivity.this,"Location is null",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChooseLocationActivity.this,"on FAILED!!",Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()){
                    if(task.getResult() != null){
                        currentLocation = task.getResult();
                        Toast.makeText(ChooseLocationActivity.this, "GET current Location!!!",Toast.LENGTH_SHORT).show();
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map_fragment);
                        mapFragment.getMapAsync(ChooseLocationActivity.this);
                    }
                    else{
                        Toast.makeText(ChooseLocationActivity.this,"Location is null",Toast.LENGTH_SHORT).show();

                    }
                }
                else{
                    Toast.makeText(ChooseLocationActivity.this,"on Complete: NOT SUCCESSFUL",Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(ChooseLocationActivity.this,"CANCEL ACTION",Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
//        //ON TEST
//        LatLng myLocation = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
//        mMap.addMarker(new MarkerOptions().position(myLocation).title("My location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
//        //ON TEST

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
        MarkerOptions markerOptions = new MarkerOptions();
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


}


//references : https://github.com/androidmads/PlacePickerSample
//references 2: https://medium.com/@abhinavv.singh/integrating-google-maps-in-android-studio-using-java-a-step-by-step-guide-with-code-5ba857dff344


