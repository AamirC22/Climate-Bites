package com.app.weathernews.models;

import java.io.Serializable;
import java.util.Objects;

// Model class to store article data
public class Article implements Serializable {
    // Data fields for article information
    private Source source;          // The source of the article
    private String author;          // The author of the article
    private String title;           // The title of the article
    private String description;     // The description of the article
    private String url;             // The URL of the article
    private String urlToImage;      // The URL of the article's image
    private String publishedAt;     // The publish date and time of the article
    private String content;         // The content of the article
    private boolean isBookmarked;   // Flag to indicate if the article is bookmarked or not

    // Getter and setter methods for data fields
    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    // Override toString() method to provide a string representation of the article object
    @Override
    public String toString() {
        return "Article{" +
                "source=" + source +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", content='" + content + '\'' +
                ", isBookmarked=" + isBookmarked +
                '}';
    }

    // Override equals() method to compare two article objects for equality based on certain fields
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Article article = (Article) obj;
        return Objects.equals(title, article.title) &&
                Objects.equals(description, article.description) &&
                Objects.equals(author, article.author) &&
                Objects.equals(publishedAt, article.publishedAt);
    }

    // Override hashCode() method to generate a hash code for the article object
    @Override
    public int hashCode() {
        return Objects.hash(title, description, author, publishedAt);
    }
}
