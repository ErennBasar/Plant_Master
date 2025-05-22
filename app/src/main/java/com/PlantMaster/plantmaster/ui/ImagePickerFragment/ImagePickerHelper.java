package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.PlantMaster.plantmaster.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class ImagePickerHelper {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Context context;
    private Fragment fragment;
    private OnImagePickedListener listener;
    private LoadingDialog loadingDialog;

    public ImagePickerHelper(Context context, Fragment fragment, OnImagePickedListener listener) {
        this.context = context;
        this.fragment = fragment;
        this.listener = listener;
        this.loadingDialog = new LoadingDialog(context);
        initializeImagePicker(fragment);
    }

    private void initializeImagePicker(Fragment fragment) {
        imagePickerLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        handleImageSelection(result.getData());
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(context, ImagePicker.getError(result.getData()), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Fotoğraf seçimi iptal edildi.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void handleImageSelection(Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                this.listener.onImagePicked(uri);
                showLoading();

                ImageUploader.uploadImage(uri, context, new ImageUploader.UploadCallback() {
                    @Override
                    public void onSuccess(Bundle resultData) {
                        dismissLoading();
                        if (fragment != null && fragment.isAdded()) {
                            navigateToFragment(fragment, resultData);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        dismissLoading();
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private void showLoading() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    private void dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void openCamera() {
        ImagePicker.with((Activity) context)
                .cameraOnly()
                .crop(224f, 224f)
                .compress(1024)
                .maxResultSize(224, 224)
                .createIntent(intent -> {
                    imagePickerLauncher.launch(intent);
                    return null;
                });
    }

    public void openGallery() {
        ImagePicker.with((Activity) context)
                .galleryOnly()
                .crop(224f, 224f)
                .compress(1024)
                .maxResultSize(224, 224)
                .createIntent(intent -> {
                    imagePickerLauncher.launch(intent);
                    return null;
                });
    }

    public static void navigateToFragment(Fragment fragment, Bundle bundle) {
        try {
            NavController navController = NavHostFragment.findNavController(fragment);
            navController.navigate(R.id.ImageResult, bundle);
            Log.d("Navigation", "ImageResult fragment'ına başarıyla geçildi");
        } catch (Exception e) {
            Log.e("NavigationError", "Fragment geçiş hatası: " + e.getMessage());
            Toast.makeText(fragment.requireContext(), "Sayfa geçişinde hata oluştu", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnImagePickedListener {
        void onImagePicked(Uri imageUri);
    }
}