package com.example.fot_news;

import android.os.Bundle;
import android.view.View;       // Import for View.OnClickListener
import android.widget.ImageView; // Import for ImageView

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Based on tools:context in activity_developer.xml, this activity is referred to as ProfileActivity.
// While your file is named 'developer.java', and the class 'developer',
// for consistency, you might consider renaming it to ProfileActivity.java.
// For now, the code below assumes 'developer' as the class name.
public class developer extends AppCompatActivity {

    private ImageView backArrow; // Declare the ImageView for the back arrow

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_developer); // Loads the activity_developer.xml layout

        // Apply padding to the root layout to avoid system bars overlapping content.
        // R.id.activity_developer is the ID of your root LinearLayout in activity_developer.xml.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_developer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the back_arrow ImageView
        backArrow = findViewById(R.id.back_arrow);

        // Set an OnClickListener for the back arrow
        if (backArrow != null) {
            backArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // This will close the current 'developer' activity
                    // and return to the activity that launched it (the 'menu' page).
                    finish();
                }
            });
        }
        // You would add any other initialization for EditText fields or other UI elements
        // from activity_developer.xml here if you want them to be interactive.
        // Example:
        // EditText studentIdEditText = findViewById(R.id.edit_text_student_id);
        // ...
    }
}