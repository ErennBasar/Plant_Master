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
            // navigation_profile veya SignupFragment dışında diğer fragment'lerde menüyü görünür yap
            if (destination.getId() == R.id.navigation_profile || destination.getId() == R.id.SignupFragment) {
                navView.setVisibility(View.GONE);
                // AppCompatActivity'yi kullanarak ActionBar'ı gizle
                ((AppCompatActivity) MainActivity.this).getSupportActionBar().hide();
            } else {
                navView.setVisibility(View.VISIBLE); // Diğerlerinde göster
                // ActionBar'ı tekrar göster
                ((AppCompatActivity) MainActivity.this).getSupportActionBar().show();
            }
        });



        // AppBar ve NavController'ı yapılandır
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_camera, R.id.navigation_profile)
                .build();


        // ActionBar ve BottomNavigationView ile NavController'ı bağla
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}