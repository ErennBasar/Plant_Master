package com.PlantMaster.plantmaster.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.PlantMaster.plantmaster.databinding.FragmentHistoryBinding;
import com.PlantMaster.plantmaster.ui.ImagePickerFragment.SharedViewModel;

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

        sharedViewModel.getHistoryItems().observe(getViewLifecycleOwner(), items -> {
            historyHelper.updateUI(items);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}