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

public class BookmarkFragment extends Fragment implements Listener {

    private RecyclerView recyclerView;
    private BookMarkAdapter adapter;

    // ArrayList to store bookmarked items
    private ArrayList<Article> bookmarkedItems;

    public BookmarkFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Paper for local storage
        Paper.init(getContext());
        // Fetch bookmarked items from local database and store them in 'bookmarkedItems'
        bookmarkedItems = Paper.book().read("bookmarked_items", new ArrayList<>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        // Find the RecyclerView in the layout
        recyclerView = view.findViewById(R.id.topicRv);
        // Set the adapter for the RecyclerView
        setAdapter();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Any additional logic can be added here if needed
    }

    // Method to update the adapter's dataset and refresh the RecyclerView
    public void updateAdapter() {
        // Fetch bookmarked items from local database
        bookmarkedItems = Paper.book().read("bookmarked_items", new ArrayList<>());
        // Reverse the list to show the latest bookmarks first
        Collections.reverse(bookmarkedItems);
        // Set the adapter again to refresh the RecyclerView
        setAdapter();
    }

    // Callback method for bottom navigation click
    @Override
    public void onBottomNavClick() {
        // Call updateAdapter() method to refresh the RecyclerView
        updateAdapter();
    }

    // Callback method to load data
    @Override
    public void LoadData(boolean clearList) {
        // Call updateAdapter() method to refresh the RecyclerView
        updateAdapter();
    }

    // Method called when the fragment resumes
    @Override
    public void onResume() {
        super.onResume();
        // Fetch bookmarked items from local database and refresh the adapter
        bookmarkedItems = Paper.book().read("bookmarked_items", new ArrayList<>());
        setAdapter();
    }

    // Method to set the adapter in the RecyclerView to display the bookmarked items
    private void setAdapter() {
        // Reverse the list to show the latest bookmarks first
        Collections.reverse(bookmarkedItems);
        // Initialize the adapter with the bookmarked items and set it to the RecyclerView
        adapter = new BookMarkAdapter(getContext(), bookmarkedItems, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.notifyDataSetChanged();
    }
}
