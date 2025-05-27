package com.PlantMaster.plantmaster;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.PlantMaster.plantmaster.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Geri tuşunu (back button + swipe gesture) devre dışı bırak
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Geri gitme engellendi
            }
        });

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        // Fullscreen mod (status barı da gizle)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        // ActionBar'ı gizle
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Binding ile layout'u bağla
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Kullanıcıyı giriş yapmış mı kontrol et

        // Navigation View (BottomNavigationView) referansı
        BottomNavigationView navView = binding.navView;

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Kullanıcı giriş yaptıysa profileDetailFragment'e yönlendir
        if (currentUser != null && savedInstanceState == null) {
            navController.navigate(R.id.profileDetailFragment,
                    null,
                    new NavOptions.Builder()
                            .setPopUpTo(R.id.FirstFragment, true) // Sadece FirstFragment'i stack'ten sil
                            .build()
            );
        }

        // Destination değiştikçe menüyü göster veya gizle
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            if ( destination.getId() == R.id.navigation_profile
                    || destination.getId() == R.id.SignupFragment
                    || destination.getId() == R.id.FirstFragment
                    || destination.getId() == R.id.ResetPasswordFragment
                    || destination.getId() == R.id.usernameChangeFragment
                    || destination.getId() == R.id.passwordChangeFragment
                    || destination.getId() == R.id.emailChangeFragment
                    || destination.getId() == R.id.ProfileEditFragment) {
                navView.setVisibility(View.GONE);
                getWindow().setBackgroundDrawableResource(android.R.color.white);

            }
            else if (destination.getId() == R.id.ImageResult) {
                navView.setVisibility(View.GONE);
                getWindow().setBackgroundDrawableResource(R.drawable.gradient_btn);
            }

            else if (destination.getId() == R.id.ImagePickerFragment || destination.getId() == R.id.navigation_home ||
                    destination.getId() == R.id.HistoryDetail) {
                navView.setVisibility(View.VISIBLE);
                getWindow().getDecorView().setBackgroundColor(
                        ContextCompat.getColor(this, R.color.pastelGreen)
                );
            } else {
                navView.setVisibility(View.VISIBLE);
                getWindow().setBackgroundDrawableResource(android.R.color.white);
            }

        });

        // AppBar ve NavController'ı yapılandır
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.ImagePickerFragment, R.id.navigation_profile)
                .build();


        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}