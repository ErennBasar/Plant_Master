package com.PlantMaster.plantmaster.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.PlantMaster.plantmaster.databinding.FragmentHistoryBinding;
import com.PlantMaster.plantmaster.ui.ImagePickerFragment.SharedViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    private FragmentHistoryBinding binding;
    private HistoryHelper historyHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        historyHelper = new HistoryHelper(requireContext(), binding, sharedViewModel);

        // Firestore'dan geçmiş verileri çek
        loadHistoryFromFirestore();

        // ViewModel'den güncellenmiş veriyi dinle
        sharedViewModel.getHistoryItems().observe(getViewLifecycleOwner(), items -> {
            historyHelper.updateUI(items);
        });

        return root;
    }

    private void loadHistoryFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Kullanıcı oturumda mı kontrol et
        if (auth.getCurrentUser() == null) {
            auth.signInAnonymously().addOnSuccessListener(authResult -> {
                fetchHistoryData(db, auth.getCurrentUser().getUid());
            }).addOnFailureListener(e -> {
                // hata durumu (isteğe bağlı)
            });
        } else {
            fetchHistoryData(db, auth.getCurrentUser().getUid());
        }
    }

    private void fetchHistoryData(FirebaseFirestore db, String uid) {
        db.collection("users")
                .document(uid)
                .collection("history")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> items = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            items.add(document.getData());
                        }

                        // ViewModel'a verileri aktar
                        sharedViewModel.setHistoryItemsFromMapList(items);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}