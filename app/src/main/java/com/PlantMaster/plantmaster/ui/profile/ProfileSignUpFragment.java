package com.PlantMaster.plantmaster.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.PlantMaster.plantmaster.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileSignUpFragment extends Fragment {

    private EditText usernameInput,emailInput,passwordInput, passwordInputAgain;
    private Button submitButton;
    private FirebaseAuth mAuth;

    public ProfileSignUpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile_signup, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        usernameInput = rootView.findViewById(R.id.usernameInput);
        emailInput = rootView.findViewById(R.id.emailInput);
        passwordInput = rootView.findViewById(R.id.passwordInput);
        passwordInputAgain = rootView.findViewById(R.id.passwordInputAgain);
        submitButton = rootView.findViewById(R.id.submit);


        submitButton.setOnClickListener(v -> {

            String username = usernameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String passwordAgain = passwordInputAgain.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {

                Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(passwordAgain)) {

                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Passwords match!", Toast.LENGTH_SHORT).show();
                createUserWithEmail(username,email, password);
            }

        });

        return rootView;
    }
    private void createUserWithEmail(String username, String email, String password){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Kullanıcının profilini güncelle
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username) // Display Name olarak kullanıcı adını ayarla
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            Toast.makeText(getContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(getContext(), "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
