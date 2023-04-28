package com.example.githubclientapp.model;

import android.content.Context;

import com.example.githubclientapp.model.retrofit.GithubAPIService;
import com.example.githubclientapp.model.retrofit.dataСlasses.Branch;
import com.example.githubclientapp.model.retrofit.dataСlasses.RepositoryInfo;
import com.example.githubclientapp.model.retrofit.dataСlasses.User;
import com.example.githubclientapp.model.retrofit.dataСlasses.commits.Commits;
import com.example.githubclientapp.viewModel.RepositoryViewModel;

import java.util.List;

public class Repository {

    private final GithubAPIService retrofit;
    private final RepositoryViewModel viewModel;

    public Repository(RepositoryViewModel repositoryViewModel, Context context) {
        viewModel = repositoryViewModel;
        retrofit = new GithubAPIService(this, context);
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

    public void getRepositories(Integer pageSize, Integer pageNum) {
        retrofit.getRepositories(pageSize, pageNum);
    }

    public void setRepositoriesInfo(List<RepositoryInfo> data, Integer code) {
        viewModel.setRepositoriesInfo(data, code);
    }

    public void getRepositoryBranches(String owner, String repositoryName,
                                      Integer pageSize, Integer pageNum) {
        retrofit.getRepositoryBranches(owner, repositoryName, pageSize, pageNum);
    }

    public void sendBranches(List<Branch> data, int code) {
        viewModel.setBranches(data, code);
    }

    public void getCommits(String owner, String repositoryName, String branch, Integer pageSize, Integer pageNum) {
        retrofit.getRepositoryCommits(owner, repositoryName, branch, pageSize, pageNum);
    }

    public void sendCommits(List<Commits> data, int code) {
        viewModel.setCommits(data, code);
    }

    public boolean AccessTokenNotNull() {
        return retrofit.AccessTokenNotNull();
    }

}
