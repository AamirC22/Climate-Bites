package com.app.weathernews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.app.weathernews.R;

import io.paperdb.Paper;

public class Settings extends AppCompatActivity {
    private ImageView backButton;
    private Switch notificationSwitch;
    private static final int REQUEST_CODE_NOTIFICATION = 112;

    boolean isNotificationsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize UI elements
        backButton = findViewById(R.id.backButton);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        Paper.init(Settings.this);

        // Retrieve notification status from Paper database
        isNotificationsEnabled = Paper.book().read("isNotificationEnabled", false);

        // Set switch state based on notification status
        notificationSwitch.setChecked(isNotificationsEnabled);

        // Switch listener for notification toggle
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) { // If switch is checked
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                        if (ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            // Permission not granted, request it
                            ActivityCompat.requestPermissions(Settings.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION);
                        } else {
                            // Permission granted, update notification status in Paper database
                            Paper.book().write("isNotificationEnabled", isChecked);
                        }
                    }
                } else { // If switch is unchecked
                    // Update notification status in Paper database
                    Paper.book().write("isNotificationEnabled", isChecked);
                }
            }
        });

        // Back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, update notification status in Paper database and set switch checked
                Paper.book().write("isNotificationEnabled", true);
                notificationSwitch.setChecked(true);
            } else {
                // Permission denied, set switch unchecked
                notificationSwitch.setChecked(false);
            }
        }
    }
}
