package com.example.fot_news;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType; // Import InputType for setting keyboard type
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment; // Import DialogFragment base class

import com.example.fot_news.fragments.EditFieldDialogFragment; // Import your custom dialog fragment
import com.example.fot_news.fragments.FragmentSignOut; // Assuming this is your sign-out fragment
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

// Implement the EditFieldDialogListener interface to receive data from the dialog
public class user extends AppCompatActivity implements EditFieldDialogFragment.EditFieldDialogListener {

    private ImageView backArrow;
    private ImageView profileImage; // Assuming you have a profile image view
    private TextView headerUserName; // The TextView in the header displaying user name

    private MaterialButton logoutButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    // EditText fields and their corresponding edit icons
    private EditText editTextName, editTextUserName, editTextTelephone, editTextEmail;
    private ImageView iconEditName, iconEditUserName, iconEditTelephone, iconEditEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // For edge-to-edge display
        setContentView(R.layout.activity_user);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser(); // Get the currently logged-in user

        // Apply window insets for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_user), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        backArrow = findViewById(R.id.back_arrow);
        profileImage = findViewById(R.id.profile_image); // Assuming you have this ID in your layout
        headerUserName = findViewById(R.id.user_name); // Assuming this is the TextView in the header

        logoutButton = findViewById(R.id.logout_button);

        editTextName = findViewById(R.id.edit_text_name);
        iconEditName = findViewById(R.id.icon_edit_name);

        editTextUserName = findViewById(R.id.edit_text_user_name);
        iconEditUserName = findViewById(R.id.icon_edit_user_name);

        editTextTelephone = findViewById(R.id.edit_text_telephone);
        iconEditTelephone = findViewById(R.id.icon_edit_telephone);

        editTextEmail = findViewById(R.id.edit_text_email);
        iconEditEmail = findViewById(R.id.icon_edit_email);

        // Make EditTexts non-editable directly; editing happens via the dialog
        // This is important because the icon listener will trigger the dialog
        editTextName.setFocusable(false);
        editTextName.setClickable(true); // Allow clicks to trigger potential parent listeners/drawable clicks
        editTextUserName.setFocusable(false);
        editTextUserName.setClickable(true);
        editTextTelephone.setFocusable(false);
        editTextTelephone.setClickable(true);
        editTextEmail.setFocusable(false);
        editTextEmail.setClickable(true);

        // Set up click listener for the back arrow
        if (backArrow != null) {
            backArrow.setOnClickListener(v -> finish());
        }

        // Set up click listener for the logout button
        if (logoutButton != null) {
            logoutButton.setOnClickListener(v -> {
                Intent intent = new Intent(user.this, FragmentSignOut.class);
                startActivityForResult(intent, FragmentSignOut.REQUEST_CODE_SIGN_OUT);
            });
        }

        // Set up listeners for pencil icons to launch the edit dialog
        // Pass the appropriate title, current value, field name for Firestore, and input type
        setupEditIconListener(iconEditName, "Edit Name", editTextName.getText().toString(), "name", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        setupEditIconListener(iconEditUserName, "Edit User Name", editTextUserName.getText().toString(), "username", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setupEditIconListener(iconEditTelephone, "Edit Telephone", editTextTelephone.getText().toString(), "telephone", InputType.TYPE_CLASS_PHONE);
        setupEditIconListener(iconEditEmail, "Edit Email", editTextEmail.getText().toString(), "email", InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        // Note: The email field from Firebase Auth is often read-only, but you can allow
        // editing if your app handles email re-authentication/updates separately.
        // For simplicity, we allow editing here, but remember Firebase Auth email changes
        // need FirebaseAuth.getInstance().getCurrentUser().updateEmail().

        // Load existing user data from Firestore
        loadUserData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FragmentSignOut.REQUEST_CODE_SIGN_OUT) {
            if (resultCode == RESULT_OK && data != null) {
                boolean signOutConfirmed = data.getBooleanExtra(FragmentSignOut.RESULT_SIGN_OUT, false);
                if (signOutConfirmed) {
                    mAuth.signOut();
                    Toast.makeText(user.this, "Logged out successfully.", Toast.LENGTH_SHORT).show();

                    // Redirect to login screen and clear back stack
                    Intent intent = new Intent(user.this, login.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(user.this, "Sign out cancelled.", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(user.this, "Sign out cancelled.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Sets up the click listener for an edit icon to launch the EditFieldDialogFragment.
     *
     * @param icon The ImageView (pencil icon) that triggers the edit.
     * @param dialogTitle The title to display in the dialog (e.g., "Edit Name").
     * @param currentValue The current value of the field to pre-fill the dialog's EditText.
     * @param fieldName The Firestore field name (e.g., "name", "username") to update.
     * @param inputType The InputType constant for the dialog's EditText (e.g., InputType.TYPE_CLASS_TEXT).
     */
    private void setupEditIconListener(ImageView icon, String dialogTitle, String currentValue, String fieldName, int inputType) {
        if (icon != null) {
            icon.setOnClickListener(v -> {
                // Create a new instance of the dialog fragment with required arguments
                EditFieldDialogFragment dialogFragment = EditFieldDialogFragment.newInstance(dialogTitle, currentValue, fieldName, inputType);
                // Show the dialog using the activity's FragmentManager
                dialogFragment.show(getSupportFragmentManager(), EditFieldDialogFragment.TAG);
                // Clear target fragment if set by previous call (good practice for Activity-launched dialogs)
                dialogFragment.setTargetFragment(null, 0);
            });
        }
    }

    /**
     * Callback method from EditFieldDialogFragment when the "Save" button is clicked.
     * This method receives the updated field name and its new value.
     *
     * @param fieldName The name of the field that was edited (e.g., "name", "telephone").
     * @param newValue The new value entered by the user in the dialog.
     */
    @Override
    public void onSaveField(String fieldName, String newValue) {
        // First, save the updated data to Firebase Firestore
        saveUserDataField(fieldName, newValue);

        // Then, update the corresponding EditText on the main screen to reflect the change immediately
        switch (fieldName) {
            case "name":
                editTextName.setText(newValue);
                // Also update the header name if it's the "name" field
                headerUserName.setText(newValue);
                break;
            case "username":
                editTextUserName.setText(newValue);
                break;
            case "telephone":
                editTextTelephone.setText(newValue);
                break;
            case "email":
                editTextEmail.setText(newValue);
                // Note: Updating email in Firestore does NOT update the Firebase Auth email.
                // For Auth email update, use currentUser.updateEmail(newValue).
                break;
            // Add more cases here if you have other editable fields
        }
    }

    /**
     * Loads the current user's data from Firebase Firestore.
     */
    private void loadUserData() {
        if (currentUser != null) {
            String uid = currentUser.getUid(); // Get the current user's UID
            DocumentReference docRef = db.collection("users").document(uid); // Reference to the user's document

            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Data exists for this user, retrieve it
                    String name = documentSnapshot.getString("name");
                    String username = documentSnapshot.getString("username");
                    String telephone = documentSnapshot.getString("telephone");
                    // String profileImageUrl = documentSnapshot.getString("profileImageUrl"); // If you implement image loading

                    String email = currentUser.getEmail(); // Get email from Firebase Auth (more reliable)

                    // Update UI with loaded data
                    if (name != null && !name.isEmpty()) {
                        editTextName.setText(name);
                        headerUserName.setText(name);
                    } else {
                        // Fallback to Firebase Auth display name if Firestore 'name' is empty
                        editTextName.setText(currentUser.getDisplayName());
                        headerUserName.setText(currentUser.getDisplayName());
                    }
                    if (username != null && !username.isEmpty()) {
                        editTextUserName.setText(username);
                    } else {
                        editTextUserName.setHint("Set your username");
                    }
                    if (telephone != null && !telephone.isEmpty()) {
                        editTextTelephone.setText(telephone);
                    } else {
                        editTextTelephone.setHint("Add your telephone number");
                    }
                    if (email != null && !email.isEmpty()) {
                        editTextEmail.setText(email);
                    }
                } else {
                    // No data found for this user in Firestore, initialize a new profile
                    Toast.makeText(user.this, "Profile data not found. Initializing new profile.", Toast.LENGTH_LONG).show();
                    // Set default values in UI from Auth
                    if (currentUser.getDisplayName() != null) {
                        editTextName.setText(currentUser.getDisplayName());
                        headerUserName.setText(currentUser.getDisplayName());
                    }
                    if (currentUser.getEmail() != null) {
                        editTextEmail.setText(currentUser.getEmail());
                    }
                    initializeNewUserProfile(); // Call method to save default profile to Firestore
                }
            }).addOnFailureListener(e -> {
                // Handle errors during data loading
                Toast.makeText(user.this, "Error loading profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                // Set default UI values even on failure to avoid empty fields
                if (currentUser.getDisplayName() != null) {
                    editTextName.setText(currentUser.getDisplayName());
                    headerUserName.setText(currentUser.getDisplayName());
                }
                if (currentUser.getEmail() != null) {
                    editTextEmail.setText(currentUser.getEmail());
                }
            });
        } else {
            // User not logged in, redirect to login screen
            Toast.makeText(user.this, "User not logged in. Please log in again.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(user.this, login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    /**
     * Saves a specific user data field to Firebase Firestore.
     *
     * @param fieldName The name of the field to update (e.g., "name", "telephone").
     * @param value The new value for the field.
     */
    private void saveUserDataField(String fieldName, String value) {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DocumentReference docRef = db.collection("users").document(uid); // Reference to the user's document

            Map<String, Object> updates = new HashMap<>();
            updates.put(fieldName, value); // Put the updated field and value into a map

            docRef.update(updates) // Update the specific field(s) in Firestore
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(user.this, fieldName + " updated successfully!", Toast.LENGTH_SHORT).show();
                        // If the name field was updated, also update the header text
                        if (fieldName.equals("name")) {
                            headerUserName.setText(value);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(user.this, "Error updating " + fieldName + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        } else {
            Toast.makeText(user.this, "Cannot save data: user not logged in.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initializes a new user profile document in Firestore with default values
     * if one doesn't already exist for the authenticated user.
     */
    private void initializeNewUserProfile() {
        if (currentUser != null) {
            String uid = currentUser.getUid();
            DocumentReference docRef = db.collection("users").document(uid);

            Map<String, Object> userDefaults = new HashMap<>();
            // Use Firebase Auth display name as default if available, otherwise "New User"
            userDefaults.put("name", currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "New User");
            // Generate a simple default username
            userDefaults.put("username", "user_" + uid.substring(0, Math.min(uid.length(), 6)));
            userDefaults.put("telephone", ""); // Default empty string for telephone
            // Use Firebase Auth email as default if available, otherwise empty
            userDefaults.put("email", currentUser.getEmail() != null ? currentUser.getEmail() : "");
            userDefaults.put("profileImageUrl", ""); // Default empty string for profile image URL

            // Use .set() with MergeField to avoid overwriting existing fields if called again
            // For initial setup, .set() is fine as it creates if not exists
            docRef.set(userDefaults)
                    .addOnSuccessListener(aVoid -> Toast.makeText(user.this, "New profile initialized!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(user.this, "Error initializing profile: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }
    }
}