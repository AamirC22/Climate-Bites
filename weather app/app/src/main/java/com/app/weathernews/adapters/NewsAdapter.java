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

    // Creates a context reference fro the News adapter
    private Context context;
    // This displays the list of articles as Data
    private List<Article> data;
    // This creates an ArrayList to store the Bookmarked Articles
    private ArrayList<Article> bookMarked;

    // This acts as a Constructor method for the NewsAdapter ,and initialises the values
    public NewsAdapter(Context context, List<Article> data) {
        this.context = context;
        this.data = data;
        // Initialize Paper for local storage
        Paper.init(context);
        // Reads the  bookmarked articles from local storage
        this.bookMarked = Paper.book().read("bookmarked_items", new ArrayList<>());
        // This updates the bookmarked status for each article, depending
        for (Article article : data) {
            article.setBookmarked(bookMarked.contains(article));
        }
    }

    // his acts as a class that caches the different views to be able to be seen
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView; // Initialises the Image fields
        TextView titleTextView, authorTextView, timeTextView; // initialises the different text fields
        ImageButton bookmarkButton;
        SpinKitView progressBar; // The progress bar for when it is loading

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // This initialises all the UI Elements
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            bookmarkButton = itemView.findViewById(R.id.bookmarkButton);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            // This is done by finding the views by ID
            progressBar = itemView.findViewById(R.id.image_progress);
        }
    }


    /**
     * This inflates the view articles layout and creates the instance of ViewHolder, it is called by Recyclerview
     * to create new instances for every item involved
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates the new View holder instance and then inflates the item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_items, parent, false);
        // This returns a new instance of View holder that will also contain the new layout
        return new ViewHolder(view);
    }


    /**
     * onBindViewHolder is called by RecyclerView to display the data that is at the position I specified.
     * It updates the content of the item view based on the current position that is specified.
     *
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // This gets the article that is at the current int position
        Article article = data.get(position);
        // This shows the loading bar while images are loading
        holder.progressBar.setVisibility(View.VISIBLE);

        // Utilisation of Glide Library in order to load images
        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            Glide.with(context).load(article.getUrlToImage()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                    // If the image fails to load, a default image is put instead
                    holder.imageView.setImageDrawable(context.getDrawable(R.drawable.cloudy));
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, @NonNull Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                    // When the image has loaded, the Progress bar is then set to invisible
                    holder.progressBar.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    return false;
                }
            }).centerCrop().placeholder(R.drawable.cloudy).into(holder.imageView);
        } else {
            // Set default image if URL is empty and the article has no image
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.cloudy));
            holder.imageView.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        }

        // Set bookmark button icon based on bookmark status and if bookmarked or not
        holder.bookmarkButton.setImageResource(article.isBookmarked() ? R.drawable.ic_baseline_bookmark_24 : R.drawable.baseline_bookmark_black);
        // This sets the articles title, author and date published
        holder.titleTextView.setText(getTitleLength(article.getTitle()));
        holder.authorTextView.setText(article.getSource().getName());
        holder.timeTextView.setText(formatDate(article.getPublishedAt()));


        // This handles the events of when Bookmark buttons are clicked, fills it in if empty
        // or Makes it empty if it was already filled in
        holder.bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                article.setBookmarked(!article.isBookmarked());
                holder.bookmarkButton.setImageResource(article.isBookmarked() ? R.drawable.ic_baseline_bookmark_24 : R.drawable.baseline_bookmark_black);
                if (article.isBookmarked()) {
                    bookMarked.add(article);
                } else {
                    bookMarked.remove(article);
                }
                // Store updated bookmark list in the bookmark fragment in local storage
                Paper.book().write("bookmarked_items", bookMarked);
            }
        });

        // Handles the click of when articles are pressed and shows it on Details page
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
        // This returns the number of articles that are in the data set
        return data.size();
    }

    public static String formatDate(String dateString) {
        /**
         * Utility method to format the date string received from the API into a format that is more readable for the user
         * @param dateString
         * @return
         */
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm", Locale.getDefault());

        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    // This limits the length of article titles and then removes all the white spaces
    private String getTitleLength(String title) {
        // Split the full title into individual words
        String[] words = title.split(" ");
        // Create a StringBuilder to store the shortened title
        StringBuilder shortenedText = new StringBuilder();
        //  This shortens the title through a loop and appends until the max length if necessary
        for (int i = 0; i < Math.min(4, words.length); i++) {
            shortenedText.append(words[i]).append(" ");
        }
        // This returns a shortened title if it has been trimmed
        return shortenedText.toString().trim();
    }
}
