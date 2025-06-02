package com.PlantMaster.plantmaster.ui.profile;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.PlantMaster.plantmaster.R;
import com.PlantMaster.plantmaster.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private FragmentProfileBinding binding;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ProfileViewModel profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textView;
        profileViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mAuth = FirebaseAuth.getInstance();

        // Kullanıcı oturum açmış mı kontrol et
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Eğer kullanıcı giriş yapmışsa, ProfileDetailFragment'e yönlendir
            navigateToProfileDetail();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        // Gmail butonuna tıklama işlemi
        binding.loginWithGoogleButton.setOnClickListener(v -> signInWithGoogle());

        // Butonlara tıklama olayları bağlandı
        binding.loginButton.setOnClickListener(this::loginClicked);
        binding.forgetPasswordText.setOnClickListener(this::forgetPasswordClicked);
        binding.loginGuestButton.setOnClickListener(this::loginGuestButtonClicked);

        String fullText = getString(R.string.login_message);
        SpannableString spannableString = new SpannableString(fullText);


        int signUpStart = fullText.indexOf("Sign up");
        int signUpEnd = signUpStart + "Sign up".length();


        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.SignupFragment);
            }
        }, signUpStart, signUpEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.green_500)),
                signUpStart, signUpEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        spannableString.setSpan(new UnderlineSpan(),
                signUpStart, signUpEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        return root;
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getActivity(), "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        navigateToProfileDetail();
                    } else {
                        Toast.makeText(getActivity(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void loginGuestButtonClicked(View view){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.profileDetailFragment);
    }
    public void loginClicked(View view) {
        String email = binding.emailInput.getText().toString();
        String password = binding.passwordInput.getText().toString();

        // Email ve şifre boş mu kontrolü
        if (email.isEmpty()) {
            binding.emailInput.setError("Email is required!");
            return;
        }
        if (password.isEmpty()) {
            binding.passwordInput.setError("Password is required!");
            return;
        }

        // Şifre uzunluğunu kontrol
        if (password.length() < 6) {
            binding.passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        // Firebase ile kullanıcı giriş işlemi
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // Giriş başarılı, ProfileDetailFragment'e yönlendir
                    navigateToProfileDetail();
                })
                .addOnFailureListener(e -> {
                    // Hata mesajını göster
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });
    }
    public void forgetPasswordClicked (View view){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.ResetPasswordFragment);
    }

    // ProfileDetailFragment'e yönlendirme metodu
    private void navigateToProfileDetail() {
        // Navigation işlemi
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.profileDetailFragment);
    }

    public void signupClicked(View view) {
        String email = binding.emailInput.getText().toString();
        String password = binding.passwordInput.getText().toString();

        if (email.isEmpty()) {
            binding.emailInput.setError("Email is required!");
            return;
        }
        if (password.isEmpty()) {
            binding.passwordInput.setError("Password is required!");
            return;
        }

        if (password.length() < 6) {
            binding.passwordInput.setError("Password must be at least 6 characters");
            return;
        }

        // Firebase ile kullanıcı oluşturma
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            // Başarılı olduğunda yapılacak işlemler
            Toast.makeText(getActivity(), "Sign Up Successful!", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(e -> {
            // Başarısız olduğunda yapılacak işlemler
            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
