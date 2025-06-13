package com.example.fot_news.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fot_news.R; // Make sure this R matches your package name

public class FragmentSignOut extends AppCompatActivity {

    // These are constants for sending results back to the calling activity (user.java)
    public static final String RESULT_SIGN_OUT = "sign_out_result";
    public static final int REQUEST_CODE_SIGN_OUT = 1001; // A unique request code for this activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to your dialog layout
        setContentView(R.layout.activity_fragment_sign_out);

        // Initialize UI elements from the layout
        TextView signOutPrompt = findViewById(R.id.textViewSignOutPrompt);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        Button buttonOk = findViewById(R.id.buttonOk);

        // Set up click listeners for the buttons
        if (buttonCancel != null) {
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an Intent to send the result back
                    Intent resultIntent = new Intent();
                    // Put extra data to indicate that the user cancelled the sign out
                    resultIntent.putExtra(RESULT_SIGN_OUT, false);
                    // Set the result code to CANCELED and pass the intent
                    setResult(Activity.RESULT_CANCELED, resultIntent);
                    finish(); // Close this dialog activity
                }
            });
        }

        if (buttonOk != null) {
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an Intent to send the result back
                    Intent resultIntent = new Intent();
                    // Put extra data to indicate that the user confirmed sign out
                    resultIntent.putExtra(RESULT_SIGN_OUT, true);
                    // Set the result code to OK and pass the intent
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish(); // Close this dialog activity
                }
            });
        }
    }
}