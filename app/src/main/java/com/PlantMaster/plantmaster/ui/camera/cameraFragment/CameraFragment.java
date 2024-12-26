package com.PlantMaster.plantmaster.ui.camera.cameraFragment;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.view.PreviewView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
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

public class CameraFragment extends Fragment {

    private FragmentCameraBinding binding;

    private PreviewView previewView;
    private  CameraHelper cameraHelper;
    private PermissionHelper permissionCameraHelper;
    private PermissionHelper permissionGalleryHelper;
    private CameraHelper galleryHelper;

    // Kamera izin isteme baslaticisi
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                // İzin sonucu PermissionHelper'a yönlendirilir
                permissionCameraHelper.handleCameraPermissionResult(isGranted, () -> cameraHelper.startCamera(previewView));
            });
    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    // Seçilen resim üzerinde işlem yapabilirsiniz
                    Toast.makeText(requireContext(), "Resim seçildi: " + selectedImageUri, Toast.LENGTH_SHORT).show();
                }
            });
    private final ActivityResultLauncher<String> requestGalleryPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                permissionGalleryHelper.handleGalleryPermissionResult(isGranted,()->galleryHelper.openGallery());
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CameraViewModel cameraViewModel =
                new ViewModelProvider(this).get(CameraViewModel.class);

        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Nesne tanimlamalari
        previewView = binding.previewView;
        ImageButton galleryButton = binding.galleryButton;

        // Sinif nesnelerini tanimlama islemi
        cameraHelper = new CameraHelper(requireContext(),this);
        permissionCameraHelper = new PermissionHelper(requireContext(),requestPermissionLauncher);

        permissionGalleryHelper = new PermissionHelper(requireContext(),requestGalleryPermissionLauncher);
        galleryHelper = new CameraHelper(requireContext(),this,galleryActivityResultLauncher);

        // Kamera izinlerini kontrol et ve gerekliyse izin iste
        permissionCameraHelper.checkAndRequestCameraPermission(()-> cameraHelper.startCamera(previewView));

        // galleryButton'a tıklama
        galleryButton.setOnClickListener(v -> {
            permissionGalleryHelper.checkAndRequestGalleryPermission(() -> galleryHelper.openGallery());
        });

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}