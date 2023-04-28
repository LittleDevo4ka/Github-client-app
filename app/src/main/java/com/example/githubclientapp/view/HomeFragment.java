package com.example.githubclientapp.view;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.githubclientapp.R;
import com.example.githubclientapp.databinding.FragmentHomeBinding;
import com.example.githubclientapp.databinding.RepositoryCardBinding;
import com.example.githubclientapp.model.retrofit.dataСlasses.RepositoryInfo;
import com.example.githubclientapp.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements RepositoryRecyclerItem.onItemClickListener {

    private String tag = "HomeFragment";
    private FragmentHomeBinding binding;
    private MainViewModel viewModel;
    private NavController navController;
    private boolean onBackPressed = false;

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
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (onBackPressed) {
                    System.exit(0);
                } else {
                    Toast.makeText(requireContext(), "Press again to exit",
                            Toast.LENGTH_SHORT).show();
                    onBackPressed = true;
                }
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        binding.swipeRefreshLayoutHome.setRefreshing(true);

        viewModel.stateUserCode.observe(getViewLifecycleOwner(), it -> {
            switch (it) {
                case 304: {
                    Toast.makeText(requireContext(), "Not modified: Code 304",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case 401: {
                    Toast.makeText(requireContext(), "Oops, the access token has expired." +
                            "\nYou need to log in again.", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_homeFragment_to_splashFragment);
                    break;
                }
                case 403: {
                    Toast.makeText(requireContext(), "Oops, The number of requests has ended." +
                            "\nWait a couple of minutes.", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 500: {
                    Toast.makeText(requireContext(), "Check your internet connection",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default: break;
            }
        });

        viewModel.stateRepositoriesCode.observe(getViewLifecycleOwner(), it -> {
            switch (it) {
                case 304: {
                    Toast.makeText(requireContext(), "Not modified: Code 304",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case 403: {
                    Toast.makeText(requireContext(), "Oops, The number of requests has ended." +
                            "\nWait a couple of minutes.", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 422: {
                    Toast.makeText(requireContext(), "Validation failed, or the endpoint" +
                                    " has been spammed: Code 422", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 500: {
                    Toast.makeText(requireContext(), "Check your internet connection",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default: break;
            }

        });

        List<RepositoryInfo> adapterList = new ArrayList<>();
        RepositoryRecyclerItem myAdapter = new RepositoryRecyclerItem(adapterList, requireContext(),
                this);
        binding.repositoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.repositoriesRecyclerView.setAdapter(myAdapter);

        viewModel.stateUserInfo.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                binding.userNameHome.setText(user.name);
                binding.userBioHome.setText(user.bio);
                binding.userFollowersHome.setText("Followers: " + user.followers);
                binding.userFollowingHome.setText("Following: " + user.following);

                Glide.with(requireContext()).load(user.avatar_url)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.profileImageHome);
            }
        });

        viewModel.stateRepositories.observe(getViewLifecycleOwner(), it -> {
            binding.swipeRefreshLayoutHome.setRefreshing(false);

            adapterList.clear();
            adapterList.addAll(it);

            myAdapter.notifyDataSetChanged();
        });

        navController = Navigation.findNavController(requireView());

        binding.swipeRefreshLayoutHome.setOnRefreshListener(() -> {
            viewModel.getUser();
            viewModel.getRepositories();
        });

    }

    @Override
    public void onItemClick(int position) {
        viewModel.getBranches(position);
        viewModel.getCommits(position, "");
        navController.navigate(R.id.action_homeFragment_to_commitsFragment);
    }

    @Override
    public void uploadNextPage() {
        viewModel.uploadNextRepositories();
    }
}