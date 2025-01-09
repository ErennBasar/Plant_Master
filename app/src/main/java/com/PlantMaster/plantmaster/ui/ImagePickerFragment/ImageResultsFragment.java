package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.animation.ObjectAnimator;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentImageResultsBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageResultsFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private FragmentImageResultsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentImageResultsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getImageUri().observe(getViewLifecycleOwner(), new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null) {
                    binding.imageViewResult.setImageURI(uri);
                }
            }
        });

        binding.buttonBack.setOnClickListener(v -> {
            navigateToFragment(root);
        });

        String plantName = "Potato";
        String diseaseName = "Rabies";
        String treatment = "Cure";

        binding.textViewPlantResult.setText("Plant Name: " + plantName);
        binding.textViewDiseaseResult.setText("Plant Disease: " + diseaseName);
        binding.textViewTreatmentResult.setText("Treatment: " + treatment);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        binding.textViewDateResult.setText("Date: " + currentDate);

        sharedViewModel.setPlantName(plantName);
        sharedViewModel.setDisease(diseaseName);
        sharedViewModel.setTreatment(treatment);
        sharedViewModel.setDate("Date: " + currentDate);

        animateTextView(binding.textViewPlantResult);
        animateTextView(binding.textViewDiseaseResult);
        animateTextView(binding.textViewTreatmentResult);
        animateTextView(binding.textViewDateResult);

        return root;
    }

    private void navigateToFragment(View view) {
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.ImagePickerFragment);
    }

    private void animateTextView(TextView textView) {
        ObjectAnimator fadeInAnim = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
        fadeInAnim.setDuration(2000);
        fadeInAnim.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
