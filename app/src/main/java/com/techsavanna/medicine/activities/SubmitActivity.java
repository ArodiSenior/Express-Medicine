package com.techsavanna.medicine.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.adapters.CustomInfoWindowAdapter;
import com.techsavanna.medicine.adapters.PlaceAutocompleteAdapter;
import com.techsavanna.medicine.database.Database;
import com.techsavanna.medicine.models.OrderModel;
import com.techsavanna.medicine.models.Request;
import com.techsavanna.medicine.models.TrackingModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SubmitActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener {
    ArrayList<OrderModel> cartList = new ArrayList<>();
    MaterialButton button;
    DatabaseReference checkreference;
    String price;
    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    
    //widgets
    private AutoCompleteTextView mSearchText;
    
    //vars
    private Boolean mLocationPermissionsGranted = false;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_submit);
        Toolbar toolbar = findViewById(R.id.CheckToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getLocationPermission();
        mSearchText = findViewById(R.id.input_search);
        
        databaseReference = FirebaseDatabase.getInstance().getReference("Locations");
        price = getIntent().getStringExtra("price");
        
        checkreference = FirebaseDatabase.getInstance().getReference("Request");
        cartList = (ArrayList<OrderModel>) new Database(this).getCarts();
        button = findViewById(R.id.PlaceOrder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                Request request = new Request(
                        FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        "",
                        price,
                        cartList
                );
                checkreference.child(String.valueOf(System.currentTimeMillis()))
                        .setValue(request);
                
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(SubmitActivity.this, "Thank you order placed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SubmitActivity.this, MainActivity.class));
                finish();
            }
        });
        loanLocationForThisUser();
    }
    
    private void loanLocationForThisUser() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final TrackingModel trackingModel = snapshot.getValue(TrackingModel.class);
                    
                    LatLng friendLocation = new LatLng(Double.parseDouble(trackingModel.getLat()), Double.parseDouble(trackingModel.getLng()));
                    System.out.println("locs"+ new Gson().toJson(friendLocation));
                    
                    ArrayList<LatLng> arrayList = new ArrayList<>();
                    arrayList.add(friendLocation);
    
                    Location friend = new Location("");
                    friend.setLatitude(Double.parseDouble(trackingModel.getLat()));
                    friend.setLongitude(Double.parseDouble(trackingModel.getLng()));
                    
                    
                    for (int i = 0; i < arrayList.size(); i++) {
    
                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(SubmitActivity.this));
                        try {
                            String snippet = "User Id: " + trackingModel.getUid() + "\n" +
                                    "Name:" + "Fridah" + "\n" +
                                    "Phone Number: " + "0756432567" + "\n" +
                                    "Status: " + "Free" + "\n";

                            MarkerOptions options = new MarkerOptions()
                                    .position(arrayList.get(i))
                                    .title(trackingModel.getEmail())
                                    .snippet(snippet)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi));
                            mMap.addMarker(options);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(i), 12.0f));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(arrayList.get(i), 12));
                            

                        } catch (NullPointerException e) {
                            Log.e("moveCamera", "moveCamera: NullPointerException: " + e.getMessage());
                        }

                    }
                    
                }
            }
            
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            
            }
        });
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            
            init();
        }
    }
    
    private void initMap() {
        Log.d("initMap", "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        
        mapFragment.getMapAsync(SubmitActivity.this);
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
        
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    
                    //execute our method for searching
                    geoLocate();
                }
                
                return false;
            }
        });
        
        
        hideSoftKeyboard();
    }
    
    private void geoLocate() {
        Log.d("geoLocate", "geoLocate: geolocating");
        
        String searchString = mSearchText.getText().toString();
        
        Geocoder geocoder = new Geocoder(SubmitActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            Log.e("geoLocate", "geoLocate: IOException: " + e.getMessage());
        }
        
        if (list.size() > 0) {
            Address address = list.get(0);
            
            Log.d("geoLocate", "geoLocate: found a location: " + address.toString());
            Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            
        }
    }
    
    private void getDeviceLocation() {
        Log.d("getDeviceLocation", "getDeviceLocation: getting the devices current location");
        
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        
        try {
            if (mLocationPermissionsGranted) {
                
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d("onComplete", "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            Geocoder geocoder = new Geocoder(SubmitActivity.this, Locale.getDefault());
                            
                            List<Address> addresses = null; // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            try {
                                addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                                String city = addresses.get(0).getAddressLine(0);
                                ;
                                
                                mSearchText.setText(city);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            
                        } else {
                            Log.d("onComplete", "onComplete: current location is null");
                            Toast.makeText(SubmitActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("getDeviceLocation", "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }
    
    private void getLocationPermission() {
        Log.d("getLocationPermission", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("result", "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;
        
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
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
    
    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
    
    
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    
    }
}
