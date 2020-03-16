package com.techsavanna.medicine.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.techsavanna.medicine.R;

public class Splash extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 2500;
    ImageView SplashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        SplashImage = findViewById(R.id.SplashImage);
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.splash_animation);
        SplashImage.startAnimation(anim);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
