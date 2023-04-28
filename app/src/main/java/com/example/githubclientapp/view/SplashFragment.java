package com.example.githubclientapp.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.githubclientapp.BuildConfig;
import com.example.githubclientapp.R;
import com.example.githubclientapp.databinding.FragmentHomeBinding;
import com.example.githubclientapp.databinding.FragmentSplashBinding;
import com.example.githubclientapp.viewModel.MainViewModel;


public class SplashFragment extends Fragment {
    private final String tag = "SplashFragment";
    private FragmentSplashBinding binding;
    private MainViewModel viewModel;
    private NavController navController;
    private final String redirectURL = "githubclientapp://callback";
    private final String logInUri = "https://github.com/login/oauth/authorize?client_id="
            + BuildConfig.Client_ID + "&scope=repo&redirect_uri=" + redirectURL;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSplashBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(requireView());

        if (viewModel.AccessTokenNotNull()) {
            viewModel.getUser();
            viewModel.getRepositories();
            navController.navigate(R.id.action_splashFragment_to_homeFragment);
        }

        binding.logInButton.setOnClickListener(view1 -> {
            if (!viewModel.AccessTokenNotNull()){
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(logInUri));
                startActivity(intent);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (!viewModel.AccessTokenNotNull()) {

            Uri uri = requireActivity().getIntent().getData();

            if (uri != null && uri.toString().startsWith(redirectURL)) {
                String code = uri.getQueryParameter("code");
                viewModel.getAccessToken(code);

                navController.navigate(R.id.action_splashFragment_to_homeFragment);
            }
        }
    }
}