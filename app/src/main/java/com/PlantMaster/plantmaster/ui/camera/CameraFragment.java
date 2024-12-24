package com.PlantMaster.plantmaster.ui.camera;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.PlantMaster.plantmaster.R;

import com.PlantMaster.plantmaster.databinding.FragmentCameraBinding;
import com.PlantMaster.plantmaster.databinding.FragmentDashboardBinding;
import com.PlantMaster.plantmaster.ui.dashboard.DashboardViewModel;

public class CameraFragment extends Fragment {

    private FragmentCameraBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CameraViewModel cameraViewModel =
                new ViewModelProvider(this).get(CameraViewModel.class);

        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textCamera;
        cameraViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        // cameraButton'a tıklama
        ImageButton cameraButton = binding.cameraButton;
        cameraButton.setOnClickListener(v -> {
            // Yeni aktiviteyi başlat
            Intent intent = new Intent(getActivity(), InfoScreen.class);
            startActivity(intent);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}