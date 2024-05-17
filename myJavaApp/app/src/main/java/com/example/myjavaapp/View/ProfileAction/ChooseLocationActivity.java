//package com.example.myjavaapp.View.ProfileAction;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toolbar;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.myjavaapp.R;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.location.places.ui.PlacePicker;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.snackbar.Snackbar;
//
//public class ChooseLocationActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
////declaration of Google API Client
//    private GoogleApiClient mGoogleApiClient;
//    private int PLACE_PICKER_REQUEST = 1;
//    private FloatingActionButton fabPickPlace;
//    private TextView tvPlaceDetails;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //phương thức setContentView ở đây
//        setContentView(R.layout.choose_location_screen);
//        initView();
//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
//                .build();
//        fabPickPlace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PlacePicker.IntentBuilder bulder = new PlacePicker.IntentBuilder();
//                try{
//                    startActivityForResult(bulder.build(ChooseLocationActivity.this),PLACE_PICKER_REQUEST);
//                }catch(GooglePlayServicesNotAvailableException  | GooglePlayServicesRepairableException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private void initView(){
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        fabPickPlace = (FloatingActionButton) findViewById(R.id.fab);
//        tvPlaceDetails = (TextView) findViewById(R.id.placeDetails);
//    }
//
//    private void setSupportActionBar(Toolbar toolbar) {
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    protected void onStop() {
//        mGoogleApiClient.disconnect();
//        super.onStop();
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Snackbar.make(fabPickPlace,connectionResult.getErrorMessage(),Snackbar.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == PLACE_PICKER_REQUEST){
//            if(resultCode == RESULT_OK){
//                Place place = PlacePicker.getPlace(data,this);
//                StringBuilder stringBuilder = new StringBuilder();
//                String placename = String.format("%s",place.getName());
//                String latitude = String.valueOf(place.getLatLng().latitude);
//                String longtitude = String.valueOf(place.getLatLng().longitude);
//                String address = String.format("%s",place.getAddress());
//                stringBuilder.append("Name: " + placename + "\n");
//                stringBuilder.append("Latitude: "  + latitude + "\n");
//                stringBuilder.append("Longtitude: " + longtitude + "\n");
//                stringBuilder.append("Address: " + address + "\n");
//                tvPlaceDetails.setText(stringBuilder.toString());
//
//            }
//        }
//    }
//}
