package com.app.weathernews;

import com.app.weathernews.network.APIClient;
import com.app.weathernews.network.APIService;
import com.app.weathernews.request.EverythingRequest;
import com.app.weathernews.request.SourcesRequest;
import com.app.weathernews.request.TopHeadlinesRequest;
import com.app.weathernews.response.ArticleResponse;
import com.app.weathernews.response.SourcesResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

// This class is responsible for interacting with the News API and handling requests and responses.
public class NewsApiClient {
    private String mApiKey; // API key used for authentication
    private Map<String, String> query; // Query parameters for API requests
    private APIService mAPIService; // Service interface for making API calls

    // Constructor to initialize the API key and APIService
    public NewsApiClient(String apiKey) {
        mApiKey = apiKey;
        mAPIService = APIClient.getAPIService();
        query = new HashMap<>();
        query.put("apiKey", apiKey); // Adding API key to the query parameters
    }

    // Callback interfaces for handling API responses
    public interface SourcesCallback {
        void onSuccess(SourcesResponse response);
        void onFailure(Throwable throwable);
    }

    public interface ArticlesResponseCallback {
        void onSuccess(ArticleResponse response);
        void onFailure(Throwable throwable);
    }

    // Method to parse the error message from the API response
    // Method to create a Throwable object with an error message extracted from a JSON string
    private Throwable errMsg(String str) {
        Throwable throwable = null; // Initialize throwable object

        try {
            // Parse the JSON string to extract error message
            JSONObject obj = new JSONObject(str);
            // Get the error message from the JSON object
            String errorMessage = obj.getString("message");
            // Create a Throwable object with the extracted error message
            throwable = new Throwable(errorMessage);
        } catch (JSONException e) {
            // Catch JSONException if parsing fails
            e.printStackTrace(); // Print stack trace for debugging
        }

        // Check if throwable is still null (no error message extracted or JSONException occurred)
        if (throwable == null) {
            // If throwable is null, create a new Throwable object with a generic error message
            throwable = new Throwable("An error occurred");
        }

        // Return the created Throwable object (either with extracted error message or generic error message)
        return throwable;
    }

    // Method to create the query parameters for API requests
    private Map<String, String> createQuery() {
        query = new HashMap<>();
        query.put("apiKey", mApiKey); // Adding API key to the query parameters
        return query;
    }

    // Method to fetch news sources from the API
    public void getSources(SourcesRequest sourcesRequest, final SourcesCallback callback) {
        query = createQuery();
        query.put("category", sourcesRequest.getCategory());
        query.put("language", sourcesRequest.getLanguage());
        query.put("country", sourcesRequest.getCountry());

        query.values().removeAll(Collections.singleton(null));

        mAPIService.getSources(query)
                .enqueue(new Callback<SourcesResponse>() {
                    @Override
                    public void onResponse(Call<SourcesResponse> call, retrofit2.Response<SourcesResponse> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            callback.onSuccess(response.body());
                        } else {
                            try {
                                callback.onFailure(errMsg(response.errorBody().string()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SourcesResponse> call, Throwable throwable) {
                        callback.onFailure(throwable);
                    }
                });
    }

    // Method to fetch top headlines from the API
    public void getTopHeadlines(TopHeadlinesRequest topHeadlinesRequest, final ArticlesResponseCallback callback) {
        query = createQuery();
        query.put("country", topHeadlinesRequest.getCountry());
        query.put("language", topHeadlinesRequest.getLanguage());
        query.put("category", topHeadlinesRequest.getCategory());
        query.put("sources", topHeadlinesRequest.getSources());
        query.put("q", topHeadlinesRequest.getQ());
        query.put("pageSize", topHeadlinesRequest.getPageSize());
        query.put("page", topHeadlinesRequest.getPage());

        query.values().removeAll(Collections.singleton(null));
        query.values().removeAll(Collections.singleton("null"));

        mAPIService.getTopHeadlines(query)
                .enqueue(new Callback<ArticleResponse>() {
                    @Override
                    public void onResponse(Call<ArticleResponse> call, retrofit2.Response<ArticleResponse> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            callback.onSuccess(response.body());
                        } else {
                            try {
                                callback.onFailure(errMsg(response.errorBody().string()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArticleResponse> call, Throwable throwable) {
                        callback.onFailure(throwable);
                    }
                });
    }

    // Method to fetch everything from the API
    public void getEverything(EverythingRequest everythingRequest, final ArticlesResponseCallback callback) {
        // Create query parameters for the API request
        query = createQuery();
        query.put("q", everythingRequest.getQ()); // Add search query parameter
        query.put("sources", everythingRequest.getSources()); // Add sources parameter
        query.put("domains", everythingRequest.getDomains()); // Add domains parameter
        query.put("from", everythingRequest.getFrom()); // Add from date parameter
        query.put("to", everythingRequest.getTo()); // Add to date parameter
        query.put("language", everythingRequest.getLanguage()); // Add language parameter
        query.put("sortBy", everythingRequest.getSortBy()); // Add sort by parameter
        query.put("pageSize", everythingRequest.getPageSize()); // Add page size parameter
        query.put("page", everythingRequest.getPage()); // Add page parameter

        // Remove null values from the query parameters
        query.values().removeAll(Collections.singleton(null));
        // Remove "null" string values from the query parameters
        query.values().removeAll(Collections.singleton("null"));

        // Make the API call to fetch everything
        mAPIService.getEverything(query)
                .enqueue(new Callback<ArticleResponse>() {
                    @Override
                    public void onResponse(Call<ArticleResponse> call, retrofit2.Response<ArticleResponse> response) {
                        // Check if the response code indicates success
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            // Invoke the onSuccess callback with the response body
                            callback.onSuccess(response.body());
                        } else {
                            try {
                                // Invoke the onFailure callback with the error message obtained from the response body
                                callback.onFailure(errMsg(response.errorBody().string()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArticleResponse> call, Throwable throwable) {
                        // Invoke the onFailure callback with the Throwable object
                        callback.onFailure(throwable);
                    }
                });
    }
}
