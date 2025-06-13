package com.example.fot_news;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Added import for Log
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private static final String TAG = "LOGIN_DEBUG";

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "onCreate: Login Activity started.");

        // IMPORTANT FIX: Correctly reference the root layout ID from activity_login.xml
        // The root ConstraintLayout in activity_login.xml has android:id="@+id/activity_login"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: FirebaseAuth initialized.");

        // Initialize UI elements
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);

        // Null check for UI elements (crucial for preventing crashes if IDs are mismatched)
        if (etEmail == null) Log.e(TAG, "etEmail (R.id.etEmail) is null! Check XML ID.");
        if (etPassword == null) Log.e(TAG, "etPassword (R.id.etPassword) is null! Check XML ID.");
        if (btnLogin == null) Log.e(TAG, "btnLogin (R.id.btnLogin) is null! Check XML ID.");
        if (tvSignUp == null) Log.e(TAG, "tvSignUp (R.id.tvSignUp) is null! Check XML ID.");

        Log.d(TAG, "onCreate: UI elements initialized.");

        // Set listeners only if elements are found
        if (tvSignUp != null) {
            tvSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "tvSignUp clicked. Navigating to RegisterActivity.");
                    Intent intent = new Intent(login.this, register.class);
                    startActivity(intent);
                }
            });
        } else {
            Log.e(TAG, "tvSignUp is null, cannot set OnClickListener.");
        }

        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "btnLogin clicked. Calling loginUser().");
                    loginUser();
                }
            });
        } else {
            Log.e(TAG, "btnLogin is null, cannot set OnClickListener.");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // As per your request, we are NOT automatically redirecting to news.
        // The login screen will always be displayed after the splash.
        Log.d(TAG, "onStart: Login activity showing. User session check (if any) is disabled.");
    }

    private void loginUser() {
        // Ensure etEmail and etPassword are not null before accessing their text
        if (etEmail == null || etPassword == null) {
            Log.e(TAG, "loginUser: Email or Password input fields are null. Cannot proceed.");
            Toast.makeText(login.this, "Error: Input fields not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Log.d(TAG, "loginUser: Attempting to log in with Email: " + email);

        // Basic input validation
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required.");
            etEmail.requestFocus();
            Log.d(TAG, "loginUser: Email is empty.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required.");
            etPassword.requestFocus();
            Log.d(TAG, "loginUser: Password is empty.");
            return;
        }

        // Authenticate user with Firebase Email and Password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Login successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "onComplete: Login Successful for user: " + (user != null ? user.getEmail() : "null"));
                            Toast.makeText(login.this, "Login Successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(login.this, news.class);
                            startActivity(intent);
                            finish(); // Finish LoginActivity after successful login
                        } else {
                            // Login failed
                            String errorMessage = "Authentication failed.";
                            if (task.getException() != null) {
                                errorMessage += " " + task.getException().getMessage();
                                Log.e(TAG, "onComplete: Login failed: " + task.getException().getMessage(), task.getException());
                            } else {
                                Log.e(TAG, "onComplete: Login failed with unknown error.");
                            }
                            Toast.makeText(login.this, errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}