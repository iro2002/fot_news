package com.example.fot_news.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fot_news.R;
import com.example.fot_news.menu;  // Ensure the correct package is imported for menu.java
import com.example.fot_news.user;  // Ensure the correct package is imported for user.java

public class FragmentTopBar extends Fragment {

    private ImageView menuIconInSearch;
    private ImageView profileIcon;

    public FragmentTopBar() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_fragment_top_bar, container, false);

        // Initialize menuIconInSearch and set its click listener to navigate to menu.java
        menuIconInSearch = view.findViewById(R.id.menuIconInSearch);
        if (menuIconInSearch != null) {
            menuIconInSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to menu.java
                    Intent intentToMenu = new Intent(getActivity(), menu.class);
                    startActivity(intentToMenu);
                }
            });
        } else {
            android.util.Log.w("FragmentTopBar", "menuIconInSearch ImageView not found in activity_fragment_top_bar.xml");
        }

        // Initialize profileIcon and set its click listener to navigate to user.java
        profileIcon = view.findViewById(R.id.profileIcon);
        if (profileIcon != null) {
            profileIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Navigate to user.java
                    Intent intentToUser = new Intent(getActivity(), user.class);
                    startActivity(intentToUser);
                }
            });
        } else {
            android.util.Log.w("FragmentTopBar", "profileIcon ImageView not found in activity_fragment_top_bar.xml");
        }

        return view;
    }
}
