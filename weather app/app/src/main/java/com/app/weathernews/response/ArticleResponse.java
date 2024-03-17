package com.app.weathernews.response;

import com.app.weathernews.models.Article;
import java.util.ArrayList;

/**
 * This class represents the response received from the API when fetching articles.
 */
public class ArticleResponse {
    private String status; // Status of the response (e.g., "ok" or "error")
    private int totalResults; // Total number of articles available
    private ArrayList<Article> articles; // List of articles retrieved from the API

    // Getter and setter methods for all fields

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "ArticleResponse{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", articles=" + articles +
                '}';
    }
}
