package com.app.weathernews.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app.weathernews.R;

// Simple Splash Activity with a Handler : https://gist.github.com/dlfinis/ed29b7fd43f9792acb54 Author : dlfinis , Accessed 28/04/2024

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // The post delayed causes a short delay of 2 seconds before moving the user to the fragment of the main screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Intent to start the Main Activity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish the SplashActivity to prevent going back to it
            }
        }, 2000); // 2000 milliseconds (2 seconds) delay
    }
}
