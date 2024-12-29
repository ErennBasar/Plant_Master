package com.PlantMaster.plantmaster.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentProfileDetailBinding;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileDetailFragment extends Fragment {

    private FragmentProfileDetailBinding binding;
    private TextView emailTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // ViewBinding kullanarak layout'u bağla
        binding = FragmentProfileDetailBinding.inflate(inflater, container, false);
        binding.profileEditButton.setOnClickListener(this::profileEditButtonClicked);

        // Kullanıcı bilgilerini Firebase üzerinden al ve göster

        // Firebase kullanıcısını al
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            binding.textProfileName.setText(mAuth.getCurrentUser().getDisplayName() != null
                    ? mAuth.getCurrentUser().getDisplayName() : "No Display Name"); // DisplayName boşsa varsayılan bir mesaj göster
            binding.textEmail.setText(mAuth.getCurrentUser().getEmail());
        } else {
            // Kullanıcı oturum açmamışsa varsayılan mesajlar göster
            binding.textProfileName.setText("Guest User");
            binding.textEmail.setText("No email");
        }

        return binding.getRoot();
    }

    private void profileEditButtonClicked(View view) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.action_profileDetailFragment_to_profileEditFragment);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
