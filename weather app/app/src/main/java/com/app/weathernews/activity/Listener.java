package com.app.weathernews.activity;

// This is an interface that is able to handle click events in the bottom navigation bar and loads data
public interface Listener {

    // This handles bottom navigation clicks on the bar
    void onBottomNavClick();

    // This clears existing lists and is a method to load Data
    void LoadData(boolean clearList);
}
