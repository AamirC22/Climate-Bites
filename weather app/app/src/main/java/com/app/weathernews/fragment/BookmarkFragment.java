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
     * Callback method triggered by interactions in the bottom navigation.
     * Currently used to refresh the list of bookmarked articles.
     */
    @Override
    public void onBottomNavClick() {
        updateAdapter();
    }

    /**
     * Callback method to refresh the list or clear it based on the provided parameter.
     * @param clearList If true, clear the list; otherwise, refresh the list.
     */
    @Override
    public void LoadData(boolean clearList) {
        if (clearList) {
            bookmarkedItems.clear(); // Clear the list if requested.
        }
        updateAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Re-fetch bookmarked items when fragment resumes and update the adapter.
        bookmarkedItems = Paper.book().read("bookmarked_items", new ArrayList<>());
        setAdapter();
    }

    /**
     * Sets the RecyclerView adapter to display the list of bookmarked articles.
     */
    private void setAdapter() {
        // Reverse the bookmark list each time to ensure the newest items appear at the top.
        Collections.reverse(bookmarkedItems);
        adapter = new BookMarkAdapter(getContext(), bookmarkedItems, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Notify the adapter of data changes so the UI can be updated.
        adapter.notifyDataSetChanged();
    }
}
