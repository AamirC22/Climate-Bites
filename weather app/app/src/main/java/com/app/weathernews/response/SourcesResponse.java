package com.app.weathernews.response;

import com.app.weathernews.models.Article;
import com.app.weathernews.models.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the response received from the API when fetching sources.
 */
public class SourcesResponse {
    private String status; // Checks the status of the response and where it is ok or an error
    private List<Source> sources; // This retrieves the list of sources from the API

    private List<Article> articles; // This is the List of articles that are retrieved from the API and put in an Array

    /**
     * These are the getter and setter methods involved for the Sources, Articles and the Status
     */
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
