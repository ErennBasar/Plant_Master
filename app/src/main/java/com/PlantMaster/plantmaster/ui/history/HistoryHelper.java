package com.PlantMaster.plantmaster.ui.history;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentHistoryBinding;
import com.PlantMaster.plantmaster.ui.ImagePickerFragment.SharedViewModel;


import java.util.List;


public class HistoryHelper {

    private final Context context;
    private final FragmentHistoryBinding binding;
    private final SharedViewModel sharedViewModel;
    private final LifecycleOwner lifecycleOwner;

    public HistoryHelper(Context context, FragmentHistoryBinding binding, SharedViewModel sharedViewModel, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.binding = binding;
        this.sharedViewModel = sharedViewModel;
        this.lifecycleOwner = lifecycleOwner;
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
        ConstraintLayout newConstraintLayout = new ConstraintLayout(context);
        ConstraintLayout originalLayout = binding.constraintLayoutHistory;

        newConstraintLayout.setLayoutParams(originalLayout.getLayoutParams());
        newConstraintLayout.setBackground(originalLayout.getBackground());
        newConstraintLayout.setClickable(true);
        newConstraintLayout.setFocusable(true);


        ImageView imageView = createImageView(imageUri);
        newConstraintLayout.addView(imageView);

        TextView textViewPlant = createTextView("", 16, Color.BLACK, 0f);
        newConstraintLayout.addView(textViewPlant);

        TextView textViewDisease = createTextView("", 14, Color.BLACK, 0f);
        newConstraintLayout.addView(textViewDisease);

        TextView textViewDate = createTextView("", 12, Color.BLACK, 0f);
        newConstraintLayout.addView(textViewDate);

        sharedViewModel.getPlantName().observe(lifecycleOwner, plantName -> {
            textViewPlant.setText("Plant Name: " + plantName);
        });

        sharedViewModel.getDisease().observe(lifecycleOwner, disease -> {
            textViewDisease.setText("Plant Disease: " + disease);
        });

        sharedViewModel.getDate().observe(lifecycleOwner, date -> {
            textViewDate.setText(date);
        });

        setupLayoutConstraints(newConstraintLayout, imageView, textViewPlant, textViewDisease, textViewDate);


        animateTextView(textViewPlant);
        animateTextView(textViewDisease);
        animateTextView(textViewDate);


        newConstraintLayout.setOnClickListener(view -> navigateToFragment(view, imageUri, textViewPlant, textViewDisease, textViewDate));
        mainLayout.addView(newConstraintLayout);
    }


    private TextView createTextView(String text, int textSize, int textColor, float alpha) {
        TextView textView = new TextView(context);
        textView.setId(View.generateViewId());
        textView.setText(text);
        textView.setTextSize(textSize);
        textView.setTextColor(textColor);
        textView.setAlpha(alpha);
        return textView;
    }


    private ImageView createImageView(Uri imageUri) {
        ImageView imageView = new ImageView(context);
        imageView.setId(View.generateViewId());
        imageView.setImageURI(imageUri);
        imageView.setTag(imageUri);
        return imageView;
    }

    private void setupLayoutConstraints(ConstraintLayout newLayout, ImageView imageView, TextView textViewPlant, TextView textViewDisease, TextView textViewDate) {
        ConstraintSet set = new ConstraintSet();
        set.clone(newLayout);


        set.connect(imageView.getId(), ConstraintSet.START, newLayout.getId(), ConstraintSet.START);
        set.connect(imageView.getId(), ConstraintSet.TOP, newLayout.getId(), ConstraintSet.TOP);
        set.setVerticalBias(imageView.getId(), 0.6f);


        set.connect(textViewPlant.getId(), ConstraintSet.START, imageView.getId(), ConstraintSet.END);
        set.connect(textViewPlant.getId(), ConstraintSet.TOP, imageView.getId(), ConstraintSet.TOP);
        set.setMargin(textViewPlant.getId(), ConstraintSet.START, 15);

        set.connect(textViewDisease.getId(), ConstraintSet.START, textViewPlant.getId(), ConstraintSet.START);
        set.connect(textViewDisease.getId(), ConstraintSet.TOP, textViewPlant.getId(), ConstraintSet.BOTTOM);
        set.setMargin(textViewDisease.getId(), ConstraintSet.TOP, 15);

        set.connect(textViewDate.getId(), ConstraintSet.START, textViewDisease.getId(), ConstraintSet.START);
        set.connect(textViewDate.getId(), ConstraintSet.TOP, textViewDisease.getId(), ConstraintSet.BOTTOM);
        set.setMargin(textViewDate.getId(), ConstraintSet.TOP, 20);

        set.applyTo(newLayout);
    }



    private void animateTextView(TextView textView) {
        ObjectAnimator fadeInAnim = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
        fadeInAnim.setDuration(1000);
        fadeInAnim.start();
    }


    private void navigateToFragment(View view, Uri imageUri, TextView textViewPlant, TextView textViewDisease, TextView textViewDate) {
        Bundle bundle = new Bundle();
        bundle.putString("imageUri", imageUri.toString());
        bundle.putString("plantName", textViewPlant.getText().toString());
        bundle.putString("disease", textViewDisease.getText().toString());
        bundle.putString("date", textViewDate.getText().toString());

        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.HistoryDetail, bundle);
    }

}
