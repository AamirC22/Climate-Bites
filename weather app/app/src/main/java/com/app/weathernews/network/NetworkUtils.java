package com.app.weathernews.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * This is a utility class that checks the network connectivitiy status within the device that is running
 * the application. Methods are used to determine if the device is offline or not,
 * Very important as internet is needed to retrieve articles via API calls
 */
public class NetworkUtils {
    /**
     * This method checks if the device has internet connectivity or not.
     * Without network connectivity, the application would not be able to function or attempt
     * network operations without the connectivity.
     * @param context Says the context of the application or activity, important for accessing
     *                System services, passed from an Activity or Application subclass.
     * @return boolean that returns True if device connected, false if otherwise
     *
     * ConnectivityManager is utilises to check the current state of the network.
     * It is a system service that manages network connections and informs the application.
     */
    public static boolean isNetworkAvailable(Context context) {
        // Get the ConnectivityManager Service to be used by the system
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // Gets the network information of the Device
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            // Returns depending on if the network info is null or not
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        // Return false if there is no network or if the connectivity manager returns Null
        return false;
    }
}
