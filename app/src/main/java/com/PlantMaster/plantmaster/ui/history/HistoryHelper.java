package com.PlantMaster.plantmaster.ui.history;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
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

    public HistoryHelper(Context context, FragmentHistoryBinding binding,
                         SharedViewModel sharedViewModel) {
        this.context = context;
        this.binding = binding;
        this.sharedViewModel = sharedViewModel;
    }

    public void updateUI(List<SharedViewModel.HistoryItem> items) {
        if (items == null || items.isEmpty()) {
            binding.HistoryLinearLayout.removeAllViews();
            return;
        }

        LinearLayout mainLayout = binding.HistoryLinearLayout;
        mainLayout.removeAllViews();

        for (SharedViewModel.HistoryItem item : items) {
            addHistoryItem(mainLayout, item);
        }
    }

    private void addHistoryItem(LinearLayout parent, SharedViewModel.HistoryItem item) {
        // Inflate the item layout
        ConstraintLayout itemLayout = (ConstraintLayout) View.inflate(
                context,
                R.layout.item_history,
                null
        );


        ImageView imageView = itemLayout.findViewById(R.id.imageViewHistory);
        TextView tvPlant = itemLayout.findViewById(R.id.tvPlantName);
        TextView tvDisease = itemLayout.findViewById(R.id.tvDisease);
        TextView tvDate = itemLayout.findViewById(R.id.tvDate);

        try {

            imageView.setImageURI(item.imageUri);
            tvPlant.setText(context.getString(R.string.plant_label, item.plantName));
            tvDisease.setText(context.getString(R.string.disease_label, item.disease));
            tvDate.setText(item.date);


            itemLayout.setOnClickListener(v -> navigateToDetail(v, item));

            parent.addView(itemLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToDetail(View view, SharedViewModel.HistoryItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("imageUri", item.imageUri.toString());
        bundle.putString("plantName", item.plantName);
        bundle.putString("disease", item.disease);
        bundle.putString("date", item.date);
        bundle.putString("treatment", item.treatment);

        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.HistoryDetail, bundle);
    }
}