package com.example.fot_news.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fot_news.R;
import com.example.fot_news.model.Article;
import com.bumptech.glide.Glide; // Use Glide for loading images if you're using URLs

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private FragmentManager fragmentManager;

    // Constructor for passing data (articles and fragment manager)
    public ArticleAdapter(List<Article> articles, FragmentManager fragmentManager) {
        this.articles = articles;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = articles.get(position);

        // Set article title, description, and time
        holder.title.setText(article.getTitle());
        holder.description.setText(article.getDescription());

        // Load the image using Glide (if image is a URL)
        String imageUrl = article.getImage();  // Assuming this is a URL or a drawable resource name
        if (imageUrl != null) {
            // If it's a URL, use Glide to load the image
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .into(holder.imageView);
        } else {
            // If it's a drawable resource name, use the resource ID
            int imageResId = holder.itemView.getContext().getResources().getIdentifier(imageUrl, "drawable", holder.itemView.getContext().getPackageName());
            holder.imageView.setImageResource(imageResId);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder {
        // Define views for article data
        TextView title, description, timeAgoTextView;
        ImageView imageView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            // Initialize views from item_article.xml (correct the IDs based on your XML layout)
            title = itemView.findViewById(R.id.article_summary_title);  // Corrected ID for article title
            description = itemView.findViewById(R.id.article_summary_description);  // Corrected ID for article description
            timeAgoTextView = itemView.findViewById(R.id.article_summary_time_ago); // Corrected ID for article time
            imageView = itemView.findViewById(R.id.article_summary_image);  // Corrected ID for article image
        }
    }
}
