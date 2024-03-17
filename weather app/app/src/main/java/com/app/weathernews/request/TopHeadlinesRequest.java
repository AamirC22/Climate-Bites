package com.app.weathernews.request;

/**
 * A class representing a request to fetch top headlines from the API.
 */
public class TopHeadlinesRequest {
    // Query parameters
    private String category;
    private String sources;
    private String q;
    private String pageSize;
    private String page;
    private String country;
    private String language;

    // Private constructor that takes a Builder object
    private TopHeadlinesRequest(Builder builder) {
        this.category = builder.category;
        this.sources = builder.sources;
        this.q = builder.q;
        this.pageSize = builder.pageSize;
        this.page = builder.page;
        this.country = builder.country;
        this.language = builder.language;
    }

    // Getter methods for all query parameters
    public String getCategory() {
        return category;
    }

    public String getSources() {
        return sources;
    }

    public String getQ() {
        return q;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getPage() {
        return page;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }

    // Builder class to construct a TopHeadlinesRequest object
    public static class Builder {
        private String q;
        private String category;
        private String sources;
        private String country;
        private String language;
        private String pageSize;
        private String page;

        // Default constructor
        public Builder() {}

        // Methods to set query parameters
        public Builder q(String q) {
            this.q = q;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder sources(String sources) {
            this.sources = sources;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder pageSize(int pageSize) {
            this.pageSize = String.valueOf(pageSize);
            return this;
        }

        public Builder page(int page) {
            this.page = String.valueOf(page);
            return this;
        }

        // Method to construct a TopHeadlinesRequest object
        public TopHeadlinesRequest build() {
            return new TopHeadlinesRequest(this);
        }
    }
}
