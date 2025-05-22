package com.PlantMaster.plantmaster.ui.history;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentHistoryDetailBinding;
import com.PlantMaster.plantmaster.ui.ImagePickerFragment.SharedViewModel;

public class HistoryDetailFragment extends Fragment {

    private FragmentHistoryDetailBinding binding;
    private SharedViewModel sharedViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        setupUI();
        processArguments();
    }

    private void setupUI() {
        binding.buttonBack.setOnClickListener(v -> navigateBack());
    }
    private void processArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            binding.textViewPlant.setText(
                    getString(R.string.plant_label, bundle.getString("plantName", ""))
            );
            binding.textViewDisease.setText(
                    getString(R.string.disease_label, bundle.getString("disease", ""))
            );
            binding.textViewTreatment.setText(
                    getString(R.string.treatment_label, bundle.getString("treatment", "") )
            );
            binding.textViewDate.setText("Date "+bundle.getString("date", ""));

            String imageUriString = bundle.getString("imageUri");
            if (imageUriString != null) {
                binding.imageViewHistory.setImageURI(Uri.parse(imageUriString));
            }
        }
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.navigation_home);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}