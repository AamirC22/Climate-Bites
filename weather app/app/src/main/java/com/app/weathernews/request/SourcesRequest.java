package com.app.weathernews.request;

/**
 * A class representing a request to fetch news sources from the API.
 */
public class SourcesRequest {
    // Query parameter: category
    private String category;
    // Query parameter: language
    private String language;
    // Query parameter: country
    private String country;

    // Private constructor that takes a Builder object
    public SourcesRequest(Builder builder) {
        this.category = builder.category;
        this.language = builder.language;
        this.country = builder.country;
    }

    // Getter methods for all query parameters
    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    // Builder class to construct a SourcesRequest object
    public static class Builder {
        private String category, language, country;

        // Default constructor
        public Builder() {}

        // Methods to set query parameters
        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        // Method to construct a SourcesRequest object
        public SourcesRequest build() {
            return new SourcesRequest(this);
        }
    }
}
