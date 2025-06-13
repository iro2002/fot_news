package com.example.fot_news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.fot_news.fragments.FragmentAcademic;
import com.example.fot_news.fragments.FragmentEvent;
import com.example.fot_news.fragments.FragmentSport;
import com.example.fot_news.fragments.FragmentTopBar;

public class news extends AppCompatActivity {

    private LinearLayout sportNavItem, academicNavItem, eventNavItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        sportNavItem = findViewById(R.id.nav_sport_item);
        academicNavItem = findViewById(R.id.nav_academic_item);
        eventNavItem = findViewById(R.id.nav_event_item);

        // Set up listeners for fragment navigation
        sportNavItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentSport());
            }
        });

        academicNavItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentAcademic());
            }
        });

        eventNavItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentEvent());
            }
        });

        // Initially load the TopBarFragment
        loadTopBarFragment();

        // Load the first fragment (e.g., SportFragment) initially
        loadFragment(new FragmentSport());
    }

    private void loadTopBarFragment() {
        FragmentTopBar topBarFragment = new FragmentTopBar();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.topBarContainer, topBarFragment);
        transaction.commit();
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);  // Optional: Allows the user to go back to previous fragment
        transaction.commit();
    }
}
