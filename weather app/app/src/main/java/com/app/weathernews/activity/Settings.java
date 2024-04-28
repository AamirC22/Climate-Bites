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
    private ImageView backButton; // Creates back button image for top bar
    private Switch notificationSwitch;
    private static final int REQUEST_CODE_NOTIFICATION = 112;

    boolean isNotificationsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialises the different elements into the User interface
        backButton = findViewById(R.id.backButton);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        Paper.init(Settings.this);

        // Retrieves the status of the notification from the Paper database
        isNotificationsEnabled = Paper.book().read("isNotificationEnabled", false);

        // Switches the state and sets it based on the status of the notification
        notificationSwitch.setChecked(isNotificationsEnabled);

        // This is a switch listener that toggles when Notifications are turned on or off
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) { // This checks if the switch has been checked and then handles it
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                        if (ContextCompat.checkSelfPermission(Settings.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            // As the permission has not been granted, the system then Requests it to try and grant permission
                            ActivityCompat.requestPermissions(Settings.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION);
                        } else {
                            // The permission has been granted therefore the status of the notification is updated in the Paper Database
                            Paper.book().write("isNotificationEnabled", isChecked);
                        }
                    }
                } else {
                    // If it is, the Notification status in the Paper Database is updated and written
                    // Update notification status in Paper database
                    Paper.book().write("isNotificationEnabled", isChecked);
                }
            }
        });

        // An event listener on click that functions when the Back button is clicked
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // Handles te results of the permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NOTIFICATION) {
            // Ensures that the grant results has an actual value and is not an empty Array
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Switch is checked as the Permission has been granted
                // Therefore, the notification status updates in the paper database
                Paper.book().write("isNotificationEnabled", true);
                notificationSwitch.setChecked(true);
            } else {
                // Switch is kept unchecked as the Permission is denied
                // Therefore, the notification status is not updated in the Paper database
                notificationSwitch.setChecked(false);
            }
        }
    }
}
