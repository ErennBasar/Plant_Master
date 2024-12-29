package com.PlantMaster.plantmaster.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.PlantMaster.plantmaster.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileEditFragment extends Fragment {

    private FirebaseAuth auth;
    private EditText editUsername, currentEmailAddress, editEmailAddress, currentPassword, editPassword, editPasswordConfirm;
    private Button editSummitButton;

    public ProfileEditFragment() {
        super(R.layout.fragment_profile_edit);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();

        editUsername = view.findViewById(R.id.editUsername);
        currentEmailAddress = view.findViewById(R.id.currentEmailAddress);
        editEmailAddress = view.findViewById(R.id.editEmailAddress);
        currentPassword = view.findViewById(R.id.currentPassword);
        editPassword = view.findViewById(R.id.editPassword);
        editPasswordConfirm = view.findViewById(R.id.editPasswordConfirm);
        editSummitButton = view.findViewById(R.id.editSummitButton);

        editSummitButton.setOnClickListener(v -> updateUserProfile());
    }

    private void updateUserProfile() {
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "No authenticated user found.", Toast.LENGTH_SHORT).show();
            return;
        }

        String newUsername = editUsername.getText().toString();
        String newEmail = editEmailAddress.getText().toString();
        String newPassword = editPassword.getText().toString();
        String confirmPassword = editPasswordConfirm.getText().toString();

        // Kullanıcı Adını Güncelle
        if (!newUsername.isEmpty()) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newUsername)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Username updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error updating username: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Eposta Güncellemesi
        String currentEmail01 = currentEmailAddress.getText().toString();
        String currentPassword01 = currentPassword.getText().toString();
        String newEmail01 = editEmailAddress.getText().toString();

        verifyAndUpdateEmail(currentEmail01, currentPassword01, newEmail01);

        // Şifre Güncelle
        if (!newPassword.isEmpty() && newPassword.equals(confirmPassword)) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Password updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error updating password: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyAndUpdateEmail(String currentEmail, String currentPassword, String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            Toast.makeText(getContext(), "No authenticated user found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kullanıcıyı yeniden doğrula
        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, currentPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // verifyBeforeUpdateEmail metodu ile yeni e-posta adresine doğrulama gönder
                        user.verifyBeforeUpdateEmail(newEmail)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Toast.makeText(getContext(), "Verification email sent to new email address.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Failed to send verification email: " + updateTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), "Reauthentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
