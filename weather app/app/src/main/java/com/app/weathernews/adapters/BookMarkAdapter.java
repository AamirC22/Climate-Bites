package com.app.weathernews.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.app.weathernews.fragment.BookmarkFragment;
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

/**
 * This is an adapter class for Bookmarks that display the bookmarked news articles
 * in a recycler view, it interacts with Paper database to manage bookmarks.
 */
public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {

    // Context reference
    private Context context;

    // List of articles
    private List<Article> data;

    // List of bookmarked articles
    private ArrayList<Article> bookMarked;

    // Reference to the bookmark fragment
    private BookmarkFragment fragment;

    /**
     * Constructs the adapter using the provided context, list of articles, and a reference to the bookmark fragment.
     * Constructor for the adapter using list of articles, context, data and references to the fragment
     *
     * @param context
     * @param data List of articles that will be included to RecyclerView.
     * @param fragment Reference to the BookmarkFragment for UI updates and additions.
     */
    public BookMarkAdapter(Context context, List<Article> data, BookmarkFragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.data = data;
        Paper.init(context);
        this.bookMarked = Paper.book().read("bookmarked_items", new ArrayList<>());
        for (Article article : data) {
            article.setBookmarked(bookMarked.contains(article));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout of the RecyclerView and then returns the new view holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Load article data into the ViewHolder
        Article article = data.get(position);
        // Check if the article has a valid image URL and if not, handles error
        if(article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            // If the URL is not null , it loads image and the details of the article
            holder.progressBar.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(article.getUrlToImage())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                            // If image loading fails, set the placeholder image in place, hide the progress bar , then returns false
                            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.cloudy));
                            holder.imageView.setVisibility(View.VISIBLE);
                            holder.progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, @NonNull Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            holder.progressBar.setVisibility(View.GONE);
                            holder.imageView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .centerCrop()
                    .placeholder(R.drawable.cloudy)
                    .into(holder.imageView);
        } else {
            // If no valid image URL, show placeholder image
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.cloudy));
            holder.imageView.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
        }

// Set title, author, and time of the article
        holder.titleTextView.setText(getTitleLength(article.getTitle()));
        holder.authorTextView.setText(article.getSource().getName());
        holder.timeTextView.setText(formatDate(article.getPublishedAt()));

// Set bookmark button icon
        holder.bookmarkButton.setImageResource(R.drawable.ic_baseline_bookmark_24);

// Set click listener for the bookmark button
        holder.bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the article from the bookmarked list
                bookMarked.remove(article);
                // Update the local database
                Paper.book().write("bookmarked_items", bookMarked);
                // Mark the article as not bookmarked
                article.setBookmarked(false);
                // Update the adapter in the bookmark fragment
                fragment.updateAdapter(); // Notify the fragment that there is a change in the data set.
            }
        });

    }

    @Override
    public int getItemCount() {
        // This returns the number of articles that are in the data set
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView titleTextView, authorTextView, timeTextView;
        ImageButton bookmarkButton;
        SpinKitView progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // This initialises all the UI Elements using a superclass of ItemView
            imageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            bookmarkButton = itemView.findViewById(R.id.bookmarkButton);
            progressBar = itemView.findViewById(R.id.image_progress);

            // This sets an on click listener for item view
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Handle click on item view
            int currentPosition = getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                Article article = data.get(currentPosition);
                // Start DetailsActivity with article data
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("ARTICLE", article);
                intent.putExtra("BOOKMARKED", article.isBookmarked());
                context.startActivity(intent);
            }
        }
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
