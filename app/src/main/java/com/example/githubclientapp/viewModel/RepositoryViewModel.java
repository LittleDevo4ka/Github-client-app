package com.example.githubclientapp.viewModel;

import com.example.githubclientapp.model.Repository;
import com.example.githubclientapp.model.retrofit.dataСlasses.Branch;
import com.example.githubclientapp.model.retrofit.dataСlasses.RepositoryInfo;
import com.example.githubclientapp.model.retrofit.dataСlasses.User;
import com.example.githubclientapp.model.retrofit.dataСlasses.commits.Commits;

import java.util.List;

public interface RepositoryViewModel {

    void setUserInfo(User data, Integer code);
    void setRepositoriesInfo(List<RepositoryInfo> data, Integer code);
    void setBranches(List<Branch> data, int code);
    void setCommits(List<Commits> data, int code);
}
