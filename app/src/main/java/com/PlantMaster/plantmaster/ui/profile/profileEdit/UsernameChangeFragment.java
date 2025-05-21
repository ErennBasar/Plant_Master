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
import com.PlantMaster.plantmaster.databinding.FragmentUsernameChangeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class UsernameChangeFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText editUsername;
    private Button saveUsernameButton;
    private FragmentUsernameChangeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsernameChangeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        auth = FirebaseAuth.getInstance();


        editUsername = binding.editUsername;
        saveUsernameButton = binding.saveUsernameButton;
        binding.backUsernameChange.setOnClickListener(this::navigateToBack);

        saveUsernameButton.setOnClickListener(v -> updateUserProfile());

        return root;
    }

    private void navigateToBack(View view){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.ProfileEditFragment);
    }
    private void navigateToFragment() {
        if (getView() != null) {
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.profileDetailFragment);
        }
    }


    private void updateUserProfile() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "No authenticated user found.", Toast.LENGTH_SHORT).show();
            return;
        }
        String newUsername = editUsername.getText().toString();

        // Kullanıcı Adını Güncelle
        if (!newUsername.isEmpty()) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newUsername)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Username updated!", Toast.LENGTH_SHORT).show();

                            navigateToFragment();
                        } else {
                            Toast.makeText(getContext(), "Error updating username: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}