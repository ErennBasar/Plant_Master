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
import android.widget.Toast;

import com.PlantMaster.plantmaster.R;

import com.PlantMaster.plantmaster.databinding.FragmentPasswordChangeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class PasswordChangeFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText editPassword,editPasswordConfirm;
    private Button savePasswordButton;
    private FragmentPasswordChangeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPasswordChangeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();

       // currentPassword = binding.currentPassword;
        editPassword = binding.editPassword;
        editPasswordConfirm = binding.editPasswordConfirm;
        savePasswordButton = binding.savePasswordButton;
        binding.backPasswordChange.setOnClickListener(this::navigateToBack);

        savePasswordButton.setOnClickListener(v -> updateUserProfile());

        return root;
    }

    private void updateUserProfile() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "No authenticated user found.", Toast.LENGTH_SHORT).show();
            return;
        }
        String newPassword = editPassword.getText().toString();
        String confirmPassword = editPasswordConfirm.getText().toString();

    // Şifre Güncelle
        if (!newPassword.isEmpty() && newPassword.equals(confirmPassword)) {
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Password updated!", Toast.LENGTH_SHORT).show();
                        navigateToFragment();
                    } else {
                        Toast.makeText(getContext(), "Error updating password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    } else if (!newPassword.equals(confirmPassword)) {
        Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToFragment() {
        if (getView() != null) {
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.navigation_profile);
        }
    }
    private void navigateToBack(View view){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.ProfileEditFragment);
    }
}