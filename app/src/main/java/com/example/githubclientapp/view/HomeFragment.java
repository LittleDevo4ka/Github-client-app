package com.example.githubclientapp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.githubclientapp.R;
import com.example.githubclientapp.databinding.FragmentHomeBinding;
import com.example.githubclientapp.viewModel.MainViewModel;

import java.util.Objects;


public class HomeFragment extends Fragment {

    private String tag = "HomeFragment";
    private FragmentHomeBinding binding;
    private MainViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.stateUserInfo.observe(getViewLifecycleOwner(), user -> {
            Log.i(tag, user.name);

            binding.userNameHome.setText(user.name);
            binding.userBioHome.setText(user.bio);
            binding.userFollowersHome.setText("Followers: " + user.followers);
            binding.userFollowingHome.setText("Following: " + user.following);

            Glide.with(requireContext()).load(user.avatar_url)
                    .placeholder(R.drawable.placeholder)
                    .into(binding.profileImageHome);
        });

    }
}