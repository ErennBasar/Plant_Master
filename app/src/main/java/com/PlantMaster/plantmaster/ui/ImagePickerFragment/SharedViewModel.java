package com.PlantMaster.plantmaster.ui.ImagePickerFragment;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Uri> imageUri = new MutableLiveData<>();
    private final MutableLiveData<String> plantName = new MutableLiveData<>();
    private final MutableLiveData<String> disease = new MutableLiveData<>();
    private final MutableLiveData<String> date = new MutableLiveData<>();

    // Setters
    public void setPlantName(String name) {
        plantName.setValue(name);
    }

    public void setDisease(String disease) {
        this.disease.setValue(disease);
    }

    public void setDate(String date) {
        this.date.setValue(date);
    }

    // Getters
    public LiveData<String> getPlantName() {
        return plantName;
    }

    public LiveData<String> getDisease() {
        return disease;
    }

    public LiveData<String> getDate() {
        return date;
    }
    public void setImageUri(Uri uri) {
        imageUri.setValue(uri);
    }

    public LiveData<Uri> getImageUri() {
        return imageUri;
    }
}
