package com.PlantMaster.plantmaster.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private TextView textViewPlant;
    private TextView textViewDisease;
    private TextView textViewDate;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textViewPlant = root.findViewById(R.id.textViewPlant);
        textViewDisease = root.findViewById(R.id.textViewDisease);
        textViewDate = root.findViewById(R.id.textViewDate);


        ConstraintLayout constraintLayout = binding.constraintLayoutHistory;
        constraintLayout.setOnClickListener(v -> navigateToFragment(root));


        return root;
    }

    private void navigateToFragment(View view) {

        Bundle bundle = new Bundle();
        bundle.putString("plant_name", textViewPlant.getText().toString());
        bundle.putString("disease", textViewDisease.getText().toString());
        bundle.putString("date", textViewDate.getText().toString());

        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.HistoryDetail,bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
