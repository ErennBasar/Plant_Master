package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        processArguments();
        setupUI();
        setupObservers();
    }

    private void processArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            String imageUri = bundle.getString("imageUri");
            String plant = bundle.getString("plant");
            String disease = bundle.getString("disease");
            double confidence=bundle.getDouble("confidence");

            if (imageUri != null) {
                Uri uri = Uri.parse(imageUri);
                binding.imageViewResult.setImageURI(uri);
                sharedViewModel.setImageUri(uri);
            }

            if (plant != null && disease != null) {
                updateUI(plant, disease,confidence);
            }
        }
    }

    private void setupUI() {
        binding.buttonBack.setOnClickListener(v -> navigateBack());
    }

    private void updateUI(String plantName, String diseaseName,double confidence) {
        String treatment = getTreatmentForDisease(plantName, diseaseName);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        binding.textViewPlantResult.setText("Plant: " + plantName);
        binding.textViewDiseaseResult.setText("Disease: " + diseaseName);
        binding.textViewTreatmentResult.setText("Treatment: " + treatment);
        binding.textViewDateResult.setText("Date: " + currentDate);
        binding.textViewConfidence.setText(String.format(Locale.getDefault(), "Confidence: %.2f%%", confidence * 100));


        animateTextViews();
        saveHistoryItem(plantName, diseaseName, currentDate,treatment);
    }

    private String getTreatmentForDisease(String plantName, String diseaseName) {
        if (diseaseName == null || plantName == null) return "Consult an agricultural expert";

        plantName = plantName.toLowerCase();
        diseaseName = diseaseName.toLowerCase();

        if (diseaseName.contains("healthy")) {
            return "The plant is healthy. No treatment needed!";
        }

        switch (plantName) {
            case "apple":
                switch (diseaseName) {
                    case "scab":
                        return "Apply fungicides early in the season. Remove fallen leaves.";
                    case "black rot":
                        return "Prune affected branches. Use fungicide spray.";
                    case "cedar apple rust":
                        return "Remove nearby junipers. Use fungicides like myclobutanil.";
                }
                break;

            case "blueberry":
                return "Ensure proper drainage and apply fungicide if necessary.";

            case "cherry":
                if (diseaseName.contains("powdery mildew"))
                    return "Prune infected parts and apply sulfur.";
                break;

            case "corn":
                if (diseaseName.contains("cercospora") || diseaseName.contains("gray leaf spot"))
                    return "Use resistant hybrids. Apply fungicides like azoxystrobin.";
                else if (diseaseName.equals("common rust"))
                    return "Use rust-resistant hybrids and fungicide if severe.";
                else if (diseaseName.equals("northern leaf blight"))
                    return "Use resistant hybrids and rotate crops.";
                break;

            case "grape":
                if (diseaseName.equals("black rot"))
                    return "Use mancozeb or captan fungicide. Remove mummified berries.";
                else if (diseaseName.contains("esca") || diseaseName.contains("black measles"))
                    return "Prune infected wood and avoid over-irrigation.";
                else if (diseaseName.contains("leaf blight") || diseaseName.contains("isariopsis"))
                    return "Apply fungicide. Improve air circulation.";
                break;

            case "orange":
                if (diseaseName.contains("huanglongbing") || diseaseName.contains("citrus greening"))
                    return "Remove infected trees. Control psyllid vectors.";
                else if (diseaseName.equals("canker"))
                    return "Apply copper sprays. Remove infected limbs.";
                break;

            case "peach":
                if (diseaseName.equals("bacterial spot"))
                    return "Use copper-based sprays. Prune infected limbs.";
                break;

            case "pepper":
            case "bell pepper":
                if (diseaseName.equals("bacterial spot"))
                    return "Use disease-free seeds and copper sprays.";
                break;

            case "potato":
                switch (diseaseName) {
                    case "early blight":
                        return "Apply chlorothalonil or mancozeb.";
                    case "late blight":
                        return "Use fungicide sprays. Destroy infected tubers.";
                }
                break;

            case "raspberry":
                return "Ensure proper spacing. Prune to increase airflow.";

            case "soybean":
                return "Use resistant varieties and rotate crops.";

            case "squash":
                if (diseaseName.contains("powdery mildew"))
                    return "Apply sulfur-based fungicides.";
                break;

            case "strawberry":
                if (diseaseName.equals("leaf scorch"))
                    return "Remove infected leaves. Use fungicides if needed.";
                break;

            case "tomato":
                switch (diseaseName) {
                    case "bacterial spot":
                        return "Use copper-based sprays and certified seeds.";
                    case "early blight":
                        return "Remove old leaves. Use fungicides like chlorothalonil.";
                    case "late blight":
                        return "Destroy infected plants. Apply fungicides like mancozeb.";
                    case "leaf mold":
                        return "Ensure ventilation. Use fungicides like chlorothalonil.";
                    case "septoria leaf spot":
                        return "Remove lower leaves. Use appropriate fungicide.";
                    case "target spot":
                        return "Use preventative fungicide sprays.";
                    case "yellow leaf curl virus":
                        return "Control whiteflies. Use resistant cultivars.";
                    case "mosaic virus":
                        return "Remove infected plants. Disinfect tools.";
                    case "spider mites two-spotted spider mite":
                        return "Use insecticidal soap or neem oil. Increase humidity if indoors.";
                }
                break;
        }

        return "Consult an agricultural expert for advice.";
    }



    private void animateTextViews() {
        animateTextView(binding.textViewPlantResult);
        animateTextView(binding.textViewDiseaseResult);
        animateTextView(binding.textViewTreatmentResult);
        animateTextView(binding.textViewDateResult);
    }

    private void animateTextView(TextView textView) {
        textView.setAlpha(0f);
        textView.animate().alpha(1f).setDuration(1000).start();
    }

    private void saveHistoryItem(String plantName, String diseaseName, String date,String treatment) {
        Uri imageUri = sharedViewModel.getImageUri().getValue();
        if (imageUri != null) {
            sharedViewModel.addHistoryItem(imageUri, plantName, diseaseName, date,treatment);
        }
    }

    private void setupObservers() {
        sharedViewModel.getImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                binding.imageViewResult.setImageURI(uri);
            }
        });
    }

    private void navigateBack() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.ImagePickerFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}