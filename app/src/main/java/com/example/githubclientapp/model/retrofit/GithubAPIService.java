package com.example.githubclientapp.model.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.githubclientapp.BuildConfig;
import com.example.githubclientapp.model.Repository;
import com.example.githubclientapp.model.retrofit.dataСlasses.AccessToken;
import com.example.githubclientapp.model.retrofit.dataСlasses.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubAPIService {
    private final String tag = "GithubAPIService";
    private Repository mainRepository;
    private String baseURL = "https://github.com/login/oauth/authorize";
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private GithubAPI retrofit = new Retrofit.Builder()
            .baseUrl("https://github.com/login/oauth/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(GithubAPI.class);

    private String accessToken = null;
    private String tokenType = null;

    public GithubAPIService(Repository repository) {
        mainRepository = repository;
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
                        tokenType = response.body().getTokenType();
                        getUser();
                    }
                } else {
                    Log.i(tag,"getAccessToken: Something went wrong " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                Log.i(tag,"getAccessToken: Something really went wrong " + t.getMessage());
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
                            Log.i(tag, "OK: " + response.body().name);
                            setUserInfo(response.body(), response.code());
                        }
                    } else {
                        Log.w(tag, "getUser: Something went wrong: " + response.code());
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
        }
    }

    private void setUserInfo(User data, Integer code) {
        mainRepository.setUserInfo(data, code);
    }
}
