package com.app.weathernews.response;

import com.app.weathernews.models.Article;
import com.app.weathernews.models.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the response received from the API when fetching sources.
 */
public class SourcesResponse {
    private String status; // Status of the response (e.g., "ok" or "error")
    private List<Source> sources; // List of sources retrieved from the API

    private List<Article> articles; // List of articles retrieved from the API

    // Getter and setter methods for all fields


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }



}
