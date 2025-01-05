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
    private SharedViewModel sharedViewModel;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history_detail, container, false);

        textViewPlant = root.findViewById(R.id.textViewPlant);
        textViewDisease = root.findViewById(R.id.textViewDisease);
        textViewDate = root.findViewById(R.id.textViewDate);
        imageViewHistory = root.findViewById(R.id.imageViewHistory);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        sharedViewModel.getPlantName().observe(getViewLifecycleOwner(), plantName -> {
            textViewPlant.setText(plantName);
        });

        sharedViewModel.getDisease().observe(getViewLifecycleOwner(), disease -> {
            textViewDisease.setText(disease);
        });

        sharedViewModel.getDate().observe(getViewLifecycleOwner(), date -> {
            textViewDate.setText(date);
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            String imageUriString = bundle.getString("imageUri");
            if (imageUriString != null) {
                Uri imageUri = Uri.parse(imageUriString);
                imageViewHistory.setImageURI(imageUri); // ImageView'a set etme
            }
        }


        return root;
    }
}
