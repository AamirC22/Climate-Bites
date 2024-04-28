package com.app.weathernews;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Constants {
    // These are all the sources that have been decided to be reliable and will be the ones that the API retrieves to be used in the Application
    public static final Set<String> ALLOWED_SOURCES = new HashSet<>(Arrays.asList("bbc-news", "abc-news","al-jazeera-english","associated-press","axios","bloomberg","cnn","nbc-news","national-geographic","reuters","new-scientist","new-york-magazine","rt"));

}
