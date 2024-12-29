package com.PlantMaster.plantmaster;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.PlantMaster.plantmaster.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding ile layout'u bağla
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Navigation View (BottomNavigationView) referansı
        BottomNavigationView navView = binding.navView;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Destination değiştikçe menüyü göster veya gizle
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            if (destination.getId() == R.id.AfterCamera || destination.getId() == R.id.SignupFragment
                    || destination.getId() == R.id.FirstFragment)
            {
                navView.setVisibility(View.GONE);
                getWindow().setBackgroundDrawableResource(android.R.color.white);
            } else if (destination.getId() == R.id.navigation_camera) {
                navView.setVisibility(View.VISIBLE);
                getWindow().setBackgroundDrawableResource(android.R.color.black);
            } else {
                navView.setVisibility(View.VISIBLE);
                getWindow().setBackgroundDrawableResource(android.R.color.white);
            }

        });

        // AppBar ve NavController'ı yapılandır
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_camera, R.id.navigation_profile)
                .build();


        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}