package com.PlantMaster.plantmaster.ui.profile.profileEdit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.view.LayoutInflater;
import android.widget.Toast;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentEmailChangeBinding;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EmailChangeFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText currentEmailAddress,editEmailAddress,currentPassword;
    private Button saveEmailButton;
    private FragmentEmailChangeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentEmailChangeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        auth = FirebaseAuth.getInstance();

        currentEmailAddress = binding.currentEmailAddress;
        editEmailAddress = binding.editEmailAddress;
        saveEmailButton = binding.saveEmailButton;
        currentPassword = binding.currentPassword;

        saveEmailButton.setOnClickListener(v -> updateUserProfile());

        return root;

    }
    private void updateUserProfile() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "No authenticated user found.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentEmail01 = currentEmailAddress.getText().toString();
        String currentPassword01 = currentPassword.getText().toString();
        String newEmail01 = editEmailAddress.getText().toString();

        verifyAndUpdateEmail(currentEmail01,currentPassword01, newEmail01);
    }
    private void verifyAndUpdateEmail(String currentEmail, String currentPassword, String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "No authenticated user found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kullanıcıyı yeniden doğrula
        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, currentPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // verifyBeforeUpdateEmail metodu ile yeni e-posta adresine doğrulama gönder
                        user.verifyBeforeUpdateEmail(newEmail)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Toast.makeText(getContext(), "Verification email sent to new email address.", Toast.LENGTH_SHORT).show();
                                        navigateToFragment();
                                    } else {
                                        Toast.makeText(getContext(), "Failed to send verification email: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), "Reauthentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void navigateToFragment() {
        if (getView() != null) {
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.navigation_profile);
        }
    }
}