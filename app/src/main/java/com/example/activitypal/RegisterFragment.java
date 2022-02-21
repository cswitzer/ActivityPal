package com.example.activitypal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.activitypal.databinding.FragmentRegisterBinding;
import com.example.activitypal.utils.APICallHandler;
import com.example.activitypal.utils.SharedPrefsHandler;

public class RegisterFragment extends Fragment {
    FragmentRegisterBinding binding;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        binding.regButton.setOnClickListener(view -> {
            // NEVER TRUST THE USER!!! VALIDATE AND SANITIZE USER INPUT YOU MADMAN!!!
            String email = binding.email.getText().toString().trim();
            String password = binding.password.getText().toString();
            // TODO: make an api call
            SharedPrefsHandler.SaveUserCred(getActivity(), email, password);
            APICallHandler.HandleRegistration(getActivity(), email, password);
        });
        View view = binding.getRoot();
        return view;
    }
}