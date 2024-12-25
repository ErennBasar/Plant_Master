package com.PlantMaster.plantmaster.ui.camera;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.PlantMaster.plantmaster.R;

import com.PlantMaster.plantmaster.databinding.FragmentCameraBinding;
import com.PlantMaster.plantmaster.databinding.FragmentDashboardBinding;
import com.PlantMaster.plantmaster.ui.dashboard.DashboardViewModel;
import com.google.common.util.concurrent.ListenableFuture;

public class CameraFragment extends Fragment {

    private FragmentCameraBinding binding;

    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CameraViewModel cameraViewModel =
                new ViewModelProvider(this).get(CameraViewModel.class);

        binding = FragmentCameraBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // cameraButton'a tıklama
        ImageButton cameraButton = binding.cameraButton;
        cameraButton.setOnClickListener(v -> {
            // Yeni aktiviteyi başlat
            Intent intent = new Intent(getActivity(), InfoScreen.class);
            startActivity(intent);
        });

        // preview tanimlama
        previewView = binding.previewView;

        // goruntu cekme islemi
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        // kamera izinleri verilmis ise goruntuyu gosterir
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            }
        } else {
            startCamera();
        }
        return root;
    }

    // Yeni izin isteme için ActivityResultLauncher
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // İzin verildiyse kamerayı başlat
                    startCamera();
                } else {
                    // İzin verilmediyse kullanıcıyı uyar
                    Toast.makeText(requireContext(), "Kamera izni gereklidir", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void startCamera() {
        cameraProviderFuture.addListener(() -> {
            try {
                // CamerProvider al
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Preview konfigurasyon
                Preview preview = new Preview.Builder().build();

                // Kamera secimi
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                // Preview -> PreviewView baglanti
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                // Kamera ve Priview baglanti
                Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview);

            } catch (Exception e) {
                Log.e("CameraX", "Kamera baslamadi", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }
}