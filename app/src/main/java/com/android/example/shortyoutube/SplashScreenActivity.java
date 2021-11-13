package com.android.example.shortyoutube;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 2000; //in ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.shared_preference_name), MODE_PRIVATE);
        boolean aBoolean = mSharedPreferences.getBoolean(getString(R.string.shared_preference_night_mode), false);
        if(!aBoolean){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Objects.requireNonNull(getSupportActionBar()).hide();

        SpinKitView spinKitView = findViewById(R.id.pb_splash_screen);
        spinKitView.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spinKitView.setProgress(SPLASH_TIME);
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME);
    }
}