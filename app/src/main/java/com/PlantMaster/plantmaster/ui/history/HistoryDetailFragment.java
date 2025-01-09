package com.PlantMaster.plantmaster.ui.history;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.PlantMaster.plantmaster.R;

import com.PlantMaster.plantmaster.ui.ImagePickerFragment.SharedViewModel;

public class HistoryDetailFragment extends Fragment {

    private ImageView imageViewHistory;
    private TextView textViewPlant;
    private TextView textViewDisease;
    private TextView textViewDate;
    private TextView textViewTreatment;
    private SharedViewModel sharedViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history_detail, container, false);

        textViewPlant = root.findViewById(R.id.textViewPlant);
        textViewDisease = root.findViewById(R.id.textViewDisease);
        textViewDate = root.findViewById(R.id.textViewDate);
        imageViewHistory = root.findViewById(R.id.imageViewHistory);
        textViewTreatment= root.findViewById(R.id.textViewTreatment);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getTreatment().observe(getViewLifecycleOwner(), treatment -> {
            textViewTreatment.setText("Treatment: " + treatment);
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            String plantName = bundle.getString("plantName");
            String disease = bundle.getString("disease");
            String date = bundle.getString("date");
            String imageUriString = bundle.getString("imageUri");


            if (plantName != null) {
                textViewPlant.setText(plantName);
            }
            if (disease != null) {
                textViewDisease.setText(disease);
            }
            if (date != null) {
                textViewDate.setText(date);
            }


            if (imageUriString != null) {
                Uri imageUri = Uri.parse(imageUriString);
                imageViewHistory.setImageURI(imageUri);
            }
        }

        return root;
    }
}
