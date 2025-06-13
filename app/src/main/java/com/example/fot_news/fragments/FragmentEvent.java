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

public class FragmentEvent extends Fragment {

    private RecyclerView recyclerViewEvents;
    private ArticleAdapter articleAdapter;
    private List<Article> eventArticles;
    private DatabaseReference mDatabase;

    public FragmentEvent() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View view = inflater.inflate(R.layout.activity_fragment_event, container, false);

        recyclerViewEvents = view.findViewById(R.id.recyclerViewEvents);
        recyclerViewEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        eventArticles = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("categories/event");

        // Firebase Database Listener
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventArticles.clear(); // Clear any existing articles
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class); // Convert Firebase data to Article object
                    if (article != null) {
                        eventArticles.add(article); // Add article to list
                    }
                }

                // Initialize and set the adapter
                articleAdapter = new ArticleAdapter(eventArticles, getParentFragmentManager());
                recyclerViewEvents.setAdapter(articleAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors from the Firebase database
            }
        });

        return view;
    }
}
