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
            // This parses the JSON string and initialises the JSON Object to extract the error message.
            JSONObject obj = new JSONObject(str);
            // This sets the string errorMessage and gets the String of the error and sets it
            String errorMessage = obj.getString("message");
            // This creates a new throwable by getting the message of the Error
            throwableObj = new Throwable(errorMessage);
        } catch (JSONException e) {
            // If the parsing for whatever reason fails, this catches the exception and prints it to be used for debugging
            e.printStackTrace(); // Log the exception in order to be used for potential debugging purposes
        }

        // This if statement checks if there is a problem with the throwable Object and it has been set as null
        if (throwableObj == null) {
            // If throwableObj is null, send a message that informs of it to be flagged for Debugging
            throwableObj = new Throwable("There has been an error involving the throwable object.");
        }
        // This returns the throwable object to the application to be used
        return throwableObj;
    }

    /**
     * This is a Helper method that creates a map of the different query paramters for the API requests
     * and ensures it is always involved and included
     * @return It returns an API Key and the Map object involving the query parameters.
     */
    private Map<String, String> createQuery() {
        query = new HashMap<>();
        query.put("apiKey", mApiKey); // This adds the API key to the parameters of the Query
        return query;
    }

    /**
     * This fetches the news sources that are set to be retrieved, utilising Source Request
     *
     * @param sourcesRequest The request parameters for fetching news sources.
     * @param sources This is the list of the sources that are needed to be retrieved from the API
     * @param callback Needed to handle the response from the API
     */
    public void getSources(SourcesRequest sourcesRequest, List<String> sources, final SourcesCallback callback) {
        query = createQuery();
        // Extract query parameters from SourcesRequest object
        String category = sourcesRequest.getCategory(); // gets the category
        String language = sourcesRequest.getLanguage();// gets the language
        String country = sourcesRequest.getCountry();// gets the country

        query.put("category", category); // adds category to the query parameters
        query.put("language", language);// adds language to the query parameters
        query.put("country", country);// adds country to the query parameters

        // This adds the sources to the query if the array of sources is not empty
        if (sources != null && !sources.isEmpty()) {
            query.put("sources", String.join(",", sources));
        }

        query.values().removeAll(Collections.singleton(null)); // this removes all the values from the Query parameters that are null

        mAPIService.getSources(query)
                .enqueue(new Callback<SourcesResponse>() {
                    @Override
                    public void onResponse(Call<SourcesResponse> call, retrofit2.Response<SourcesResponse> response) {
                        if (response.code() == HttpURLConnection.HTTP_OK) // Checks if the HTTP urls of the articles are ok
                            {
                            callback.onSuccess(response.body());
                        } else {
                            try {
                                callback.onFailure(errorMessages(response.errorBody().string()));
                            } catch (IOException e) // Sends an error message if the URLs from the API are not valid
                            {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override // This is the method that is called if the sources response fails
                    public void onFailure(Call<SourcesResponse> call, Throwable throwable) {
                        callback.onFailure(throwable);
                    }
                });
    }

    /**
     * This fetches the TopHeadlines from the API
     *
     * @param topHeadlinesRequest The request parameters for fetching top headlines and then displaying it in the application
     * @param callback Needed to handle the response from the API
     */
    public void getTopHeadlines(TopHeadlinesRequest topHeadlinesRequest, final ArticlesResponseCallback callback) {
        query = createQuery();
        query.put("country", topHeadlinesRequest.getCountry()); // gets country from API
        query.put("language", topHeadlinesRequest.getLanguage());// gets language from API
        query.put("category", topHeadlinesRequest.getCategory());// gets category from API
        query.put("sources", topHeadlinesRequest.getSources());// gets the article source from API
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

    /**
     * This fetches ALL the articles from the NewsAPI based on the parameters in EverythingRequest
     *
     * @param everythingRequest The request parameters for fetching all the news articles
     * @param callback Needed to handle the callbacks from the response of the API
     */
    public void getEverything(EverythingRequest everythingRequest, final ArticlesResponseCallback callback) {
        // This creates the query parameters that are needed for the API Request
        query = createQuery();
        query.put("q", everythingRequest.getQ());
        query.put("sources", String.join(",", Constants.ALLOWED_SOURCES));// Adds the allowed sources parameters to the query
        query.put("domains", everythingRequest.getDomains()); // Adds the Domains parameter to the query
        query.put("from", everythingRequest.getFrom()); // Adds the date from  parameter to the query
        query.put("to", everythingRequest.getTo()); // Adds the date up to parameter to the query
        query.put("language", everythingRequest.getLanguage()); // Adds the Language parameter to the query
        query.put("sortBy", everythingRequest.getSortBy()); // Adds the sorting parameter to the query
        query.put("pageSize", everythingRequest.getPageSize()); // Adds the page size parameter to the query
        query.put("page", everythingRequest.getPage()); // Adds the page parameter to the query

        // This is needed to remove all the null values that are grabbed from the Query Parameters
        query.values().removeAll(Collections.singleton(null));
        // Ensures no null string values remain and are removed
        query.values().removeAll(Collections.singleton("null"));

        // Make the API call to fetch everything
        mAPIService.getEverything(query)
                .enqueue(new Callback<ArticleResponse>() {
                    @Override
                    public void onResponse(Call<ArticleResponse> call, retrofit2.Response<ArticleResponse> response) {
                        // Ensures that the API call results in success and handles error if otherwise
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            // Invoke the onSuccess callback with the response body, sends it into the console
                            callback.onSuccess(response.body());
                        } else {
                            try {
                                // logs error if failure, sends to console for Debugging
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
}
