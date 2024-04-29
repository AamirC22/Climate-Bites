package com.app.weathernews.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model for a news article, contains all the variables initialises and Setters and Getters.
 * Designed to be an entity for passage between storage and being shown as Views.
 */
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
    private boolean isBookmarked;   // This is a flag that is used to check if an article is bookmarked or not

    // Getter and setter methods for data fields
    public Source getSource() {
        return source;
    }
    // Gets source of the article
    public void setSource(Source source) {
        this.source = source;
    }
    // Sets source of the article
    public String getAuthor() {
        return author;
    }
    // Retrieves the Author
    public void setAuthor(String author) {
        this.author = author;
    }
    // Sets the Author
    public String getTitle() {
        return title;
    }
    // Gets the title of the Article
    public void setTitle(String title) {
        this.title = title;
    }
    // Sets the title of the article
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
    // Gets the image url needed to display the images for each article

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
    // Sets the url to an image

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
    // Checks whether an article is bookmarked or not, True or false
    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
    // Sets an article as bookmarked if the button is pressed

    // This provides a string representation of the source object
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

    /**
     * Compares articles to other objects to check for equality, needed to check if articles are the same or not
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Article article = (Article) obj;
        return Objects.equals(title, article.title) && // Checks similarities between Articles
                Objects.equals(description, article.description) &&
                Objects.equals(author, article.author) &&
                Objects.equals(publishedAt, article.publishedAt);
    }

    /**
     * Generates a hash code for an article, ensuring consistency in the output based upon its content.
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, description, author, publishedAt);
    }
}
