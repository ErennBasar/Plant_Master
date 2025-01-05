package com.PlantMaster.plantmaster.ui.history;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;

import android.os.Bundle;


import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentHistoryBinding;
import com.PlantMaster.plantmaster.ui.ImagePickerFragment.SharedViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryHelper {

    private final Context context;
    private final FragmentHistoryBinding binding;
    private final SharedViewModel sharedViewModel;

    public HistoryHelper(Context context, FragmentHistoryBinding binding, SharedViewModel sharedViewModel) {
        this.context = context;
        this.binding = binding;
        this.sharedViewModel = sharedViewModel;
    }

    public void updateUIWithImages(List<Uri> uriList) {
        if (uriList == null || uriList.isEmpty()) return;

        LinearLayout mainLayout = binding.HistoryLinearLayout;

        for (Uri uri : uriList) {
            if (!isUriAlreadyDisplayed(mainLayout, uri)) {
                addNewConstraintLayout(mainLayout, uri);
            }
        }
    }

    private boolean isUriAlreadyDisplayed(LinearLayout mainLayout, Uri uri) {
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            View child = mainLayout.getChildAt(i);
            if (child instanceof ConstraintLayout) {
                ImageView imageView = child.findViewById(com.PlantMaster.plantmaster.R.id.imageViewHistory);
                if (imageView != null && uri.equals(imageView.getTag())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addNewConstraintLayout(LinearLayout mainLayout, Uri imageUri) {
        ConstraintLayout originalLayout = binding.constraintLayoutHistory;

        ConstraintLayout newConstraintLayout = new ConstraintLayout(context);
        newConstraintLayout.setLayoutParams(originalLayout.getLayoutParams());
        newConstraintLayout.setBackground(originalLayout.getBackground());
        newConstraintLayout.setClickable(true);
        newConstraintLayout.setFocusable(true);

        // Dinamik olarak ImageView oluşturuluyor
        ImageView imageView = new ImageView(context);
        imageView.setId(View.generateViewId());
        imageView.setLayoutParams(binding.imageViewHistory.getLayoutParams());
        imageView.setImageURI(imageUri);
        imageView.setTag(imageUri);  // URI'yi tag olarak ayarlıyoruz
        newConstraintLayout.addView(imageView);

        Uri retrievedImageUri = (Uri) imageView.getTag();

        // TextView'ları ekleme (Plant, Disease, Date)
        TextView textViewPlant = new TextView(context);
        textViewPlant.setId(View.generateViewId());
        textViewPlant.setLayoutParams((ConstraintLayout.LayoutParams) binding.textViewPlant.getLayoutParams());
        textViewPlant.setText("Bitki Adı: " + "Patates");
        textViewPlant.setTextSize(16);
        textViewPlant.setTextColor(Color.BLACK);
        textViewPlant.setAlpha(0f);
        newConstraintLayout.addView(textViewPlant);

        TextView textViewDisease = new TextView(context);
        textViewDisease.setId(View.generateViewId());
        textViewDisease.setLayoutParams((ConstraintLayout.LayoutParams) binding.textViewDisease.getLayoutParams());
        textViewDisease.setText("Hastalık: " + "Kuduz");
        textViewDisease.setTextSize(14);
        textViewDisease.setTextColor(Color.BLACK);
        textViewDisease.setAlpha(0f);
        newConstraintLayout.addView(textViewDisease);

        // Dinamik olarak bugünün tarihini al
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        TextView textViewDate = new TextView(context);
        textViewDate.setId(View.generateViewId());
        textViewDate.setLayoutParams((ConstraintLayout.LayoutParams) binding.textViewDate.getLayoutParams());
        textViewDate.setText("Tarih: " + currentDate);
        textViewDate.setTextSize(12);
        textViewDate.setTextColor(Color.BLACK);
        textViewDate.setAlpha(0f);
        newConstraintLayout.addView(textViewDate);

        // ViewModel'e verileri set et
        sharedViewModel.setPlantName(textViewPlant.getText().toString());
        sharedViewModel.setDisease(textViewDisease.getText().toString());
        sharedViewModel.setDate(textViewDate.getText().toString());



        // ConstraintSet ile layout'u düzenleme
        ConstraintSet set = new ConstraintSet();
        set.clone(newConstraintLayout);

        // ImageView'ı ortalamak ve biraz aşağı taşımak
        set.connect(imageView.getId(), ConstraintSet.START, newConstraintLayout.getId(), ConstraintSet.START);
        set.connect(imageView.getId(), ConstraintSet.END, newConstraintLayout.getId(), ConstraintSet.END);
        set.connect(imageView.getId(), ConstraintSet.TOP, newConstraintLayout.getId(), ConstraintSet.TOP);
        set.setVerticalBias(imageView.getId(), 0.4f); // Daha aşağı almak için bias ayarla

        // TextView'ları konumlandırma
        set.connect(textViewPlant.getId(), ConstraintSet.START, imageView.getId(), ConstraintSet.END);
        set.connect(textViewPlant.getId(), ConstraintSet.TOP, imageView.getId(), ConstraintSet.TOP);

        set.connect(textViewDisease.getId(), ConstraintSet.START, textViewPlant.getId(), ConstraintSet.START);
        set.connect(textViewDisease.getId(), ConstraintSet.TOP, textViewPlant.getId(), ConstraintSet.BOTTOM);

        set.connect(textViewDate.getId(), ConstraintSet.START, textViewDisease.getId(), ConstraintSet.START);
        set.connect(textViewDate.getId(), ConstraintSet.TOP, textViewDisease.getId(), ConstraintSet.BOTTOM);

        set.applyTo(newConstraintLayout);
        mainLayout.addView(newConstraintLayout);
        newConstraintLayout.setVisibility(View.VISIBLE);

        // Animasyonlar
        ObjectAnimator fadeInAnimPlant = ObjectAnimator.ofFloat(textViewPlant, "alpha", 0f, 1f);
        fadeInAnimPlant.setDuration(1000);
        fadeInAnimPlant.start();

        ObjectAnimator fadeInAnimDisease = ObjectAnimator.ofFloat(textViewDisease, "alpha", 0f, 1f);
        fadeInAnimDisease.setDuration(1000);
        fadeInAnimDisease.start();

        ObjectAnimator fadeInAnimDate = ObjectAnimator.ofFloat(textViewDate, "alpha", 0f, 1f);
        fadeInAnimDate.setDuration(1000);
        fadeInAnimDate.start();

        // Yeni ConstraintLayout tıklandığında navigasyona geç
        newConstraintLayout.setOnClickListener(view -> navigateToFragment(view,retrievedImageUri));
    }


    private void navigateToFragment(View view,Uri retrievedImageUri) {
        Bundle bundle = new Bundle();
        bundle.putString("imageUri", retrievedImageUri.toString()); // URI'yi string olarak gönder
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.HistoryDetail,bundle);
    }


}
