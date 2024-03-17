package com.app.weathernews.request;

/**
 * A class representing a request to fetch everything from the API.
 * This includes news articles, blogs, and other content.
 */
public class EverythingRequest {
    // Query parameter: search query
    private String q;
    // Query parameter: sources
    private String sources;
    // Query parameter: domains
    private String domains;
    // Query parameter: start date
    private String from;
    // Query parameter: end date
    private String to;
    // Query parameter: language
    private String language;
    // Query parameter: sort by
    private String sortBy;
    // Query parameter: page size
    private String pageSize;
    // Query parameter: page number
    private String page;

    // Private constructor that takes a Builder object
    private EverythingRequest(Builder builder) {
        this.q = builder.q;
        this.sources = builder.sources;
        this.domains = builder.domains;
        this.from = builder.from;
        this.to = builder.to;
        this.language = builder.language;
        this.sortBy = builder.sortBy;
        this.pageSize = builder.pageSize;
        this.page = builder.page;
    }

    // Getter methods for all query parameters
    public String getQ() {
        return q;
    }

    public String getSources() {
        return sources;
    }

    public String getDomains() {
        return domains;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getLanguage() {
        return language;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getPage() {
        return page;
    }

    // Builder class to construct an EverythingRequest object
    public static class Builder {
        private String q, sources, domains, from, to, language, sortBy, pageSize, page;

        // Methods to set query parameters
        public Builder q(String q) {
            this.q = q;
            return this;
        }

        public Builder sources(String sources) {
            this.sources = sources;
            return this;
        }

        public Builder domains(String domains) {
            this.domains = domains;
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder sortBy(String sortBy) {
            this.sortBy = sortBy;
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

        // Method to construct an EverythingRequest object
        public EverythingRequest build() {
            return new EverythingRequest(this);
        }
    }
}
