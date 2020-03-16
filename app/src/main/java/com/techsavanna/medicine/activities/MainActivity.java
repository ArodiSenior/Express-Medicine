package com.techsavanna.medicine.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.techsavanna.medicine.R;
import com.techsavanna.medicine.fragments.AccountFragment;
import com.techsavanna.medicine.fragments.CategoryFragment;
import com.techsavanna.medicine.fragments.FavFragment;
import com.techsavanna.medicine.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout MainFrame;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    boolean  doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isServicesOK();
        MainFrame = findViewById(R.id.FrameLayout);
        Toolbar toolbar = findViewById(R.id.Maintoolbar);
        setSupportActionBar(toolbar);
        MaterialButton adminButton = findViewById(R.id.action_admin);
        MaterialButton LoginButton = findViewById(R.id.LoginButton);
        LoginButton.setOnClickListener(this);
        adminButton.setOnClickListener(this);
        toolbar.setNavigationOnClickListener(
            
                new NavigationIconClickListener(
                        this,
                        MainFrame,
                        new LinearInterpolator(),
                        ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sort, null),
                        ResourcesCompat.getDrawable(getResources(), R.drawable.close_menu, null)
            
            
                )
    
        );
        

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.FrameLayout, new HomeFragment()).commit();
    
    
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.FrameLayout, new HomeFragment()).commit();
                        break;
                    case R.id.action_favorites:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.FrameLayout, new FavFragment()).commit();
                        break;
                    case R.id.action_Categories:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.FrameLayout, new CategoryFragment()).commit();
                        break;
                    case R.id.action_account:
                        if (FirebaseAuth.getInstance().getCurrentUser() == null){
                            startActivity(new Intent(MainActivity.this, Login.class));
                        }else {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.FrameLayout, new AccountFragment()).commit();
    
                        }
                        break;
                }
                return true;
            }
        });

    }
    
    public int stackCount() {
        return getSupportFragmentManager().getBackStackEntryCount();
    }
    
    @Override
    public void onBackPressed() {
        
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.FrameLayout);
        if (fragment instanceof HomeFragment) {
            if (doubleBackToExitPressedOnce && stackCount() == 0) {
                super.onBackPressed();
                finish();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to logout", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 3500);
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout,
                    new HomeFragment()).commit();
        }
        
        if (stackCount() != 0) {
            super.onBackPressed();
        }
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       
        if (id == R.id.action_search) {
//            Intent intent = new Intent(MainActivity.this, Admin.class);
//            startActivity(intent);
        }
        else if (id == R.id.action_cart) {
            Intent intent = new Intent(MainActivity.this, Cart.class);
            startActivity(intent);
        }
        


        return super.onOptionsItemSelected(item);
    }
    
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
    
    public boolean isServicesOK(){
        Log.d("isServicesOK", "isServicesOK: checking google services version");
        
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        
        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d("isServicesOK", "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d("isServicesOK", "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LoginButton:
                startActivity(new Intent(MainActivity.this,Login.class));
                break;
            case R.id.action_admin:
                startActivity(new Intent(MainActivity.this,Admin.class));
                break;
        }
        
    }
}
