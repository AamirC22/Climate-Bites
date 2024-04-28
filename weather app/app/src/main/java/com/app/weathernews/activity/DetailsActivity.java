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

    // This is the implementation of the different UI Elements
    SpinKitView spinKitView;
    WebView webView;
    long pressedTime;
    boolean isFirstLoad = true;
    private ImageView imageView;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView bookmarkButton;
    private ImageView backButton;

    // This is the data model that ive chosen to use
    Article newsModel;
    String webUrl;

    // This is an array for all the articles that are Bookmarked
    private ArrayList<Article> bookMarked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // This initialises all the UI Elements into the Application
        bookmarkButton = findViewById(R.id.bookmarkButton);
        backButton = findViewById(R.id.backbutton);
        newsModel = new Article();

        // This retrieves bookmarked articles from the bookmarked Array
        bookMarked = Paper.book().read("bookmarked_items", new ArrayList<>());

        // Retrieving article details passed from previous activity
        Intent intent = getIntent();
        Article article = (Article) intent.getSerializableExtra("ARTICLE");
        if (article != null) {
            webUrl = article.getUrl();
        }

        // This checks if an article is bookmarked or not, and if not the appearance changes
        if (bookMarked.contains(article)) {
            bookmarkButton.setImageResource(R.drawable.bookmark_white);
        } else {
            bookmarkButton.setImageResource(R.drawable.baseline_bookmark_border_24);
        }

        // This method checks if the back button is clicked or not
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // This method checks if the bookmark is clicked or not and changes the appearance
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
                // This updates the data to the database that is local
                Paper.book().write("bookmarked_items", bookMarked);
            }
        });

        // This initialises the Views for the loading indicator for the application
        // It then loads the Sprites
        spinKitView = findViewById(R.id.spin_kit);
        webView = findViewById(R.id.webview);
        Sprite doubleBounce = new DoubleBounce();
        spinKitView.setIndeterminateDrawable(doubleBounce);
        webView.getSettings().setJavaScriptEnabled(true);

        // This handles events that involve page loading by utilising WebView Client
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
                // This checks if the network url is the same as the string url
                if (URLUtil.isNetworkUrl(url)) {
                    return false;
                }
                // This ensures that the app is installed and if not, no activities occur
                if (appInstalledOrNot(url)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {

                }
                return true;
            }
        });

        // Loads the WEB Url
        webView.loadUrl(webUrl);
    }

    // This checks if the app is installed and if not, false is returned
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // This means that the app is not installed
        }
        return false;
    }
}
