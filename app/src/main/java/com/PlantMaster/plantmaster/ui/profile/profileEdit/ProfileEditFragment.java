package com.PlantMaster.plantmaster.ui.profile.profileEdit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentProfileDetailBinding;
import com.PlantMaster.plantmaster.databinding.FragmentProfileEditBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.PlantMaster.plantmaster.ui.profile.profileEdit.UsernameChangeFragment;

public class ProfileEditFragment extends Fragment {

    private NavController navController;
    private FragmentProfileEditBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // ViewBinding kullanarak layout'u bağla
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);

        binding.changeUsername.setOnClickListener(this::changeUsernameButtonClicked);
        binding.changeEmail.setOnClickListener(this::changeEmailButtonClicked);
        binding.changePassword.setOnClickListener(this::changePasswordButtonClicked);
        binding.logoutButton.setOnClickListener(this::logoutButtonClicked);
        binding.backProfileEditButton.setOnClickListener(this::navigateToBack);

        return binding.getRoot();
    }
    // Change Username butonu tıklama olayı
    private void changeUsernameButtonClicked(View view) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.usernameChangeFragment);
    }
    // Change Email butonu tıklama olayı
    private void changeEmailButtonClicked(View view) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.emailChangeFragment);
    }

    // Change Password butonu tıklama olayı
    private void changePasswordButtonClicked(View view) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.passwordChangeFragment);
    }
    private void logoutButtonClicked(View view) {
        FirebaseAuth.getInstance().signOut(); // Firebase ile oturumu kapat

        // Çıkış yapıldıktan sonra LoginFragment'e yönlendir
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_profile); // Giriş ekranına yönlendir
    }
    private void navigateToBack(View view){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.profileDetailFragment);
    }
}
