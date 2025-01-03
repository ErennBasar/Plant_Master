package com.PlantMaster.plantmaster.ui.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentHomeBinding;
import com.PlantMaster.plantmaster.ui.ImagePickerFragment.SharedViewModel;

public class HomeFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private TextView textViewPlant;
    private TextView textViewDisease;
    private TextView textViewDate;
    private FragmentHomeBinding binding;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewPlant = root.findViewById(R.id.textViewPlant);
        textViewDisease = root.findViewById(R.id.textViewDisease);
        textViewDate = root.findViewById(R.id.textViewDate);


        ConstraintLayout constraintLayout = binding.constraintLayoutHistory;
        constraintLayout.setOnClickListener(v -> navigateToFragment(root));

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.setPlantName(textViewPlant.getText().toString());
        sharedViewModel.setDisease(textViewDisease.getText().toString());
        sharedViewModel.setDate(textViewDate.getText().toString());

        // Image URI'yi gözlemleyerek UI'yı güncelliyoruz
        sharedViewModel.getImageUri().observe(getViewLifecycleOwner(), imageUri -> {
            if (imageUri != null) {
                binding.imageViewHistory.setImageURI(imageUri);
            }
        });
        return root;
    }

    private void navigateToFragment(View view) {

        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.HistoryDetail);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
