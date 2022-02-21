package com.example.activitypal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.activitypal.databinding.FragmentNavBinding;
import com.example.activitypal.utils.ItemViewModel;

public class NavFragment extends Fragment {
    private static final int REGISTER_ID = 1;
    private static final int LOGIN_ID = 2;
    FragmentNavBinding binding;
    private ItemViewModel viewModel;

    public NavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_nav, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ItemViewModel.class);

        binding.Register.setOnClickListener(view1 -> {
            // this code is just so I can hook up btn event listeners to the main class
            // as long as selectItem is called, main activity will update the "my_container"
            viewModel.selectItem(REGISTER_ID);
        });

        binding.Login.setOnClickListener(view2 -> {
            viewModel.selectItem(LOGIN_ID);
        });
    }
}