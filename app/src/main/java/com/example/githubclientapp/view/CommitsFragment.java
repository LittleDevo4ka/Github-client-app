package com.example.githubclientapp.view;

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
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.githubclientapp.R;
import com.example.githubclientapp.databinding.FragmentCommitsBinding;
import com.example.githubclientapp.model.retrofit.dataСlasses.Branch;
import com.example.githubclientapp.model.retrofit.dataСlasses.RepositoryInfo;
import com.example.githubclientapp.model.retrofit.dataСlasses.commits.Commits;
import com.example.githubclientapp.viewModel.MainViewModel;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class CommitsFragment extends Fragment implements RepositoryRecyclerItem.onItemClickListener {

    private FragmentCommitsBinding binding;
    private MainViewModel viewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCommitsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefreshLayoutCommits.setRefreshing(true);

        navController = Navigation.findNavController(requireView());

        viewModel.stateBranches.observe(getViewLifecycleOwner(), it -> {
            MaterialAutoCompleteTextView editText =
                    (MaterialAutoCompleteTextView) binding.textLayoutCommitsFragment.getEditText();
            if (editText != null) {
                String[] branches = new String[it.size()];
                for (int i = 0; i < it.size(); i++) {
                    branches[i] = it.get(i).name;
                }

                editText.setSimpleItems(branches);
                editText.setText(viewModel.getDefaultBranch(), false);
            }
        });

        viewModel.stateBranchesCode.observe(getViewLifecycleOwner(), it -> {

            switch (it) {
                case 403: {
                    Toast.makeText(requireContext(), "Oops, The number of requests has ended." +
                            "\nWait a couple of minutes.", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 404: {
                    Toast.makeText(requireContext(), "Resource not found: Code 404",
                            Toast.LENGTH_SHORT).show();
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

        viewModel.stateCommitsCode.observe(getViewLifecycleOwner(), it -> {

            switch (it) {
                case 400: {
                    Toast.makeText(requireContext(), "Bad Request: Code 400",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case 403: {
                    Toast.makeText(requireContext(), "Oops, The number of requests has ended." +
                            "\nWait a couple of minutes.", Toast.LENGTH_SHORT).show();
                    break;
                }
                case 404: {
                    Toast.makeText(requireContext(), "Resource not found: Code 404",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case 409: {
                    Toast.makeText(requireContext(), "Conflict: Code 409",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case 500: {
                    Toast.makeText(requireContext(), "Internal Error: Code 500",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                case 600: {
                    Toast.makeText(requireContext(), "Check your internet connection",
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default: break;
            }
        });

        List<Commits> adapterList = new ArrayList<>();
        CommitRecyclerItem myAdapter = new CommitRecyclerItem(adapterList, this);
        binding.commitsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.commitsRecyclerView.setAdapter(myAdapter);

        viewModel.stateCommits.observe(getViewLifecycleOwner(), it -> {
            binding.swipeRefreshLayoutCommits.setRefreshing(false);

            adapterList.clear();
            adapterList.addAll(it);

            myAdapter.notifyDataSetChanged();
        });

        binding.topAppBarCommitsFragment.setTitle(viewModel.RepositoryName);

        binding.topAppBarCommitsFragment.setNavigationOnClickListener(view1 ->
                navController.navigate(R.id.action_commitsFragment_to_homeFragment));

        Objects.requireNonNull(binding.textLayoutCommitsFragment.getEditText())
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        viewModel.getCommits(viewModel.RepositoryIndex, charSequence.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

        binding.swipeRefreshLayoutCommits.setOnRefreshListener(() ->
                viewModel.getBranches(viewModel.RepositoryIndex));
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void uploadNextPage() {
        viewModel.uploadNextCommits();
    }


}