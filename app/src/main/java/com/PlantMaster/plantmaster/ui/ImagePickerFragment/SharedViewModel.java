package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Uri> imageUri = new MutableLiveData<>();
    private final MutableLiveData<List<Uri>> imageUriList = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<String> plantName = new MutableLiveData<>();
    private final MutableLiveData<String> disease = new MutableLiveData<>();
    private final MutableLiveData<String> date = new MutableLiveData<>();

    private final MutableLiveData<String> treatment = new MutableLiveData<>();

    // Setters for single values
    public void setPlantName(String name) {
        plantName.setValue(name);
    }

    public void setDisease(String disease) {
        this.disease.setValue(disease);
    }

    public void setDate(String date) {
        this.date.setValue(date);
    }

    public void setTreatment(String treatment) {
        this.treatment.setValue(treatment);
    }

    public void setImageUri(Uri uri) {
        imageUri.setValue(uri);
        // Automatically add the single URI to the list if needed
        addImageUri(uri);
    }

    // Add URI to the list
    // Listeye yeni bir fotoğraf eklemek
    public void addImageUri(Uri uri) {
        List<Uri> currentList = imageUriList.getValue();
        if (currentList != null) {
            currentList.add(uri);
            imageUriList.setValue(currentList);
        }
    }



    // Getters for single values
    public LiveData<String> getPlantName() {
        return plantName;
    }

    public LiveData<String> getDisease() {
        return disease;
    }

    public LiveData<String> getDate() {
        return date;
    }

    public LiveData<String> getTreatment() {
        return treatment;
    }
    public LiveData<Uri> getImageUri() {
        return imageUri;
    }

    // Getter for the list
    public LiveData<List<Uri>> getImageUriList() {
        return imageUriList;
    }
}
