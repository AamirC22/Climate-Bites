package com.app.weathernews.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton class needed to configure a retrofit instance throughout hte app, ensures all network requests to
 * NewsAPI use a single retrofit object.
 */
public class APIClient {
    // Retrofit instance variable
    private static Retrofit mRetrofit = null;

    // Private constructor to prevent instantiation of the class
    private APIClient() {}

    /**
     * @return The singleton Retrofit instance.
     *
     * Creates or returns the retrofit instance, used to prevent multiple threads from creating instances concurrently
     */
    private static Retrofit getRetrofit(){
        // Check if Retrofit instance is null
        if(mRetrofit == null){
            // If it is null, create a new Retrofit instance to be used to build the instance
            mRetrofit = new Retrofit.Builder()
                    // Base URL for the API
                    .baseUrl("https://newsapi.org/")
                    .addConverterFactory(GsonConverterFactory.create())
                    // This builds the instance using Retrofit
                    .build();
        }

        // Return the Retrofit instance
        return mRetrofit;
    }

    // Gets the API service interface Implementation
    public static APIService getAPIService(){
        // This returns the implementation from the API service using the Retrofit instance
        return getRetrofit().create(APIService.class);
    }
}
