package com.example.fot_news.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fot_news.R;
import com.example.fot_news.model.Article;
import com.example.fot_news.adapters.ArticleAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class FragmentSport extends Fragment {

    private RecyclerView recyclerViewSports;
    private ArticleAdapter articleAdapter;
    private List<Article> sportArticles;
    private DatabaseReference mDatabase;

    public FragmentSport() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_sport, container, false);

        recyclerViewSports = view.findViewById(R.id.recyclerViewSports);
        recyclerViewSports.setLayoutManager(new LinearLayoutManager(getContext()));

        sportArticles = new ArrayList<>();

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("categories/sport");

        // Fetch real-time data from Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sportArticles.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    sportArticles.add(article);
                }

                // Initialize and set the adapter
                articleAdapter = new ArticleAdapter(sportArticles, getParentFragmentManager());
                recyclerViewSports.setAdapter(articleAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any database errors
            }
        });

        return view;
    }
}
