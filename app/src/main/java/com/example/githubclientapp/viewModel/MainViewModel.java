package com.example.githubclientapp.viewModel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.githubclientapp.model.Repository;
import com.example.githubclientapp.model.retrofit.dataСlasses.Branch;
import com.example.githubclientapp.model.retrofit.dataСlasses.RepositoryInfo;
import com.example.githubclientapp.model.retrofit.dataСlasses.User;
import com.example.githubclientapp.model.retrofit.dataСlasses.commits.Commits;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainViewModel extends AndroidViewModel implements RepositoryViewModel {

    private final String tag = "MainViewModel";
    private final Repository repository = new Repository(this, getApplication());

    private final MutableLiveData<User> mutableUserInfo = new MutableLiveData<>();
    public LiveData<User> stateUserInfo = mutableUserInfo;
    private final MutableLiveData<Integer> mutableUserCode = new MutableLiveData<>(-1);
    public LiveData<Integer> stateUserCode = mutableUserCode;

    private final List<RepositoryInfo> repositoriesList = new ArrayList<>();
    private final MutableLiveData<List<RepositoryInfo>> mutableRepositories = new MutableLiveData<>(repositoriesList);
    public LiveData<List<RepositoryInfo>> stateRepositories = mutableRepositories;
    private final MutableLiveData<Integer> mutableRepositoriesCode = new MutableLiveData<>(-1);
    public LiveData<Integer> stateRepositoriesCode = mutableRepositoriesCode;
    private int repositoriesPage = 1;
    private boolean repositoriesLastPage = false;

    private final List<Branch> branchesList = new ArrayList<>();
    private final MutableLiveData<List<Branch>> mutableBranches = new MutableLiveData<>(branchesList);
    public LiveData<List<Branch>> stateBranches = mutableBranches;
    private final MutableLiveData<Integer> mutableBranchesCode = new MutableLiveData<>(-1);
    public LiveData<Integer> stateBranchesCode = mutableBranchesCode;
    private int branchesPage = 1;
    private boolean branchesLastPage = false;

    private final List<Commits> commitsList = new ArrayList<>();
    private final MutableLiveData<List<Commits>> mutableCommits = new MutableLiveData<>(commitsList);
    public LiveData<List<Commits>> stateCommits = mutableCommits;
    private final MutableLiveData<Integer> mutableCommitsCode = new MutableLiveData<>(-1);
    public LiveData<Integer> stateCommitsCode = mutableCommitsCode;
    private int commitsPage = 1;
    private boolean commitsLastPage = false;
    private String commitsBranch = "";

    public String RepositoryName = "";
    public int RepositoryIndex = -1;

    public MainViewModel(@NonNull Application application) {
        super(application);

        Log.i(tag, "create viewModel");
    }

    public void getAccessToken(String accessToken) {
        repository.getAccessToken(accessToken);
    }

    public void getUser() {
        RepositoryIndex = -1;
        RepositoryName = "";

        repository.getUser();
    }

    @Override
    public void setUserInfo(User data, Integer code) {
        mutableUserInfo.setValue(data);
        mutableUserCode.setValue(code);
    }

    public void getRepositories() {
        repositoriesPage = 1;
        repositoriesLastPage = false;
        RepositoryIndex = -1;
        RepositoryName = "";

        repository.getRepositories(5, repositoriesPage);
    }

    public void uploadNextRepositories() {
        if (repositoriesLastPage) return;
        repositoriesPage++;
        repository.getRepositories(5, repositoriesPage);
    }

    @Override
    public void setRepositoriesInfo(List<RepositoryInfo> data, Integer code) {
        if (data != null) {
            if (repositoriesPage == 1) {
                repositoriesList.clear();
            } else if (data.size() < 5) {
                repositoriesLastPage = true;
            }
            repositoriesList.addAll(data);
        } else {
            repositoriesLastPage = true;
            repositoriesList.clear();
        }
        mutableRepositories.setValue(repositoriesList);
        mutableRepositoriesCode.setValue(code);
    }

    public void getBranches(int repositoryIndex) {
        String repositoryName = repositoriesList.get(repositoryIndex).name;
        String repositoryOwner = repositoriesList.get(repositoryIndex).owner.login;

        branchesPage = 1;
        branchesLastPage = false;
        repository.getRepositoryBranches(Objects.requireNonNull(repositoryOwner), repositoryName,
                100, branchesPage);

        RepositoryName = repositoryName;
        RepositoryIndex = repositoryIndex;
    }

    private void uploadNextBranches() {
        if (!branchesLastPage && RepositoryIndex != -1) {
            branchesPage++;
            String repositoryName = repositoriesList.get(RepositoryIndex).name;
            String repositoryOwner = repositoriesList.get(RepositoryIndex).owner.login;

            repository.getRepositoryBranches(Objects.requireNonNull(repositoryOwner), repositoryName,
                    100, branchesPage);
        }
    }

    @Override
    public void setBranches(List<Branch> data, int code) {
        if (data != null) {
            if (branchesPage == 1) {
                branchesList.clear();
            }
            if (data.size() < 100) {
                branchesLastPage = true;
            } else {
                uploadNextBranches();
            }
            branchesList.addAll(data);
        } else {
            branchesLastPage = true;
            branchesList.clear();
        }
        mutableBranches.setValue(branchesList);
        mutableBranchesCode.setValue(code);
    }

    public void getCommits(int repositoryIndex, String branch) {
        commitsPage = 1;
        commitsLastPage = false;

        String repositoryName = repositoriesList.get(repositoryIndex).name;
        String repositoryOwner = repositoriesList.get(repositoryIndex).owner.login;
        if (Objects.equals(branch, "")) {
            commitsBranch = repositoriesList.get(repositoryIndex).default_branch;
        } else {
            commitsBranch = branch;
        }
        repository.getCommits(repositoryOwner, repositoryName, commitsBranch, 10, commitsPage);
    }

    public void uploadNextCommits() {
        if (RepositoryIndex != -1 && !commitsLastPage) {
            commitsPage++;
            String repositoryName = repositoriesList.get(RepositoryIndex).name;
            String repositoryOwner = repositoriesList.get(RepositoryIndex).owner.login;

            repository.getCommits(repositoryOwner, repositoryName, commitsBranch, 10, commitsPage);
        }
    }

    @Override
    public void setCommits(List<Commits> data, int code) {
        if (data != null) {
            if (commitsPage == 1) {
                commitsList.clear();
            }
            if (data.size() < 10) {
                commitsLastPage = true;
            }
            commitsList.addAll(data);
        } else {
            commitsLastPage = true;
            commitsList.clear();
        }
        mutableCommits.setValue(commitsList);
        mutableCommitsCode.setValue(code);
    }

    public boolean AccessTokenNotNull() {
        return repository.AccessTokenNotNull();
    }

    public String getDefaultBranch() {
        if (RepositoryIndex != -1) {
            return repositoriesList.get(RepositoryIndex).default_branch;
        }
        else return "";
    }

}
