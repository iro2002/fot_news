package com.example.fot_news;

import android.annotation.SuppressLint;
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
import com.google.firebase.auth.UserProfileChangeRequest;

public class register extends AppCompatActivity {

    private TextInputEditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUp;
    private TextView tvSignIn;
    private FirebaseAuth mAuth; // Firebase Authentication instance

    @SuppressLint("MissingInflatedId") // You can often remove this if all IDs are correctly found
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Apply window insets for system bars
        // CORRECTED: Using the root ID from activity_register.xml -> android:id="@+id/register"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        // Initialize UI elements
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvSignIn = findViewById(R.id.tvSignIn);

        // Null check for UI elements (crucial for preventing crashes if IDs are mismatched)
        if (etUsername == null) Log.e("REGISTER_DEBUG", "etUsername (R.id.etUsername) is null! Check XML ID.");
        if (etEmail == null) Log.e("REGISTER_DEBUG", "etEmail (R.id.etEmail) is null! Check XML ID.");
        if (etPassword == null) Log.e("REGISTER_DEBUG", "etPassword (R.id.etPassword) is null! Check XML ID.");
        if (etConfirmPassword == null) Log.e("REGISTER_DEBUG", "etConfirmPassword (R.id.etConfirmPassword) is null! Check XML ID.");
        if (btnSignUp == null) Log.e("REGISTER_DEBUG", "btnSignUp (R.id.btnSignUp) is null! Check XML ID.");
        if (tvSignIn == null) Log.e("REGISTER_DEBUG", "tvSignIn (R.id.tvSignIn) is null! Check XML ID.");

        // Set listeners only if elements are found
        if (tvSignIn != null) {
            tvSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(register.this, login.class);
                    startActivity(intent);
                    finish(); // Finish the register activity when navigating back to login
                }
            });
        } else {
            Log.e("REGISTER_DEBUG", "tvSignIn is null, cannot set OnClickListener.");
        }

        if (btnSignUp != null) {
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUser();
                }
            });
        } else {
            Log.e("REGISTER_DEBUG", "btnSignUp is null, cannot set OnClickListener.");
        }
    }

    private void registerUser() {
        // Ensure input fields are not null before accessing their text
        if (etUsername == null || etEmail == null || etPassword == null || etConfirmPassword == null) {
            Log.e("REGISTER_DEBUG", "registerUser: One or more input fields are null. Cannot proceed.");
            Toast.makeText(register.this, "Error: Input fields not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required.");
            etUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required.");
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required.");
            etPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters.");
            etPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            etConfirmPassword.setError("Confirm password is required.");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match.");
            etConfirmPassword.requestFocus();
            return;
        }

        // Attempt to create user with Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration successful
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Update user profile with username
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(profileTask -> {
                                            if (profileTask.isSuccessful()) {
                                                Toast.makeText(register.this, "User registered and profile updated.",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(register.this, "User registered but profile update failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            // Redirect to news activity regardless of profile update success
                                            redirectToNewsScreen();
                                        });
                            } else {
                                Toast.makeText(register.this, "User registered, but could not get user object.",
                                        Toast.LENGTH_SHORT).show();
                                redirectToNewsScreen();
                            }
                        } else {
                            // Registration failed
                            String errorMessage = "Registration failed.";
                            if (task.getException() != null) {
                                errorMessage += " " + task.getException().getMessage();
                            }
                            Toast.makeText(register.this, errorMessage,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void redirectToNewsScreen() {
        Intent intent = new Intent(register.this, news.class);
        startActivity(intent);
        finish(); // Finish the register activity
    }
}