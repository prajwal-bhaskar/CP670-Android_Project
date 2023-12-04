package com.example.event_management.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import com.example.event_management.databinding.FragmentProfileBinding;


// ... other imports ...
import com.example.event_management.R;
import com.example.event_management.databinding.FragmentProfileBinding;
import com.example.event_management.ui.login.LoggedInUserView;
import com.example.event_management.ui.login.LoginFormState;
import com.example.event_management.ui.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FragmentProfileBinding binding;
    private static final int RC_SIGN_IN = 9001;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        setupGoogleSignIn();

        binding.login.setOnClickListener(v -> {
            String email = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            attemptLogin(email, password);
        });

        binding.signup.setOnClickListener(v -> {
            // Toggle additional fields visibility
            toggleAdditionalFieldsVisibility(true);

            // Get user inputs
            String email = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            String name = binding.name.getText().toString();
            String dob = binding.dateOfBirth.getText().toString();
            String phone = binding.phoneNumber.getText().toString();

            // Proceed with sign up
            signUp(email, password, name, dob, phone);
        });

        binding.googleSignInButton.setOnClickListener(v -> signInWithGoogle());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, display user profile
            displayUserProfile(currentUser);
        } else {
            // User is not signed in, show the login form
            binding.userProfileLayout.setVisibility(View.GONE);
            binding.loginForm.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already signed in, display user profile
            displayUserProfile(currentUser);
        } else {
            // User is not signed in, show the login form
            binding.userProfileLayout.setVisibility(View.GONE);
            binding.loginForm.setVisibility(View.VISIBLE);
        }
    }

    private void toggleAdditionalFieldsVisibility(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        binding.name.setVisibility(visibility);
        binding.dateOfBirth.setVisibility(visibility);
        binding.phoneNumber.setVisibility(visibility);
    }
    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    private void attemptLogin(String username, String password) {
        if (!validateLoginForm(username, password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Login success
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUiWithUser(new LoggedInUserView(user.getDisplayName()));
                        displayUserProfile(user);

                        // TODO: Navigate to next screen or update current UI
                    } else {
                        // Login failed
                        showLoginFailed(R.string.login_failed);
                    }
                });
    }



    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Handle exception
                // ...
            }
        }
    }

    private void signUp(String email, String password, final String name, final String dob, final String phone)
    {
        if (!validateSignUpForm(email, password, name, dob,phone)) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Save additional information to Firebase Database/Firestore
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("name", name);
                            userInfo.put("dob", dob);
                            userInfo.put("phone", phone);

                            // Assuming you are using Firestore
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("users").document(user.getUid()).set(userInfo)
                                    .addOnSuccessListener(aVoid -> {
                                        // Handle success
                                        updateUiWithUser(new LoggedInUserView(name));
                                        displayUserProfile(user);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle failure
                                    });
                        }

                    } else {
                        // If sign up fails, display a message to the user.
                        showLoginFailed(R.string.sign_up_failed);
                    }
                });
    }



    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        assert user != null;
                        updateUiWithUser(new LoggedInUserView(user.getDisplayName()));
                        // TODO: Navigate to the next screen or update the current UI
                    } else {
                        // If sign in fails, display a message to the user.
                        // ...
                    }
                });
    }


    private void updateUiWithUser(LoggedInUserView model) {


        binding.loginForm.setVisibility(View.GONE);

        // Show user's profile information
        binding.userProfileLayout.setVisibility(View.VISIBLE);


        Toast.makeText(getContext(), "Welcome " + model.getDisplayName(), Toast.LENGTH_LONG).show();
    }


    private void showLoginFailed(@StringRes int errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }



    private boolean validateSignUpForm(String email, String password, String name, String dob, String phone) {
        boolean isValid = true;

        if (!isEmailValid(email)) {
            binding.username.setError(getString(R.string.invalid_email));
            isValid = false;
        }

        if (!isPasswordValid(password)) {
            binding.password.setError(getString(R.string.invalid_password));
            isValid = false;
        }

        if (name.trim().isEmpty()) {
            binding.name.setError(getString(R.string.invalid_name));
            isValid = false;
        }

        if (!isDateOfBirthValid(dob)) {
            binding.dateOfBirth.setError(getString(R.string.invalid_dob));
            isValid = false;
        }

        if (!isPhoneValid(phone)) {
            binding.phoneNumber.setError(getString(R.string.invalid_phone));
            isValid = false;
        }

        return isValid;
    }

    private boolean validateLoginForm(String email, String password) {
        boolean isValid = true;

        if (!isEmailValid(email)) {
            binding.username.setError(getString(R.string.invalid_email));
            isValid = false;
        }

        if (!isPasswordValid(password)) {
            binding.password.setError(getString(R.string.invalid_password));
            isValid = false;
        }
        return isValid;
    }

    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private boolean isDateOfBirthValid(String dob) {
        if (dob == null || dob.trim().isEmpty()) {
            return false;
        }


        return true;
    }

    private boolean isPhoneValid(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phone.matches("\\d{10}"); // Simple validation for 10 digit numbers
    }

    private void displayUserProfile(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String name = document.getString("name");
                    String dob = document.getString("dob");
                    String phone = document.getString("phone");
                    String email = user.getEmail();

                    binding.userNameTextView.setText("Name: " + (name != null ? name : ""));
                    binding.userDobTextView.setText("DOB: " + (dob != null ? dob : ""));
                    binding.userPhoneTextView.setText("Phone Number: " + (phone != null ? phone : ""));
                    binding.userEmailTextView.setText("Email: " + (email != null ? email : ""));
                } else {
                    Log.d("ProfileFragment", "No such document");
                }
            } else {
                Log.d("ProfileFragment", "get failed with ", task.getException());
            }
        });
    }

    private void loadProfileImage(String userId) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profileImageRef = storageRef.child("https://firebasestorage.googleapis.com/v0/b/event-manament-app.appspot.com/o/baseline_account_circle_black_24dp.png?alt=media&token=97f64a30-10f3-439e-b7fb-f2c4f048682e");

        profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(this)
                    .load(uri)
                    .placeholder(R.drawable.baseline_account_circle_black_24dp) // Placeholder image
                    .into(binding.profileImageButton);
        }).addOnFailureListener(exception -> {
            // Handle any errors, e.g., if the image does not exist
        });
    }
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(), task -> {
            // Update UI back to login form
            binding.userProfileLayout.setVisibility(View.GONE);
            binding.loginForm.setVisibility(View.VISIBLE);
        });
    }



}
