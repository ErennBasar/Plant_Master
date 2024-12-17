package com.erenbasar.plantmaster.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.erenbasar.plantmaster.R;

public class ProfileSignUpFragment extends Fragment {

    private EditText passwordInput, passwordInputAgain;
    private Button submitButton;

    public ProfileSignUpFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile_signup, container, false);


        passwordInput = rootView.findViewById(R.id.passwordInput);
        passwordInputAgain = rootView.findViewById(R.id.passwordInputAgain);
        submitButton = rootView.findViewById(R.id.submit);


        submitButton.setOnClickListener(v -> {

            String password = passwordInput.getText().toString();
            String passwordAgain = passwordInputAgain.getText().toString();

            if (password.isEmpty() || passwordAgain.isEmpty()) {

                Toast.makeText(getContext(), "Please fill in both password fields", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(passwordAgain)) {

                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(getContext(), "Passwords match!", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
