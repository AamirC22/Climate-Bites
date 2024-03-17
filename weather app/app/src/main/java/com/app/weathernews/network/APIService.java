package com.app.weathernews.network;

import com.app.weathernews.response.ArticleResponse;
import com.app.weathernews.response.SourcesResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Interface defining the API endpoints and their corresponding HTTP methods.
 */
public interface APIService {
    // Endpoint to fetch sources
    @GET("/v2/sources")
    // Method to make a GET request to the sources endpoint
    Call<SourcesResponse> getSources(@QueryMap Map<String, String> query);

    // Endpoint to fetch top headlines
    @GET("/v2/top-headlines")
    // Method to make a GET request to the top-headlines endpoint
    Call<ArticleResponse> getTopHeadlines(@QueryMap Map<String, String> query);

    // Endpoint to fetch everything
    @GET("/v2/everything")
    // Method to make a GET request to the everything endpoint
    Call<ArticleResponse> getEverything(@QueryMap Map<String, String> query);
}
