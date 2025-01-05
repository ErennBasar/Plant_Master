package com.PlantMaster.plantmaster.ui.profile.profileEdit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentProfileEditBinding;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.PlantMaster.plantmaster.ui.profile.profileEdit.UsernameChangeFragment;

public class ProfileEditFragment extends Fragment {

    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_edit, container, false);

        // Button tanımlamaları
        Button changeUsernameButton = view.findViewById(R.id.changeUsername);
        Button changeEmailButton = view.findViewById(R.id.changeEmail);
        Button changePasswordButton = view.findViewById(R.id.changePassword);

        // Change Username butonu tıklama olayı
        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                UsernameChangeFragment usernameChangeFragment = new UsernameChangeFragment();

                // FrameLayout içine fragment ekleme
                fragmentTransaction.replace(R.id.container_fragment, usernameChangeFragment);
                fragmentTransaction.addToBackStack(null); // Geri tuşu için
                fragmentTransaction.commit();
            }
        });
        // Change Email butonu tıklama olayı
        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                EmailChangeFragment emailChangeFragment = new EmailChangeFragment();

                // FrameLayout içine fragment ekleme
                fragmentTransaction.replace(R.id.container_fragment, emailChangeFragment);
                fragmentTransaction.addToBackStack(null); // Geri tuşu için
                fragmentTransaction.commit();
            }
        });
        // Change Password butonu tıklama olayı
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                PasswordChangeFragment passwordChangeFragment = new PasswordChangeFragment();

                // FrameLayout içine fragment ekleme
                fragmentTransaction.replace(R.id.container_fragment, passwordChangeFragment);
                fragmentTransaction.addToBackStack(null); // Geri tuşu için
                fragmentTransaction.commit();
            }
        });


        return view;
    }

}
