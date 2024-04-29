package com.app.weathernews.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.weathernews.R;
import com.app.weathernews.activity.Listener;
import com.app.weathernews.adapters.BookMarkAdapter;
import com.app.weathernews.models.Article;

import java.util.ArrayList;
import java.util.Collections;

import io.paperdb.Paper;

/**
 * This fragment is used to display the bookmarked news articles
 * It is the 2nd page of the 3 involved in the application
 * Implements a listener that handles the activities and if bookmarks are added
 * UI is then updated.
 */
public class BookmarkFragment extends Fragment implements Listener {

    private RecyclerView recyclerView; // Initialises a private Recycler View
    private BookMarkAdapter adapter;

    // a Private array list that stores the bookmarked articles
    private ArrayList<Article> bookmarkedItems;

    public BookmarkFragment() {
       // Empty default constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize PaperDB for local storage operations for the bookmarked articles.
        Paper.init(getContext());
        // This loads all the previously bookmarked articles from the paper database
        bookmarkedItems = Paper.book().read("bookmarked_items", new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        // This sets up the adapter and then initialises the RecyclerView
        recyclerView = view.findViewById(R.id.topicRv);
        setAdapter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * Updates the adapter's dataset and refreshes the RecyclerView.
     */
    public void updateAdapter() {
        // This updates the adapter by fetching the latest bookmarked articles
        bookmarkedItems = Paper.book().read("bookmarked_items", new ArrayList<>());
        // Newest bookmarks are put on top as it is reversed
        Collections.reverse(bookmarkedItems);
        setAdapter();
    }

    /**
     * Updates the adapter in the scenario that the bottom navigation bar is interacted with
     */
    @Override
    public void onBottomNavClick() {
        updateAdapter();
    }
    //Loads the data and clears the list if required
    @Override
    public void LoadData(boolean clearList) {
        if (clearList) {
            bookmarkedItems.clear();
        }
        updateAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Updates the adapter and fetches the bookmarked articles again when the fragment is opened
        bookmarkedItems = Paper.book().read("bookmarked_items", new ArrayList<>());
        setAdapter();
    }

    /**
     * Sets the RecyclerView adapter to display the list of bookmarked articles.
     */
    private void setAdapter() {
        // Newest bookmarks are put on top as it is reversed
        Collections.reverse(bookmarkedItems);
        adapter = new BookMarkAdapter(getContext(), bookmarkedItems, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Updates UI and notifies the adapter of any changes to the data set
        adapter.notifyDataSetChanged();
    }
}
