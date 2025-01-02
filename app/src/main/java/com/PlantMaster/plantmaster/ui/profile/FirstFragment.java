package com.PlantMaster.plantmaster.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.PlantMaster.plantmaster.R;

public class FirstFragment extends Fragment {

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);

        Button firstLoginButton = view.findViewById(R.id.firstLoginButton);
        Button signupButton = view.findViewById(R.id.firstsignupButton);
        Button guestButton = view.findViewById(R.id.guestButton);

        firstLoginButton.setOnClickListener(v -> navigateToFragment(view, R.id.navigation_profile));
        signupButton.setOnClickListener(v -> navigateToFragment(view, R.id.SignupFragment));
        guestButton.setOnClickListener(v -> navigateToFragment(view, R.id.ImagePickerFragment));

        return view;
    }

    private void navigateToFragment(View view, int fragmentId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(fragmentId);
    }
}
