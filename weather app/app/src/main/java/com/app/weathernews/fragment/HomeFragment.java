package com.app.weathernews.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.weathernews.NewsApiClient;
import com.app.weathernews.R;
import com.app.weathernews.activity.DetailsActivity;
import com.app.weathernews.activity.Listener;
import com.app.weathernews.activity.MainActivity;
import com.app.weathernews.activity.Settings;
import com.app.weathernews.adapters.NewsAdapter;
import com.app.weathernews.models.Article;
import com.app.weathernews.network.NetworkUtils;
import com.app.weathernews.request.EverythingRequest;
import com.app.weathernews.response.ArticleResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.paperdb.Paper;

public class HomeFragment extends Fragment implements Listener {

    // Declaration of class variables

    // List to store article items
    private List<Article> arrayList;

    // SpinKitViews for loading animations
    SpinKitView spinKitView;
    SpinKitView spinKitTopNews;

    // Boolean to track if data is loaded for the first time
    boolean isFirstLoad = true;

    // Boolean to track if data is loaded
    boolean isDataLoaded = false;

    // RequestQueue for making API requests
    private RequestQueue requestQueue;

    // RecyclerView to display articles
    RecyclerView recyclerView;

    // UI elements for top news
    CardView cardView;
    ImageView notificationBtn;
    private ImageView topNewsImage, bookmarkButtonTopNews;
    private TextView titleTopNews, descriptionTopNews, authorTopNews, timeTopNews;

    // Model to store top news article
    Article topNews;

    // Adapter to set items in RecyclerView
    NewsAdapter adapter;

    // SwipeRefreshLayout for refreshing data
    private SwipeRefreshLayout swipeRefreshLayout;

    // Boolean to track if new data is loaded
    boolean isnew = false;

    // Constructor for HomeFragment
    public HomeFragment() {
        // Required empty public constructor
    }

    // Static method to create a new instance of HomeFragment
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    // Lifecycle method called when fragment is created
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Lifecycle method called to create the fragment's view hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView and SpinKitViews
        recyclerView = view.findViewById(R.id.topicRv);
        spinKitView = view.findViewById(R.id.spin_kit);
        spinKitTopNews = view.findViewById(R.id.spin_kit_main_image);

        // Initialize top news article
        topNews = new Article();

        // Find UI elements by their IDs
        topNewsImage = view.findViewById(R.id.imageView_top);
        bookmarkButtonTopNews = view.findViewById(R.id.bookmarkButton_top_news);
        titleTopNews = view.findViewById(R.id.titleTextView_top_news);
        descriptionTopNews = view.findViewById(R.id.descriptionTextView_top_news);
        authorTopNews = view.findViewById(R.id.authorTextView_top_news);
        timeTopNews = view.findViewById(R.id.timeTextView_top_news);
        notificationBtn = view.findViewById(R.id.notification_btn);
        cardView = view.findViewById(R.id.top_news);

        // Initialize RequestQueue for making API requests
        requestQueue = Volley.newRequestQueue(getActivity());

        // Initialize SpinKit animation for loading
        Sprite doubleBounce = new DoubleBounce();
        spinKitView.setIndeterminateDrawable(doubleBounce);

        // Initialize empty ArrayList for storing articles
        arrayList = new ArrayList<Article>();

        // Show loading animation if it's the first load
        if (isFirstLoad) {
            spinKitView.setVisibility(View.VISIBLE);
        }

        // Click listener for notification button to navigate to Settings screen
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Settings.class));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Show loading indicator if data is already loaded
        if (isDataLoaded) {
            spinKitView.setVisibility(View.VISIBLE);
        }

        // Fetch data from API and load into RecyclerView
        getData();

        // Set up SwipeRefreshLayout for refreshing data
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("functionTest", "refresh");
                if (!arrayList.isEmpty()) {
                    arrayList.clear();
                    arrayList = new ArrayList<>();
                }
                isnew = true;
                getData();// Refresh data from API
                // Delay for one second before stopping refresh animation
                Handler handler = new Handler();
                final Runnable r = new Runnable() {
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                };

                handler.postDelayed(r, 1000);
            }
        });



        // Click listener for topNews image, opens DetailsActivity
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("ARTICLE", topNews);
                startActivity(intent);

            }
        });

    }


    // Method to fetch data from API
    private void getData() {
        // Check if the device is connected to the internet
        if (NetworkUtils.isNetworkAvailable(getActivity())) {
            // If connected to the internet, proceed with the API request
        } else {
            // If not connected to the internet, display a toast message
            Toast.makeText(getActivity(), "No Internet connection...", Toast.LENGTH_SHORT).show();
        }

        // Instantiate a NewsApiClient object with the API key
        NewsApiClient newsApiClient = new NewsApiClient("ac525019c81a43fabb8e3c2cbda45225");

        // Make an API request to fetch everything related to "Climate Change"
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("Climate Change") // Set the query parameter
                        .build(), // Build the request
                new NewsApiClient.ArticlesResponseCallback() { // Define a callback for handling response
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        // If the API request is successful, handle the response

                        // Log the response for debugging purposes
                        Log.d("TrendingResponse", response.toString());

                        // Set the data loading flag to true
                        isDataLoaded = true;

                        // If it's the first load, hide the loading animation
                        if (isFirstLoad) {
                            spinKitView.setVisibility(View.GONE);
                        }

                        // Store the articles retrieved from the response in the array list
                        arrayList = response.getArticles();

                        // Set the adapter for the recycler view to display the articles
                        setAdapter();

                        // Load data for the top news
                        cardView.setVisibility(View.VISIBLE);
                        topNewsImage.setVisibility(View.VISIBLE);
                        // Check if the URL to the top news image is available
                        if (topNews.getUrlToImage() != null && !topNews.getUrlToImage().isEmpty()) {

                            // If the image URL is available, load the image using Glide
                            Glide
                                    .with(getContext())
                                    .load(topNews.getUrlToImage())
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                                            // If image loading fails, set a default image and hide the loading indicator
                                            spinKitTopNews.setVisibility(View.GONE);
                                            topNewsImage.setImageDrawable(getContext().getDrawable(R.drawable.cloudy));
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                            // If image loading is successful, hide the loading indicator
                                            spinKitTopNews.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .centerCrop()
                                    .placeholder(R.drawable.cloudy)
                                    .into(topNewsImage);

                            // Make the top news card view and image view visible

                        } else {
                            // If the image URL is not available, hide the loading indicator and set a default image
                            spinKitTopNews.setVisibility(View.GONE);
                            topNewsImage.setImageDrawable(getContext().getDrawable(R.drawable.cloudy));
                        }

                        // Set the bookmark button image based on whether the article is bookmarked or not
                        bookmarkButtonTopNews.setImageResource(topNews.isBookmarked() ? R.drawable.ic_baseline_bookmark_24 : R.drawable.baseline_bookmark_black);

                        // Set the title, description, author, and time of the top news article
                        titleTopNews.setText(topNews.getTitle());
                        descriptionTopNews.setText(topNews.getDescription());
                        authorTopNews.setText(topNews.getSource().getName());
                        timeTopNews.setText(formatDate(topNews.getPublishedAt()));

                        // Remove the top news article from the list of articles
                        response.getArticles().remove(0);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // If the API request fails, set the data loading flag to false
                        isDataLoaded = false;

                        // Hide the loading animation
                        spinKitView.setVisibility(View.GONE);

                        // Print the error message
                        System.out.println(throwable.getMessage());
                    }
                }
        );
    }
    //The date come in api as standard format below method is used to convert the date as dd MMMM yyyy 'at' HH:mm

    // Method to format a date string from API response into a human-readable format
    public static String formatDate(String dateString) {
        // Define input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm", Locale.getDefault());

        try {
            // Parse the input date string into a Date object using the input format
            Date date = inputFormat.parse(dateString);
            // Format the Date object into a string using the output format
            return outputFormat.format(date);
        } catch (ParseException e) {
            // If parsing fails, print the stack trace and return an empty string
            e.printStackTrace();
            return "";
        }
    }

    // Method called when the bottom navigation is clicked to refresh the data
    @Override
    public void onBottomNavClick() {
        Log.d("functionTest", "called");
        // Show the refresh indicator
        swipeRefreshLayout.setRefreshing(true);
        // Clear the existing data if any
        if (!arrayList.isEmpty()) {
            arrayList.clear();
            arrayList = new ArrayList<>();
            Log.d("functionTest", arrayList.size() + " size");
        }
        // Set the flag to indicate new data loading
        isnew = true;
        // Fetch data from the API
        getData();

        // Define a delayed task to stop the refresh indicator after a certain time
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        // Post the delayed task with a delay of 1 second
        handler.postDelayed(r, 1000);
    }

    // Empty method stub required by the interface Listener
    @Override
    public void LoadData(boolean clearList) {
        // This method does nothing in this implementation
    }

    // Method called when the fragment resumes to update the RecyclerView with new data
    @Override
    public void onResume() {
        // Log a message indicating that the onResume method is called
        Log.d("thisssss", "onResumeView:Run ");
        // Call the superclass onResume method
        super.onResume();
        // Update the RecyclerView adapter to reflect any changes in data
        setAdapter();
    }

    // Method to set the adapter on the RecyclerView and display the articles
    private void setAdapter() {
        // Sort the list of articles based on their published date in descending order
        Collections.sort(arrayList, new Comparator<Article>() {
            @Override
            public int compare(Article article1, Article article2) {
                return article2.getPublishedAt().compareTo(article1.getPublishedAt());
            }
        });

        // Retrieve the most recent article (first one after sorting)
        if (!arrayList.isEmpty()) {
            topNews = arrayList.get(0);
            // Now topNews contains the most recent article
        } else {
            // Handle the case where the arrayList is empty
        }

        // Create a new instance of the NewsAdapter with the sorted list of articles
        adapter = new NewsAdapter(getContext(), arrayList);
        // Set the adapter on the RecyclerView
        recyclerView.setAdapter(adapter);
        // Disable nested scrolling for better performance
        recyclerView.setNestedScrollingEnabled(false);
        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
        // Set the layout manager for the RecyclerView (2 columns grid layout)
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

}