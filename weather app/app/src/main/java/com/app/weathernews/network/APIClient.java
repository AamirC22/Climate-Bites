package com.app.weathernews.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton class responsible for creating and providing the Retrofit instance.
 */
public class APIClient {
    // Retrofit instance variable
    private static Retrofit mRetrofit = null;

    // Private constructor to prevent instantiation of the class
    private APIClient() {}

    // Method to get the Retrofit instance
    private static Retrofit getRetrofit(){
        // Check if Retrofit instance is null
        if(mRetrofit == null){
            // If null, create a new Retrofit instance
            mRetrofit = new Retrofit.Builder()
                    // Base URL for the API
                    .baseUrl("https://newsapi.org/")
                    // Converter factory for JSON serialization/deserialization
                    .addConverterFactory(GsonConverterFactory.create())
                    // Build the Retrofit instance
                    .build();
        }

        // Return the Retrofit instance
        return mRetrofit;
    }

    // Method to get the API service interface implementation
    public static APIService getAPIService(){
        // Return the API service interface implementation created using the Retrofit instance
        return getRetrofit().create(APIService.class);
    }
}
