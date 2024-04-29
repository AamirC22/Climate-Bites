package com.app.weathernews.request;

/**
 * A class representing a request to fetch everything from the API.
 * This includes news articles, blogs, and other content.
 */
public class EverythingRequest {

    private String q; // variable for query

    private String sources; // variable for sources

    private String domains; // varaible for domain names

    private String from; // variable for start date for the article search retrieval
    private String to; // variable for end date for the article search retrieval
    private String language; // variable for article language
    private String sortBy; // variable for sorting by date
    private String pageSize; // variable for the size of the pages
    private String page; // variable for the page

    /**
     * Private constructor to enforce the use of the Builder pattern for creating instances.
     */
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

    // Various methods of Public Getters needed to get the query parameters of the articles
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

    /**
     * Constructs an EverythingRequest instance using the Builder class,
     * Follows builder pattern and offers various parameters.
     */
    public static class Builder {
        private String q, sources, domains, from, to, language, sortBy, pageSize, page;

        // Various methods needed to set the query parameters in the builders.
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

        // This is a method that is used to construct the EverythingRequest Object
        public EverythingRequest build() {
            return new EverythingRequest(this);
        }
    }
}
