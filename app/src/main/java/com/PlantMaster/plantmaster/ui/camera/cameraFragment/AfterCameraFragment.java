package com.PlantMaster.plantmaster.ui.camera.cameraFragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;

import java.io.File;


public class AfterCameraFragment extends Fragment {

    private ImageButton imageButton;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_send, container, false);

        imageView = view.findViewById(R.id.imageSend);
        imageButton=view.findViewById(R.id.btnClose);

        // Argument olarak gelen dosya yolunu al
        if (getArguments() != null) {
            String imagePath = getArguments().getString("imagePath");
            if (imagePath != null) {
                // Resmi URI olarak yükle ve ImageView'de göster
                File imageFile = new File(imagePath);
                Uri imageUri = Uri.fromFile(imageFile);
                imageView.setImageURI(imageUri);
            }
        }

        imageButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.navigation_camera);

        });

        return view;
    }
}


