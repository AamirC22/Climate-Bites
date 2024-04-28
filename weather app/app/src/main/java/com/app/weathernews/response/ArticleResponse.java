package com.app.weathernews.response;

import com.app.weathernews.models.Article;
import java.util.ArrayList;

/**
 * This checks the responses received from the API when fetching articles and initialises key variables and arrays
 */
public class ArticleResponse {
    private String status; // Checks the status of the response and where it is ok or an error
    private int totalResults; // This is the total number of articles that are available to the application from the API
    private ArrayList<Article> articles; // This is the list of articles from the API put into an ArrayList

    /**
     * These are the getter and setter methods involved for the Sources, Articles and the Status and the Total Results
     */

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

    @Override // Sets the articles into strings to be used in the implementation of the articles
    public String toString() {
        return "ArticleResponse{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", articles=" + articles +
                '}';
    }
}
