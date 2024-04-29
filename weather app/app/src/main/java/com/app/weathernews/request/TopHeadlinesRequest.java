package com.app.weathernews.request;

/**
 * This represents the requests needed to fetch the Top Headlines from the API
 */
public class TopHeadlinesRequest {
    private String category; // initialises category variable
    private String sources;// initialises sources variable
    private String q;// initialises q variable
    private String pageSize;// initialises page size variable
    private String page;// initialises page variable
    private String country;// initialises country variable
    private String language;// initialises Language variable

    // This takes a builder object as a private constructor and creates a Request
    private TopHeadlinesRequest(Builder builder) {
        this.category = builder.category;
        this.sources = builder.sources;
        this.q = builder.q;
        this.pageSize = builder.pageSize;
        this.page = builder.page;
        this.country = builder.country;
        this.language = builder.language;
    }

    // This is all the getter methods for the parameters of the query for Top headlines
    public String getCategory() {
        return category;
    }

    public String getSources() {
        return sources;
    } // Gets the Sources

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
    } // Gets the Country

    public String getLanguage() {
        return language;
    } // Gets the Language

    /**
     * The Builder class used to construct an instance of TopHeadlinesRequest with desired parameters.
     * The Builder uses fluent interface style to make the client code more readable.
     */
    public static class Builder {
        private String q;
        private String category;
        private String sources;
        private String country;
        private String language;
        private String pageSize;
        private String page;

        // Default constructor
        public Builder() {} // This provides Flexibility to the Builder and acts as a Constructor

        // Methods to set query parameters
        public Builder q(String q) {
            this.q = q; // Search keyword or phrase
            return this;
        }

        public Builder category(String category) {
            this.category = category; // Builds the topic and category of the news
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
            this.page = String.valueOf(page); // The page number needed to return, this is necessary for Pagination
            return this;
        }

        // Method to construct a TopHeadlinesRequest object
        public TopHeadlinesRequest build() {
            return new TopHeadlinesRequest(this);
        }
    }
}
