package com.app.weathernews.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.weathernews.NewsApiClient;
import com.app.weathernews.R;
import com.app.weathernews.activity.DetailsActivity;
import com.app.weathernews.activity.Listener;
import com.app.weathernews.activity.Settings;
import com.app.weathernews.adapters.NewsAdapter;
import com.app.weathernews.models.Article;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Fragment that is needed to display the home view of the news application,
 * shows the news in a tabloid format and has top headline as a dedicated card
 * shows a list of articles below it.
 */

public class HomeFragment extends Fragment implements Listener {

    // Declaration of class variables

    // List to store article items
    private List<Article> arrayList; // Holds an Array List for the articles displayed in RecyclerView
    SpinKitView spinKitView; // Loading animations using SpinKitViews
    SpinKitView spinKitTopNews; // Loading animation for the Top Headline card
    boolean isFirstLoad = true; // Checks if data is loaded for the first time or not
    boolean isDataLoaded = false; // Checks if the data is loaded already or not
    private RequestQueue requestQueue;
    RecyclerView recyclerView; // Displays articles
    CardView cardView; // The card for the top news headline
    ImageView notificationBtn; // Adds Notification button to the interface
    private ImageView topNewsImage, bookmarkButtonTopNews; // Sets images for UI
    private TextView titleTopNews, descriptionTopNews, authorTopNews, timeTopNews; // Sets Texts for UI
    Article topNews; // Stores top news article in a Article model

    // Adapter to set items in RecyclerView
    NewsAdapter adapter;

    // SwipeRefreshLayout for refreshing data
    private SwipeRefreshLayout swipeRefreshLayout;

    // Boolean to track if new data is loaded or not, initially false
    boolean checkIfNew = false;

    // Constructor for HomeFragment
    public HomeFragment() {
    }

    //  Creates a new instance of Home Fragment as a static object
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    // Used when a fragment is created and initialises different components of the fragment
    // Context is not needed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Lifecycle method called to create the fragment's view hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // This inflates the layout of the fragment for the UI
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d("TAGsgsg", "onCreateView: ");
        // Initialize RequestQueue for making API requests and for network requests
        requestQueue = Volley.newRequestQueue(getActivity());

        // Initialize RecyclerView and SpinKitViews
        recyclerView = view.findViewById(R.id.topicRv);
        spinKitView = view.findViewById(R.id.spin_kit);
        spinKitTopNews = view.findViewById(R.id.spin_kit_main_image);

        // Initialize the top news headline's UI elements.
        topNews = new Article();

        // Find UI elements by their IDs for the top news headline
        topNewsImage = view.findViewById(R.id.imageView_top);
        bookmarkButtonTopNews = view.findViewById(R.id.bookmarkButton_top_news);
        titleTopNews = view.findViewById(R.id.titleTextView_top_news);
        descriptionTopNews = view.findViewById(R.id.descriptionTextView_top_news);
        authorTopNews = view.findViewById(R.id.authorTextView_top_news);
        timeTopNews = view.findViewById(R.id.timeTextView_top_news);
        notificationBtn = view.findViewById(R.id.notification_btn);
        cardView = view.findViewById(R.id.top_news);



        // Initialize SpinKit animation for loading while headline loads
        Sprite doubleBounce = new DoubleBounce();
        spinKitView.setIndeterminateDrawable(doubleBounce);

        // Creates empty arraylist for storing articles
        arrayList = new ArrayList<Article>();

        // If its the first time the headline is loading, it sets the loading animation
        if (isFirstLoad) {
            spinKitView.setVisibility(View.VISIBLE);
        }
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Settings.class));
            }
        });

        return view;
    }


    // Called after onCreateView, no saved state restored yet.
    // Binds components to the data and full initialises the View Hierarchy.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // If data is already loaded, then hide the loading animation as its already loaded
        if (isDataLoaded) {
            spinKitView.setVisibility(View.GONE);
        }
       getData();
        // Initialises the swipe movements for refreshing data on swipe gestures.
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
                checkIfNew = true;
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



        // OnClickListener needed for top headline card, navigates to details view of the article
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra("ARTICLE", topNews);
                startActivity(intent);

            }
        });

    }


    // Fetches data from NewsAPI
    private void getData() {


        // Instantiate a NewsApiClient object with the API key
        NewsApiClient newsApiClient = new NewsApiClient("ac525019c81a43fabb8e3c2cbda45225");

        // This makes a request to get every article involving the query keyword of "Climate Change"
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("Climate Change") // Set the query parameter
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() { // Define a callback for handling response in case of an error while retrieving
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (!isAdded()) {
                            return;
                        }
                        Log.d("TrendingResponse", response.toString());
                        arrayList.clear();
                        isDataLoaded = true; // Sets true as data has loaded
                        if (isFirstLoad) {
                            spinKitView.setVisibility(View.GONE);
                        }

                        // Stores the articles retrieved from the previous method into the array list previously initialised
                        arrayList = response.getArticles();

                        Log.d("arraylist", "onSuccess: "+arrayList.toString());
                        // Displays article by setting the recycler view and updating it.
                        setAdapter();
                        cardView.setVisibility(View.VISIBLE);
                        topNewsImage.setVisibility(View.VISIBLE);
                        // Checks if the image has an url and if it is empty or not
                        if (topNews.getUrlToImage() != null && !topNews.getUrlToImage().isEmpty()) {

                            // Use glide to load the image using the URL if it is present
                            Glide.with(requireActivity())
                                    .load(topNews.getUrlToImage())
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                                            // Sets a default image if the image has no URL or if it is null
                                            spinKitTopNews.setVisibility(View.GONE);
                                            topNewsImage.setImageDrawable(getContext().getDrawable(R.drawable.cloudy));
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                            spinKitTopNews.setVisibility(View.GONE);
                                            return false;
                                        }
                                    })
                                    .centerCrop()
                                    .placeholder(R.drawable.cloudy)
                                    .into(topNewsImage);

                        } else {
                            spinKitTopNews.setVisibility(View.GONE);
                            topNewsImage.setImageDrawable(getContext().getDrawable(R.drawable.cloudy));
                        }

                        // Changes the appearance of the bookmark icon depending on if the article is bookmarked or not
                        bookmarkButtonTopNews.setImageResource(topNews.isBookmarked() ? R.drawable.ic_baseline_bookmark_24 : R.drawable.baseline_bookmark_black);

                        // This sets the different details of the Top Headline article
                        titleTopNews.setText(topNews.getTitle()); // sets title
                        descriptionTopNews.setText(topNews.getDescription()); // sets description
                        authorTopNews.setText(topNews.getSource().getName());
                        timeTopNews.setText(formatDate(topNews.getPublishedAt()));

                        // Remove the top news article from the list of articles to be put below
                        response.getArticles().remove(0);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d("TAGdfgsdf", "onFailure: "+throwable);
                        // Changes the if data loaded to false if there is a failure in retrieval or data is not loaded
                        isDataLoaded = false;
                        spinKitView.setVisibility(View.GONE);

                        // Print the error message in order to be visible for debugging
                        System.out.println(throwable.getMessage());
                    }
                }
        );
    }

    //The date come in api as standard format below method is used to convert the date as dd MMMM yyyy 'at' HH:mm

    // Method to format a date string from API response into an easier readable format
    public static String formatDate(String dateString) {
        // Define input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm", Locale.getDefault());

        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            // If parsing fails, print the stack trace and return an empty string
            e.printStackTrace();
            return "";
        }
    }

    // Refreshes the data and is called when the bottom navigation is clicked
    // Clears existing data and fetches new data from API
    @Override
    public void onBottomNavClick() {
        Log.d("functionTest", "called");
        swipeRefreshLayout.setRefreshing(true);
        // Clear the existing data if any
        if (!arrayList.isEmpty()) {
            arrayList.clear();
            arrayList = new ArrayList<>();
            Log.d("functionTest", arrayList.size() + " size");
        }
        // Set the flag to indicate new data loading
        checkIfNew = true;
        // Fetch data from the API
        getData();
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        };

        // Post the delayed task with a delay of 1 second
        handler.postDelayed(r, 1000);
    }

    //This is an empty method needed by the Listener Interface
    @Override
    public void LoadData(boolean clearList) {

    }

    // The method that is called when the fragment updates the recyclerview with new data
    // that has been retrieved from NewsAPI
    @Override
    public void onResume() {
        super.onResume();
        getData();
        setAdapter();
    }

    // Method to set the adapter on the RecyclerView and display the articles
    private void setAdapter() {
         //Sort the list of articles based on their published date in descending order
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
        }  // Handle the case where the arrayList is empty


        if (isAdded()) {
            // Fragment is attached to an activity, so it's safe to access context
            adapter = new NewsAdapter(requireContext(), arrayList);
            // Set the adapter on the RecyclerView
            recyclerView.setAdapter(adapter);
        }

        /*
         Create a new instance of the NewsAdapter with the sorted list of articles
         Disable nested scrolling for better performance
        */

        recyclerView.setNestedScrollingEnabled(false);
        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
        // Set the layout manager for the RecyclerView (2 columns grid layout)
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }


}