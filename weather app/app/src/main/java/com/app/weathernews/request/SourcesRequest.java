package com.app.weathernews.request;

/**
 * This acts as a sources request from the NewsAPI and uses a builder pattern for object creation,
 * emphasises the need for necessary query parameters and filters the news sources by category language and country
 */
public class SourcesRequest {
    // These are the query parameters for the API Request
    private String category; // Category of the news sources
    private String language; // Language of the news sources
    private String country;  // Country of the news sources

    /**
     * This is a builder constructor that initialises a SourcesRequest object and uses a private constructor
     * to enforce builder dependency for object creation
     * @param builder This contains the instance needed to set up the parameters using the Builder object
     */
    public SourcesRequest(Builder builder) {
        this.category = builder.category;
        this.language = builder.language;
        this.country = builder.country;
    }

   // Getter methods for the Category of the News Source
    public String getCategory() {
        return category;
    }

    // Getter methods for the Language of the News Source
    public String getLanguage() {
        return language;
    }

    // Getter methods for the Country of the news source
    public String getCountry() {
        return country;
    }

    /**
     * A static class Builder for SourcesRequest, configures methods needed to instantiate it
     * Sets optional and required parameters in a flexible manner
     */
    public static class Builder {
        private String category; // Variable for Category value
        private String language; // Variable for Language Value
        private String country;  //  Variable for country value

        // This provides Flexibility to the Builder and acts as a Constructor
        public Builder() {}

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

        /**
         * It builds and returns the sources request with the parameters that have been configured
         * @return A new instance of SourcesRequest with the configured parameters.
         */
        public SourcesRequest build() {
            return new SourcesRequest(this);
        }
    }
}
