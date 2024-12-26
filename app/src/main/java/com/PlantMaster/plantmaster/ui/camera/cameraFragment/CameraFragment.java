package com.PlantMaster.plantmaster.ui.camera.cameraFragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.PlantMaster.plantmaster.databinding.FragmentCameraBinding;
import com.PlantMaster.plantmaster.ui.camera.CameraViewModel;
import com.PlantMaster.plantmaster.ui.camera.InfoScreen;
import com.google.common.util.concurrent.ListenableFuture;

public class CameraFragment extends Fragment {

    private FragmentCameraBinding binding;

    private PreviewView previewView;
    private  CameraHelper cameraHelper;
    private PermissionHelper permissionHelper;

    // Kamera izin isteme baslaticisi
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                // İzin sonucu PermissionHelper'a yönlendirilir
                permissionHelper.handleCameraPermissionResult(isGranted, () -> cameraHelper.startCamera(previewView));
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CameraViewModel cameraViewModel =
                new ViewModelProvider(this).get(CameraViewModel.class);

        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // PreviewView tanimlama
        previewView = binding.previewView;

        // Sinif nesnelerini tanimlama islemi
        cameraHelper = new CameraHelper(requireContext(),this);
        permissionHelper = new PermissionHelper(requireContext(),requestPermissionLauncher);

        // Kamera izinlerini kontrol et ve gerekliyse izin iste
        permissionHelper.checkAndRequestCameraPermission(()-> cameraHelper.startCamera(previewView));

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}