package com.example.fot_news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class menu extends AppCompatActivity {

    private static final String TAG = "menu"; // Tag for logging

    private ImageView backArrow;
    private ImageView profileImage;
    private TextView userNameTextView;
    private TextView viewProfileTextView;
    private ImageView arrowViewProfile;
    private ConstraintLayout itemAccount, itemNotification, itemPrivacy, itemSupport, itemAboutUs;
    private MaterialButton logoutButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- Initialize UI elements ---
        backArrow = findViewById(R.id.back_arrow);
        profileImage = findViewById(R.id.profile_image);
        userNameTextView = findViewById(R.id.user_name);
        viewProfileTextView = findViewById(R.id.view_profile_text);
        arrowViewProfile = findViewById(R.id.arrow_view_profile);
        itemAccount = findViewById(R.id.item_account);
        itemNotification = findViewById(R.id.item_notification);
        itemPrivacy = findViewById(R.id.item_privacy);
        itemSupport = findViewById(R.id.item_support);
        itemAboutUs = findViewById(R.id.item_about_us); // Initialize item_about_us
        logoutButton = findViewById(R.id.logout_button);

        // --- Set up Click Listeners ---

        // Back Arrow: Navigates back to the previous activity (likely news feed)
        if (backArrow != null) {
            backArrow.setOnClickListener(v -> onBackPressed());
        } else {
            Log.w(TAG, "back_arrow ImageView not found in activity_menu.xml");
        }

        // Profile Card Clicks (View Profile Text, Arrow, and User Name TextView)
        // These all lead to the 'user' activity (your profile page).
        View.OnClickListener profileClickListener = v -> {
            Toast.makeText(menu.this, "Navigating to Profile Details!", Toast.LENGTH_SHORT).show();
            Intent profileIntent = new Intent(menu.this, user.class); // Navigates to user.java
            startActivity(profileIntent);
        };

        if (viewProfileTextView != null) viewProfileTextView.setOnClickListener(profileClickListener);
        if (arrowViewProfile != null) arrowViewProfile.setOnClickListener(profileClickListener);
        if (userNameTextView != null) userNameTextView.setOnClickListener(profileClickListener);


        // Individual Menu Item Click Listeners
        if (itemAccount != null) {
            itemAccount.setOnClickListener(v -> {
                Toast.makeText(menu.this, "Account Settings Clicked!", Toast.LENGTH_SHORT).show();
                Intent accountIntent = new Intent(menu.this, user.class); // Assuming Account settings is also your user.java
                startActivity(accountIntent);
            });
        }
        if (itemNotification != null) {
            itemNotification.setOnClickListener(v -> {
                Toast.makeText(menu.this, "Notification Settings Clicked!", Toast.LENGTH_SHORT).show();
                // TODO: Implement navigation to Notification Settings Activity (create a new Activity for this)
            });
        }
        if (itemPrivacy != null) {
            itemPrivacy.setOnClickListener(v -> {
                Toast.makeText(menu.this, "Privacy Settings Clicked!", Toast.LENGTH_SHORT).show();
                // TODO: Implement navigation to Privacy Settings Activity (create a new Activity for this)
            });
        }
        if (itemSupport != null) {
            itemSupport.setOnClickListener(v -> {
                Toast.makeText(menu.this, "Support Clicked!", Toast.LENGTH_SHORT).show();
                // TODO: Implement navigation to Support/FAQ Activity (create a new Activity for this)
            });
        }

        // --- NEW: item_about_us now navigates to the Developer ---
        if (itemAboutUs != null) {
            itemAboutUs.setOnClickListener(v -> {
                // Toast.makeText(menu.this, "About Us Clicked!", Toast.LENGTH_SHORT).show(); // Optional toast
                Intent aboutUsIntent = new Intent(menu.this, developer.class); // Navigate to ProfileActivity.java
                startActivity(aboutUsIntent);
            });
        } else {
            Log.w(TAG, "item_about_us ConstraintLayout not found in activity_menu.xml");
        }

        // Logout Button
        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> logoutUser());
        } else {
            Log.w(TAG, "logout_button MaterialButton not found in activity_menu.xml");
        }

        // --- Load User Data when the activity is created ---
        loadUserData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String displayName = currentUser.getDisplayName();
            if (userNameTextView != null) {
                if (displayName != null && !displayName.isEmpty()) {
                    userNameTextView.setText(displayName);
                } else {
                    userNameTextView.setText(currentUser.getEmail() != null ? currentUser.getEmail() : "User Name");
                }
            }
            if (profileImage != null) {
                profileImage.setImageResource(R.drawable.ic_profile_placeholder);
            }
        } else {
            Log.e(TAG, "User is not logged in when menu is accessed. Redirecting to login.");
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(menu.this, login.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
            finish();
        }
    }

    private void logoutUser() {
        mAuth.signOut();
        Toast.makeText(menu.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(menu.this, login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}