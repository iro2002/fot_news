package com.example.fot_news.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.fot_news.R;

public class FragmentArticleDetail extends Fragment {

    private TextView detailTitle;
    private TextView timeAgo;
    private TextView detailContent;
    private ImageView detailImage;

    public FragmentArticleDetail() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_article_detail, container, false);

        detailTitle = view.findViewById(R.id.detailTitle);
        timeAgo = view.findViewById(R.id.timeAgo);
        detailContent = view.findViewById(R.id.detailContent);
        detailImage = view.findViewById(R.id.detailImage);

        // Retrieve data from arguments
        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("title");
            String content = args.getString("content");
            String time = args.getString("timeAgo");
            int imageUrl = args.getInt("imageUrl", 0); // Default to 0 if not found

            detailTitle.setText(title);
            detailContent.setText(content);
            timeAgo.setText(time);
            if (imageUrl != 0) {
                detailImage.setImageResource(imageUrl);
            }
        } else {
            // Fallback for direct testing or if no arguments are passed
            detailTitle.setText("Interfaculty Championship 2025");
            detailContent.setText("The stadium is alive, the crowd is roaring, and our athletes are ready to break limits!");
            timeAgo.setText("9h ago");
            detailImage.setImageResource(R.drawable.inter_faculty_championship);
        }

        return view;
    }
}