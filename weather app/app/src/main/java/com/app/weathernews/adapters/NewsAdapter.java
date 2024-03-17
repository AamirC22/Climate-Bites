package com.app.weathernews.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.weathernews.R;
import com.app.weathernews.activity.DetailsActivity;
import com.app.weathernews.models.Article;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.SpinKitView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    // Context reference for the adapter
    private Context context;
    // List of articles to display
    private List<Article> data;
    // List to store bookmarked articles
    private ArrayList<Article> bookMarked;

    // Constructor for the adapter
    public NewsAdapter(Context context, List<Article> data) {
        this.context = context;
        this.data = data;
        // Initialize Paper for local storage
        Paper.init(context);
        // Read bookmarked articles from local storage
        this.bookMarked = Paper.book().read("bookmarked_items", new ArrayList<>());
        // Update bookmark status for each article
        for (Article article : data) {
            article.setBookmarked(bookMarked.contains(article));
        }
    }

    // ViewHolder class for caching views in a list item
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView, authorTextView, timeTextView;
        ImageButton bookmarkButton;
        SpinKitView progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize UI elements
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            bookmarkButton = itemView.findViewById(R.id.bookmarkButton);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            progressBar = itemView.findViewById(R.id.image_progress);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create ViewHolder instance
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the article at the current position
        Article article = data.get(position);
        // Show progress bar while loading image
        holder.progressBar.setVisibility(View.VISIBLE);

        // Load article image using Glide library
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            Glide.with(context).load(article.getUrlToImage()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                    // Set default image if loading fails
                    holder.imageView.setImageDrawable(context.getDrawable(R.drawable.cloudy));
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, @NonNull Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                    // Hide progress bar when image is loaded successfully
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    return false;
                }
            }).centerCrop().placeholder(R.drawable.cloudy).into(holder.imageView);
        } else {
            // Set default image if URL is empty
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.cloudy));
            holder.imageView.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        }

        // Set bookmark button icon based on bookmark status
        holder.bookmarkButton.setImageResource(article.isBookmarked() ? R.drawable.ic_baseline_bookmark_24 : R.drawable.baseline_bookmark_black);
        // Set article title, author, and published time
        holder.titleTextView.setText(getTitleLength(article.getTitle()));
        holder.authorTextView.setText(article.getSource().getName());
        holder.timeTextView.setText(formatDate(article.getPublishedAt()));

        // Handle bookmark button click
        holder.bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle bookmark status
                article.setBookmarked(!article.isBookmarked());
                // Update bookmark button icon
                holder.bookmarkButton.setImageResource(article.isBookmarked() ? R.drawable.ic_baseline_bookmark_24 : R.drawable.baseline_bookmark_black);
                // Add or remove article from bookmark list
                if (article.isBookmarked()) {
                    bookMarked.add(article);
                } else {
                    bookMarked.remove(article);
                }
                // Store updated bookmark list in local storage
                Paper.book().write("bookmarked_items", bookMarked);
            }
        });

        // Handle item click to open details activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("ARTICLE", article);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the data set
        return data.size();
    }

    // Utility method to format date string
// Utility method to format the date string received from the API into a more readable format
    public static String formatDate(String dateString) {
        // Define the input and output date formats
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm", Locale.getDefault());

        try {
            // Parse the input date string into a Date object using the input format
            Date date = inputFormat.parse(dateString);
            // Format the parsed date using the output format and return the formatted date as a string
            return outputFormat.format(date);
        } catch (ParseException e) {
            // If parsing fails (e.g., due to an invalid date format), print the stack trace and return an empty string
            e.printStackTrace();
            return "";
        }
    }

    // Utility method to limit the length of article title
// Utility method to limit the length of the article title to a maximum of 4 words
    private String getTitleLength(String title) {
        // Split the full title into individual words
        String[] words = title.split(" ");
        // Create a StringBuilder to store the shortened title
        StringBuilder shortenedText = new StringBuilder();
        // Iterate over the words, appending them to the shortened title until reaching the maximum word limit (4)
        for (int i = 0; i < Math.min(4, words.length); i++) {
            shortenedText.append(words[i]).append(" ");
        }
        // Trim any extra whitespace and return the shortened title
        return shortenedText.toString().trim();
    }
}
