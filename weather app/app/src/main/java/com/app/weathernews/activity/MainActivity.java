package com.app.weathernews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.app.weathernews.R;
import com.app.weathernews.fragment.BookmarkFragment;
import com.app.weathernews.fragment.HomeFragment;
import com.app.weathernews.models.Article;
import com.app.weathernews.network.NetworkUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.weathernews.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    // This is the binding for the layout of the activity
    private ActivityMainBinding binding;

    // Initialising each fragment and section of the application
    public static Fragment homeFragment = new HomeFragment();
    Fragment teamsFragment = new BookmarkFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment; // The default fragment when the app opens is the home Fragment

    // Variables that check where the user is and which fragment they are on
    public static boolean isHome = true;
    public static boolean isTeam = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialises Firebase App into the application
        FirebaseApp.initializeApp(MainActivity.this);

        // Method to handle the notifications received from Firebase
        // A token is generated and then there is a check to ensure if the task is successful or not
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("tokenTEst", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Gets a new FCM Registration Token
                        String token = task.getResult();
                        Log.d("tokenTEst", token);
                    }
                });


        // Ensures that the user has internet connection
        // If the user does not have internet, there is an error shown on the screen
        if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
            // Connected to the internet
        } else {
            // Not connected to the internet
            Toast.makeText(this, "No Internet connection...", Toast.LENGTH_SHORT).show();
        }

        // This initialises the bottom Navigation bar
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // This adds the different fragments such as nav host and teamsFrag to the fragment manager
        fm.beginTransaction().add(R.id.nav_host_fragment, teamsFragment, "2").hide(teamsFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "1").commit();
    }

    // This sets a listener that checks regarding the Bottom navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: // Home fragment
                    isTeam = false;
                    if (isHome) { // This checks if the user is already in the Home main activity fragment
                        HomeFragment home = (HomeFragment) homeFragment;
                        home.onBottomNavClick();
                        active = homeFragment;
                    } else { // Checks if the user is navigating to the Home main activity fragment from another fragment
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        isHome = true;
                        homeFragment.onResume();
                    }
                    return true;
                // Sets a case that checks regarding the bookmark fragment
                case R.id.navigation_bookmark: // Bookmark fragment
                    isHome = false;
                    if (isTeam) { // This is true if the user is already in the bookmark fragment
                        BookmarkFragment team = (BookmarkFragment) teamsFragment;
                        team.onBottomNavClick();
                        active = teamsFragment;
                    } else { // Checks if the user is navigating to the bookmark fragment from another fragment
                        fm.beginTransaction().hide(active).show(teamsFragment).commit();
                        BookmarkFragment home = (BookmarkFragment) teamsFragment;
                        home.LoadData(false);
                        // Sets the active fragment of the user to bookmark fragment
                        active = teamsFragment;
                        isTeam = true;
                        teamsFragment.onResume();
                    }
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        boolean isFromNotification = Paper.book().read("isFromNotification",false);
        if (isFromNotification){

            fm.beginTransaction().remove(active).commit();
            // Creates a new home fragment
            homeFragment = new HomeFragment();
            fm.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "1").commit();

            // This changes the active fragment that the user is on back to the Home Fragment
            active = homeFragment;

            // The flags on where the user is are reset, and user is sent to home page
            isHome = true;
            isTeam = false;
            // This resets the notification flag and makes it back to true
            Paper.book().write("isFromNotification", false);
        }
        Log.d("zdfdsf", "onResume: ");
    }


}
