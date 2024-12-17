package com.erenbasar.plantmaster.ui.camera;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erenbasar.plantmaster.R;

import com.erenbasar.plantmaster.databinding.FragmentCameraBinding;
import com.erenbasar.plantmaster.databinding.FragmentDashboardBinding;
import com.erenbasar.plantmaster.ui.dashboard.DashboardViewModel;

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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}