package com.techsavanna.medicine.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techsavanna.medicine.models.TrackingModel;

public class MyService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient googleApiClient;
    private Location location;
    DatabaseReference databaseReference;
    public MyService() {
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this,"Service Started",Toast.LENGTH_SHORT).show();
        scheduleNext();
        googleApiClient = new GoogleApiClient.Builder(MyService.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        return START_STICKY;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        //Toast.makeText(this,"Service Running",Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this,"Service Stopped",Toast.LENGTH_SHORT).show();
    }
    
    private void scheduleNext() {
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {
            
            @Override
            public void run() {
                //call function
                updateUser();
                ha.postDelayed(this, 1000);
            }
        }, 10000);
        
    }
    public void updateUser(){
        
        databaseReference = FirebaseDatabase.getInstance().getReference("Locations");
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    
        if (location != null){
            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(new TrackingModel(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            String.valueOf(location.getLatitude()),
                            String.valueOf(location.getLongitude())));
        }
    }
    
    @Override
    public void onConnected(@Nullable Bundle bundle) {
    
    }
    
    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
        
    }
    
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    
    }
}
