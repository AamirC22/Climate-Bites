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

    // Binding for activity layout
    private ActivityMainBinding binding;

    // Fragments for each section of the app
    public static Fragment homeFragment = new HomeFragment();
    Fragment teamsFragment = new BookmarkFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment; // Initially active fragment is homeFragment

    // Flags to track current screen and see where user is
    public static boolean isHome = true;
    public static boolean isTeam = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        FirebaseApp.initializeApp(MainActivity.this);

        // Handle push notifications from Firebase
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("tokenTEst", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d("tokenTEst", token);
                    }
                });

        // Check internet connection
        if (NetworkUtils.isNetworkAvailable(getApplicationContext())) {
            // Connected to the internet
        } else {
            // Not connected to the internet
            Toast.makeText(this, "No Internet connection...", Toast.LENGTH_SHORT).show();
        }

        // Initialize bottom navigation
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Add fragments to fragment manager
        fm.beginTransaction().add(R.id.nav_host_fragment, teamsFragment, "2").hide(teamsFragment).commit();
        fm.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "1").commit();
    }

    // Bottom navigation click listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: // Home fragment
                    isTeam = false;
                    if (isHome) { // If already in home fragment
                        HomeFragment home = (HomeFragment) homeFragment;
                        home.onBottomNavClick();
                        active = homeFragment;
                    } else { // If navigating to home fragment from another fragment
                        fm.beginTransaction().hide(active).show(homeFragment).commit();
                        active = homeFragment;
                        isHome = true;
                        homeFragment.onResume();
                    }
                    return true;

                case R.id.navigation_bookmark: // Bookmark fragment
                    isHome = false;
                    if (isTeam) { // If already in bookmark fragment
                        BookmarkFragment team = (BookmarkFragment) teamsFragment;
                        team.onBottomNavClick();
                        active = teamsFragment;
                    } else { // If navigating to bookmark fragment from another fragment
                        fm.beginTransaction().hide(active).show(teamsFragment).commit();
                        BookmarkFragment home = (BookmarkFragment) teamsFragment;
                        home.LoadData(false);
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

            homeFragment = new HomeFragment();
            fm.beginTransaction().add(R.id.nav_host_fragment, homeFragment, "1").commit();

            // Update the active fragment
            active = homeFragment;

            // Reset flags
            isHome = true;
            isTeam = false;

            Paper.book().write("isFromNotification", false);
        }
        Log.d("zdfdsf", "onResume: ");
    }


}
