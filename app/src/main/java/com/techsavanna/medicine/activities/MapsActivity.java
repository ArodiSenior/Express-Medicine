package com.techsavanna.medicine.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.adapters.PlaceAutocompleteAdapter;
import com.techsavanna.medicine.models.TrackingModel;
import com.techsavanna.medicine.service.MyService;

import java.text.DecimalFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {
    
    private GoogleMap mMap;
    private String email;
    private DatabaseReference databaseReference;
    private Double lat;
    private Double lng;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE = 7171;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
    
        getLocationPermission();
    
        databaseReference = FirebaseDatabase.getInstance().getReference("Locations");
        
        if (getIntent() != null){
            email = getIntent().getStringExtra("email");
            lat = getIntent().getDoubleExtra("lat",0);
            lng = getIntent().getDoubleExtra("lng",0);
        }
    
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },PERMISSION_REQUEST_CODE);
        
        
        }else {
            Intent myIntent = new Intent(getBaseContext(), MyService.class);
            startService(myIntent);
        }
        
    
        
    }

    
    private void loanLocationForThisUser(String email) {
        Query user_location = databaseReference.orderByChild("email").equalTo(email);
        user_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    TrackingModel trackingModel = snapshot.getValue(TrackingModel.class);

                    LatLng friendLocation = new LatLng(Double.parseDouble(trackingModel.getLat()), Double.parseDouble(trackingModel.getLng()));

                    Location current = new Location("");
                    current.setLatitude(lat);
                    current.setLongitude(lng);
                    mMap.clear();
                    Location friend = new Location("");
                    friend.setLatitude(Double.parseDouble(trackingModel.getLat()));
                    friend.setLongitude(Double.parseDouble(trackingModel.getLng()));
                    mMap.addMarker(new MarkerOptions()
                            .position(friendLocation)
                            .title(trackingModel.getEmail())
                            .snippet(new DecimalFormat("#.#").format(current.distanceTo(friend) / 1000)+" "+"km"+ "\n")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(trackingModel.getLat()), Double.parseDouble(trackingModel.getLng())),12.0f));
    
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    
    private double distance(Location current, Location friend) {
        double theta = current.getLongitude() - friend.getLongitude();
        double dist = Math.sin(deg2rad(current.getLatitude()))
                * Math.sin(deg2rad(friend.getLatitude()))
                *Math.cos(deg2rad(current.getLongitude()))
                *Math.cos(deg2rad(friend.getLongitude()))
                *Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 *1.1515;
        
        return (dist);
    }
    
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (!TextUtils.isEmpty(email)){
                loanLocationForThisUser(email);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            
            init();
        }
    }
    private void initMap(){
        Log.d("initMap", "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        
        mapFragment.getMapAsync(MapsActivity.this);
    }
    
    private void init() {
        Log.d("init", "init: initializing");
        
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);
        
        
        hideSoftKeyboard();
    }
    
    private void getDeviceLocation(){
        Log.d("getDeviceLocation", "getDeviceLocation: getting the devices current location");
        
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        
        try{
            if(mLocationPermissionsGranted){
                
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d("onComplete", "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
    
                            Double latfriend = currentLocation.getLatitude();
                            Double lngfriend = currentLocation.getLongitude();
                            LatLng currentt = new LatLng(latfriend,lngfriend);
                            mMap.clear();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latfriend,lngfriend),12.0f));
                            mMap.addMarker(new MarkerOptions().position(currentt).title(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            
                        }else{
                            Log.d("onComplete", "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e("getDeviceLocation", "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    
    private void getLocationPermission(){
        Log.d("getLocationPermission", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("result", "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;
        
        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d("result", "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d("result", "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    
    
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    
    }
}
