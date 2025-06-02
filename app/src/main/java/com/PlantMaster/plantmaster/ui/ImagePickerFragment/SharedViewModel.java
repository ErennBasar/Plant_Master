package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Uri> currentImageUri = new MutableLiveData<>();
    private final MutableLiveData<List<HistoryItem>> historyItems = new MutableLiveData<>();

    // Diğer metodlar
    public void setImageUri(Uri uri) {
        currentImageUri.setValue(uri);
    }

    public LiveData<Uri> getImageUri() {
        return currentImageUri;
    }

    public void addHistoryItem(Uri imageUri, String plantName, String disease, String date,String treatment) {
        List<HistoryItem> currentList = historyItems.getValue();
        if (currentList != null) {
            currentList.add(0, new HistoryItem(imageUri, plantName, disease, date, treatment));
            historyItems.setValue(currentList);
        }
    }

    public LiveData<List<HistoryItem>> getHistoryItems() {
        return historyItems;
    }

    public void setHistoryItemsFromMapList(List<java.util.Map<String, Object>> items) {
        List<HistoryItem> parsedItems = new ArrayList<>();
        for (Map<String, Object> item : items) {
            String plantName = (String) item.get("plantName");
            String disease = (String) item.get("disease");
            String treatment = (String) item.get("treatment");
            String date = (String) item.get("date");
            String imageUriString = (String) item.get("imageUri");

            Uri imageUri = imageUriString != null ? Uri.parse(imageUriString) : null;

            parsedItems.add(new HistoryItem(imageUri, plantName, disease, date, treatment));
        }
        historyItems.setValue(parsedItems);
    }

    public static class HistoryItem {
        public final Uri imageUri;
        public final String plantName;
        public final String disease;
        public final String date;
        public final String treatment;

        public HistoryItem(Uri imageUri, String plantName, String disease, String date,String treatment) {
            this.imageUri = imageUri;
            this.plantName = plantName;
            this.disease = disease;
            this.date = date;
            this.treatment=treatment;
        }
    }
}