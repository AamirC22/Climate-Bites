package com.app.weathernews;

import com.app.weathernews.network.APIClient;
import com.app.weathernews.network.APIService;
import com.app.weathernews.request.EverythingRequest;
import com.app.weathernews.request.SourcesRequest;
import com.app.weathernews.request.TopHeadlinesRequest;
import com.app.weathernews.response.ArticleResponse;
import com.app.weathernews.response.SourcesResponse;
// Implements the responses needed for the News api collection client

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

// interacts with the news api and handles the responses and requests
// communicates with NewsApi regarding to what articles should be collected
public class NewsApiClient {
    private String mApiKey; // API Key that is required for the authentication process to be granted
    private Map<String, String> query; // Variable for the parameters for API Requests
    private APIService mAPIService; // Service interface for making API calls

    // Constructor that is needed to initialise the API Service and the API Key
    // Needed to make the API be able to function within the application
    public NewsApiClient(String apiKey) {
        mApiKey = apiKey;
        mAPIService = APIClient.getAPIService();
        query = new HashMap<>();
        query.put("apiKey", apiKey); // Adds the API Key to the query parameters to access the API
    }

    // Different callback interferences that are in response to different outcomes from the API
    public interface SourcesCallback {
        void onSuccess(SourcesResponse response);
        void onFailure(Throwable throwable);
    }

    // Different callback interferences that are in response to different outcomes from the API
    public interface ArticlesResponseCallback {
        void onSuccess(ArticleResponse response);
        void onFailure(Throwable throwable);
    }

    // This parses an error message from the API Response and throws an Error Message
    // Method to create a Throwable object with an error message extracted from a JSON string
    private Throwable errorMessages(String str) {
        Throwable throwableObj = null; // This initialises a throwableObj object and set to null

        try {
            // Parse the JSON string to extract error message
            JSONObject obj = new JSONObject(str);
            // Get the error message from the JSON object
            String errorMessage = obj.getString("message");
            // Create a Throwable object with the extracted error message
            throwableObj = new Throwable(errorMessage);
        } catch (JSONException e) {
            // Catch JSONException if parsing fails
            e.printStackTrace(); // Print stack trace for debugging
        }

        // Check if throwableObj is still null (no error message extracted or JSONException occurred)
        if (throwableObj == null) {
            // If throwableObj is null, create a new Throwable object with a generic error message
            throwableObj = new Throwable("An error occurred");
        }

        // Return the created Throwable object (either with extracted error message or generic error message)
        return throwableObj;
    }

    // Method to create the query parameters for API requests
    private Map<String, String> createQuery() {
        query = new HashMap<>();
        query.put("apiKey", mApiKey); // Adding API key to the query parameters
        return query;
    }

    // Method to fetch news sources from the API
    public void getSources(SourcesRequest sourcesRequest, List<String> sources, final SourcesCallback callback) {
        query = createQuery();
        // Extract query parameters from SourcesRequest object
        String category = sourcesRequest.getCategory();
        String language = sourcesRequest.getLanguage();
        String country = sourcesRequest.getCountry();

        query.put("category", category);
        query.put("language", language);
        query.put("country", country);

        // Add the sources to the query if provided
        if (sources != null && !sources.isEmpty()) {
            query.put("sources", String.join(",", sources));
        }

        query.values().removeAll(Collections.singleton(null));

        mAPIService.getSources(query)
                .enqueue(new Callback<SourcesResponse>() {
                    @Override
                    public void onResponse(Call<SourcesResponse> call, retrofit2.Response<SourcesResponse> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            callback.onSuccess(response.body());
                        } else {
                            try {
                                callback.onFailure(errorMessages(response.errorBody().string()));
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
                                callback.onFailure(errorMessages(response.errorBody().string()));
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
        query.put("sources", String.join(",", Constants.ALLOWED_SOURCES));// Add sources parameter
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
                                callback.onFailure(errorMessages(response.errorBody().string()));
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
