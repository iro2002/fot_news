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

public class FragmentAcademic extends Fragment {

    private RecyclerView recyclerViewAcademic;
    private ArticleAdapter articleAdapter;
    private List<Article> academicArticles;
    private DatabaseReference mDatabase;

    public FragmentAcademic() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View view = inflater.inflate(R.layout.activity_fragment_academic, container, false);

        recyclerViewAcademic = view.findViewById(R.id.recyclerViewAcademic);
        recyclerViewAcademic.setLayoutManager(new LinearLayoutManager(getContext()));

        academicArticles = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("categories/academic");

        // Firebase Database Listener
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                academicArticles.clear(); // Clear any existing articles
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class); // Convert Firebase data to Article object
                    if (article != null) {
                        academicArticles.add(article); // Add article to list
                    }
                }

                // Initialize and set the adapter
                articleAdapter = new ArticleAdapter(academicArticles, getParentFragmentManager());
                recyclerViewAcademic.setAdapter(articleAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        });

        return view;
    }
}
