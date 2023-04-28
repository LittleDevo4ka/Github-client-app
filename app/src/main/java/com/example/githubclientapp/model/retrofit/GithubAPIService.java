package com.example.githubclientapp.model.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.githubclientapp.BuildConfig;
import com.example.githubclientapp.model.Repository;
import com.example.githubclientapp.model.retrofit.dataСlasses.AccessToken;
import com.example.githubclientapp.model.retrofit.dataСlasses.Branch;
import com.example.githubclientapp.model.retrofit.dataСlasses.RepositoryInfo;
import com.example.githubclientapp.model.retrofit.dataСlasses.User;
import com.example.githubclientapp.model.retrofit.dataСlasses.commits.Commits;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubAPIService {
    private final String tag = "GithubAPIService";
    private final Repository mainRepository;
    private final String baseURL = "https://github.com/login/oauth/authorize";
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private final GithubAPI retrofit = new Retrofit.Builder()
            .baseUrl("https://github.com/login/oauth/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GithubAPI.class);

    private String accessToken;
    private String tokenType;

    private final SharedPreferences saveInfo;
    public GithubAPIService(Repository repository, Context context) {
        mainRepository = repository;
        saveInfo = context.getSharedPreferences("saveInfo", Context.MODE_PRIVATE);
        accessToken = saveInfo.getString("accessToken", null);
        tokenType = saveInfo.getString("tokenType", null);
    }

    public boolean AccessTokenNotNull() {
        return accessToken != null;
    }

    public void getAccessToken(String code) {
        Call<AccessToken> call =
                retrofit.getAccessToken(BuildConfig.Client_ID, BuildConfig.Client_secret, code);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i(tag, "getAccessToken: OK");
                        accessToken = response.body().getAccessToken();
                        saveInfo.edit().putString("accessToken", accessToken).apply();
                        tokenType = response.body().getTokenType();
                        saveInfo.edit().putString("tokenType", tokenType).apply();

                        getUser();
                        getRepositories(5, 1);
                    }
                } else {
                    Log.w(tag,"getAccessToken: Something went wrong " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                Log.w(tag,"getAccessToken: Something really went wrong " + t.getMessage());
            }
        });
    }

    public void getUser() {
        if (accessToken != null) {

            String tempStr = tokenType + " " + accessToken;
            Call<User> call = retrofit.getUserInfo(tempStr);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Log.i(tag, "getUser: ok, " + response.body().name);
                            setUserInfo(response.body(), response.code());
                        }
                    } else {
                        Log.w(tag, "getUser: Something went wrong: " + response.code());
                        if (response.code() == 401) {
                            accessToken = null;
                            saveInfo.edit().putString("accessToken", accessToken).apply();
                            tokenType = null;
                            saveInfo.edit().putString("tokenType", tokenType).apply();
                        }
                        setUserInfo(null, response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                    Log.w(tag, "getUser: Something really went wrong: " + t.getMessage());
                    setUserInfo(null, 500);
                }
            });

        } else {
            Log.w(tag, "getUser: Error accessToken equals null");
            setUserInfo(null, 600);
        }
    }

    private void setUserInfo(User data, Integer code) {
        mainRepository.setUserInfo(data, code);
    }

    public void getRepositories(Integer pageSize, Integer pageNum) {

        if (accessToken != null) {

            String authorizationStr = tokenType + " " + accessToken;
            Call<List<RepositoryInfo>> tempCall = retrofit.getUserRepositories(authorizationStr,
                    "owner,collaborator,organization_member",
                    "created", pageSize, pageNum);

            tempCall.enqueue(new Callback<List<RepositoryInfo>>() {
                @Override
                public void onResponse(
                        @NonNull Call<List<RepositoryInfo>> call,
                        @NonNull Response<List<RepositoryInfo>> response) {

                    if (response.isSuccessful()) {
                        Log.i(tag, "getRepositories: ok");
                        sendRepositories(response.body(), response.code());
                    } else {
                        Log.w(tag, "getRepositories: Something went wrong: " + response.code());
                        sendRepositories(null, response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<RepositoryInfo>> call, @NonNull Throwable t) {
                    Log.w(tag, "getRepositories: Something really went wrong: " + t.getMessage());
                    mainRepository.setRepositoriesInfo(null, 500);
                }
            });
        } else {
            Log.w(tag, "getRepositories: Error accessToken equals null");
            sendRepositories(null, 600);
        }
    }

    private void sendRepositories(List<RepositoryInfo> data, Integer code) {
        mainRepository.setRepositoriesInfo(data, code);
    }

    public void getRepositoryBranches(String owner, String repositoryName,
                                      Integer pageSize, Integer pageNum) {

        String branchesUrl = "https://api.github.com/repos/" + owner + "/" + repositoryName
        + "/branches";
        String authorizationStr = tokenType + " " + accessToken;

        Call<List<Branch>> call = retrofit.getRepositoryBranches(authorizationStr, branchesUrl, pageSize, pageNum);
        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(@NonNull Call<List<Branch>> call, @NonNull Response<List<Branch>> response) {
                if (response.isSuccessful()) {
                    Log.i(tag, "getRepositoryBranches: ok");
                    sendBranches(response.body(), response.code());
                } else {
                    Log.w(tag, "getRepositoryBranches: Something went wrong: "
                            + response.code());
                    sendBranches(null, response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Branch>> call, @NonNull Throwable t) {
                Log.w(tag, "getRepositoryBranches: Something really went wrong: " + t.getMessage());
                sendBranches(null, 500);
            }
        });
    }

    private void sendBranches(List<Branch> data, int code) {
        mainRepository.sendBranches(data, code);
    }

    public void getRepositoryCommits(String owner, String repositoryName, String branch,
                                     Integer pageSize, Integer pageNum) {
        String commitsUrl = "https://api.github.com/repos/" + owner + "/" + repositoryName
                + "/commits";
        String authorizationStr = tokenType + " " + accessToken;

        if (branch.isEmpty()) {

            Call<List<Commits>> call = retrofit.getRepositoryCommits(authorizationStr, commitsUrl);
            call.enqueue(new Callback<List<Commits>>() {
                @Override
                public void onResponse(@NonNull Call<List<Commits>> call, @NonNull Response<List<Commits>> response) {
                    if (response.isSuccessful()) {
                        Log.i(tag, "getRepositoryCommits: ok");
                        sendCommits(response.body(), response.code());
                    } else {
                        Log.w(tag, "getRepositoryCommits: Something went wrong: " + response.code());
                        sendCommits(null, response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Commits>> call, @NonNull Throwable t) {
                    Log.w(tag, "getRepositoryCommits: Something went really wrong: " + t.getMessage());
                    sendCommits(null, 600);
                }
            });

        } else {

            Call<List<Commits>> call = retrofit.getCommitsByBranch(authorizationStr, commitsUrl, branch, pageSize, pageNum);
            call.enqueue(new Callback<List<Commits>>() {
                @Override
                public void onResponse(@NonNull Call<List<Commits>> call, @NonNull Response<List<Commits>> response) {
                    if (response.isSuccessful()) {
                        Log.i(tag, "getRepositoryCommits: ok");
                        sendCommits(response.body(), response.code());
                    } else {
                        Log.w(tag, "getRepositoryCommits: Something went wrong: " + response.code());
                        sendCommits(null, response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Commits>> call, @NonNull Throwable t) {
                    Log.w(tag, "getRepositoryCommits: Something went really wrong: " + t.getMessage());
                    sendCommits(null, 600);
                }
            });
        }
    }

    private void sendCommits(List<Commits> data, int code) {
        mainRepository.sendCommits(data, code);
    }
}
