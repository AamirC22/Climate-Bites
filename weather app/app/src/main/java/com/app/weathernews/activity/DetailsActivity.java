package com.app.weathernews.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.weathernews.R;
import com.app.weathernews.models.Article;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.paperdb.Paper;

public class DetailsActivity extends AppCompatActivity {

    // UI Elements
    SpinKitView spinKitView;
    WebView webView;
    long pressedTime;
    boolean isFirstLoad = true;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView bookmarkButton;
    private ImageView backButton;

    // Data Model
    Article newsModel;

    // Web URL
    String webUrl;

    // Bookmarked items
    private ArrayList<Article> bookMarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initializing UI Elements
        bookmarkButton = findViewById(R.id.bookmarkButton);
        backButton = findViewById(R.id.backbutton);

        // Initializing Data Model
        newsModel = new Article();

        // Retrieving bookmarked items from local database
        bookMarked = Paper.book().read("bookmarked_items", new ArrayList<>());

        // Retrieving article details passed from previous activity
        Intent intent = getIntent();
        Article article = (Article) intent.getSerializableExtra("ARTICLE");
        if (article != null) {
            webUrl = article.getUrl();
        }

        // Checking if the article is bookmarked or not
        if (bookMarked.contains(article)) {
            bookmarkButton.setImageResource(R.drawable.bookmark_white);
        } else {
            bookmarkButton.setImageResource(R.drawable.baseline_bookmark_border_24);
        }

        // Back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Bookmark button click listener
        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookMarked.contains(article)) {
                    bookMarked.remove(article);
                    bookmarkButton.setImageResource(R.drawable.baseline_bookmark_border_24);
                } else {
                    bookMarked.add(article);
                    bookmarkButton.setImageResource(R.drawable.bookmark_white);
                }
                // Writing updated data to local database
                Paper.book().write("bookmarked_items", bookMarked);
            }
        });

        // Initializing WebView and SpinKitView for loading indicator
        spinKitView = findViewById(R.id.spin_kit);
        webView = findViewById(R.id.webview);
        Sprite doubleBounce = new DoubleBounce();
        spinKitView.setIndeterminateDrawable(doubleBounce);
        webView.getSettings().setJavaScriptEnabled(true);

        // WebView client for handling page loading events
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (isFirstLoad) {
                    spinKitView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isFirstLoad) {
                    spinKitView.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    isFirstLoad = false;
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Check if URL is network URL
                if (URLUtil.isNetworkUrl(url)) {
                    return false;
                }
                // Check if the app is installed
                if (appInstalledOrNot(url)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    // Do something if app is not installed
                }
                return true;
            }
        });

        // Load the web URL
        webView.loadUrl(webUrl);
    }

    // Method to check if the app is installed or not based on the URI
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // App not found
        }
        return false;
    }
}
