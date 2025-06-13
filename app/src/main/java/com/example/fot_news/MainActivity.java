package com.example.fot_news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper; // Import Looper

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable edge-to-edge display
        setContentView(R.layout.activity_main); // Set the splash screen layout

        // Apply window insets for system bars
        // CORRECTED: Using the root ID from activity_main.xml -> android:id="@+id/main"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Use postDelayed to navigate after a delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, login.class);
            startActivity(intent);
            finish(); // Finish MainActivity so user can't go back to splash
        }, SPLASH_SCREEN_DELAY);
    }
}