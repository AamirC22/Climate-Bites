package com.app.weathernews.models;

import java.io.Serializable;

// Model class representing the source of an article
public class Source implements Serializable {
    // Data fields to store source information
    private String id;           // ID for each source initialised
    private String name;         // initialises name of each source
    private String description;  // Description of the source
    private String url;          // initialises the URL of the source
    private String category;     // initialises the source category
    private String language;     // used for the language of the source
    private String country;

    // Used to call for getting the ID of the source
    public String getId() {
        return id;
    }

    // Used to set the source iD
    public void setId(String id) {
        this.id = id;
    }

    // Gets the source name
    public String getName() {
        return name;
    }

    // Sets the source name
    public void setName(String name) {
        this.name = name;
    }

    // This provides a string representation of the source object
    @Override
    public String toString() {
        return "Source{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", category='" + category + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
