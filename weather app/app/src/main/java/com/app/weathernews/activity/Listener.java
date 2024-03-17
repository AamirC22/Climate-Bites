package com.app.weathernews.activity;

// Interface to handle bottom navigation click events and data loading
public interface Listener {

    // Method to handle bottom navigation click
    void onBottomNavClick();

    // Method to load data with an option to clear existing list
    void LoadData(boolean clearList);
}
