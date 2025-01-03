package com.PlantMaster.plantmaster.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.PlantMaster.plantmaster.R;

public class HistoryDetailFragment extends Fragment {


    private TextView textViewPlant;
    private TextView textViewDisease;
    private TextView textViewDate;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_history_detail, container, false);

        textViewPlant = root.findViewById(R.id.textViewPlant);
        textViewDisease = root.findViewById(R.id.textViewDisease);
        textViewDate = root.findViewById(R.id.textViewDate);



        Bundle bundle = getArguments();
        if (bundle != null) {
            String plantName = bundle.getString("plant_name");
            String disease = bundle.getString("disease");
            String date = bundle.getString("date");


            // UI bileşenlerine veriyi atıyoruz
            textViewPlant.setText(plantName);
            textViewDisease.setText(disease);
            textViewDate.setText(date);
        }

        return root;
    }
}
