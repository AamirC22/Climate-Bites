package com.app.weathernews.network;

import com.app.weathernews.response.ArticleResponse;
import com.app.weathernews.response.SourcesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * Flexible interface that defines API endpoints for the news api. Uitilises Retrofit to create an implementation
 * that makes network requests to endpoints.
 * Each method corresponds to endpoint of the NewsAPI and define different HTTP Operations to perform (GET)
 *
 */
public interface APIService {

    /**
     *
     * @param query A map that shows the parameters of the Query for the API Request
     * @return Call object that has a request and response for the SourcesResponse
     *
     * This GETs all the list of the news sources from the News API and corresponds to the sources endpoint.
     * HTTP GET is used for the request. It takes a map of the query parameters involving category,
     * Language and Country
     */
    @GET("/v2/sources")
    Call<SourcesResponse> getSources(@QueryMap Map<String, String> query);

    /**
     *
     * @param query A map that shows the parameters of the Query for the API Request to filter the headlines
     * @return Call object that has a request and response for the SourcesResponse
     *
     * This GETs all the list of the top headlines from the News API and corresponds to the '/v2/top-headlines' endpoint.
     * HTTP GET is used for the request. It takes a map of the query parameters involving 'country',
     * 'category', 'sources', 'q' (for search query), and pagination options like 'pageSize' and 'page'.
     */
    @GET("/v2/top-headlines")
    Call<ArticleResponse> getTopHeadlines(@QueryMap Map<String, String> query);

    /**
     * Fetches the news articles from the News API via the endpoint of ('/v2/everything')
     * Needed to filter through all the articles to find the specified queries, implements GET request
     *
     * @param query A map that shows the parameters of the Query for the API Request to filter the news articles
     * @return  A call object for requests and responses relating to the
     *         'everything' endpoint
     */
    @GET("/v2/everything")
    Call<ArticleResponse> getEverything(@QueryMap Map<String, String> query);
}
