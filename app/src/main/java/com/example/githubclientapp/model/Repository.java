package com.example.githubclientapp.model;

import com.example.githubclientapp.model.retrofit.GithubAPIService;
import com.example.githubclientapp.model.retrofit.dataСlasses.User;
import com.example.githubclientapp.viewModel.RepositoryViewModel;

public class Repository {

    private GithubAPIService retrofit = new GithubAPIService(this);
    private RepositoryViewModel viewModel;

    public Repository(RepositoryViewModel repositoryViewModel) {
        viewModel = repositoryViewModel;
    }


    public void setUserInfo(User data, Integer code) {
        viewModel.setUserInfo(data, code);
    }

    public void getAccessToken(String accessToken) {
        retrofit.getAccessToken(accessToken);
    }

    public void getUser() {
        retrofit.getUser();
    }
}
