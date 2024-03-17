package com.app.weathernews.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.app.weathernews.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Delay for 2 seconds before moving to the Main Screen
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
