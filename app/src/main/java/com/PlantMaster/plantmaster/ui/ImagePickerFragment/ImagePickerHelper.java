package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class ImagePickerHelper {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Context context;
    private OnImagePickedListener listener;

    public ImagePickerHelper(Context context, Fragment fragment, OnImagePickedListener listener){
        this.context = context;
        this.listener = listener;
        // ActivityResultLauncher'ı başlat
        imagePickerLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri uri = data.getData();
                            if (uri != null) {
                                this.listener.onImagePicked(uri); // Görüntüyü dinleyiciye gönder
                                navigateToFragment(fragment.getView());;
                            }
                        }
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(context, ImagePicker.getError(result.getData()), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Fotoğraf seçimi iptal edildi.", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
    // Kamera açma ve fotoğraf çekme işlemi
    public void openCamera() {
        ImagePicker.with((Activity) context)
                .cameraOnly() // Kamera aç
                .crop(224f, 224f) // 224x224 kırpma oranı
                .compress(1024) // Fotoğrafı 1MB ile sınırla
                .maxResultSize(224, 224) // Sonuç fotoğrafı 224x224
                .createIntent(intent -> {
                    imagePickerLauncher.launch(intent); // Kamera aç
                    return null;
                });
    }
    // Fotoğrafı seçme işlemi
    public void openGallery() {
        ImagePicker.with((Activity) context)
                .galleryOnly() // Sadece galeriden seçme
                .crop(224f, 224f) // 224x224 kırpma oranı
                .compress(1024) // Fotoğrafı 1MB ile sınırla
                .maxResultSize(224, 224) // Sonuç fotoğrafı 224x224
                .createIntent(intent -> {
                    imagePickerLauncher.launch(intent); // Galeri aç
                    return null;
                });
    }
    private void navigateToFragment(View view) {
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.ImageResult);
    }

    // Dinleyici arayüzü
    public interface OnImagePickedListener {
        void onImagePicked(Uri imageUri); // Fotoğraf seçildiğinde bu metod çağrılır
    }
}
