package com.app.weathernews.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility class for network-related operations.
 */
public class NetworkUtils {

    /**
     * Function to check if the device is connected to the internet.
     * @param context The context of the application/activity.
     * @return True if the device is connected to the internet, false otherwise.
     */
    public static boolean isNetworkAvailable(Context context) {
        // Get the connectivity manager system service
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // Get the active network information
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            // Check if the active network info is not null and is connected
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        // Return false if connectivity manager is null or no active network info is available
        return false;
    }
}
