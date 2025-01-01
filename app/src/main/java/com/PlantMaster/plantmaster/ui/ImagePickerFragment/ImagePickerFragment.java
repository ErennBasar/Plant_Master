package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.PlantMaster.plantmaster.databinding.FragmentImagePickerBinding;


public class ImagePickerFragment extends Fragment implements ImagePickerHelper.OnImagePickedListener{

    private ImageView imageView;
    private FragmentImagePickerBinding binding;
    private ImagePickerHelper cameraHelper;
    private ImagePickerHelper galeriHelper;
    private RuntimePermissionHelper runtimeCameraPermission;
    private RuntimePermissionHelper runtimeGalleryPermission;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //cameraHelper = new ImagePickerHelper(requireContext(),this,this);
        //galeriHelper = new ImagePickerHelper(requireContext(),this,this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImagePickerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button btnCameraOpen = binding.btnCameraOpen;
        Button btnGalleryOpen = binding.btnGalleryOpen;

        runtimeCameraPermission = new RuntimePermissionHelper(requireContext(),requestPermissionLauncher);
        runtimeGalleryPermission = new RuntimePermissionHelper(requireContext(),requestGalleryPermissionLauncher);

        cameraHelper = new ImagePickerHelper(requireContext(),this,this);
        galeriHelper = new ImagePickerHelper(requireContext(),this,this);

        btnCameraOpen.setOnClickListener(v -> {runtimeCameraPermission.checkAndRequestCameraPermission(()->cameraHelper.openCamera());});
        btnGalleryOpen.setOnClickListener(v -> {runtimeGalleryPermission.checkAndRequestGalleryPermission(()->galeriHelper.openGallery());});

        return root;
    }

    @Override
    public void onImagePicked(Uri imageUri) {
    imageView.setImageURI(imageUri);
    }
    /**
     * Kamera izni isteme işlemini başlatan `ActivityResultLauncher`.
     * Kamera izni sonuçlarını `PermissionHelper` ile işler.
     */
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                // İzin sonucu RuntimePermissionHelper'a yönlendirilir
                //permissionCameraHelper.handleCameraPermissionResult(isGranted, () -> cameraHelper.startCamera(previewView));
                runtimeCameraPermission.handleCameraPermissionResult(isGranted,() -> cameraHelper.openCamera());
            });
    /**
     * Galeri erişim izni isteme işlemini başlatan `ActivityResultLauncher`.
     * İzin sonucunu `PermissionHelper` ile işler ve gerekli durumda galeriyi açar.
     */
    private final ActivityResultLauncher<String> requestGalleryPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//                permissionGalleryHelper.handleGalleryPermissionResult(isGranted,()->galleryHelper.openGallery());
                runtimeGalleryPermission.handleGalleryPermissionResult(isGranted,()->galeriHelper.openGallery());
            });
}