package com.example.githubclientapp.model.retrofit;

import com.example.githubclientapp.model.Repository;
import com.example.githubclientapp.model.retrofit.dataСlasses.AccessToken;
import com.example.githubclientapp.model.retrofit.dataСlasses.Branch;
import com.example.githubclientapp.model.retrofit.dataСlasses.RepositoryInfo;
import com.example.githubclientapp.model.retrofit.dataСlasses.User;
import com.example.githubclientapp.model.retrofit.dataСlasses.commits.Commits;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GithubAPI {

    @Headers("Accept: application/json")
    @POST("https://github.com/login/oauth/access_token")
    @FormUrlEncoded
    Call<AccessToken> getAccessToken(
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("code") String clientCode
    );

    @Headers("Accept: application/vnd.github+json")
    @GET("https://api.github.com/user")
    Call<User> getUserInfo(
            @Header("Authorization") String authorization_str
    );

    @Headers("Accept: application/vnd.github+json")
    @GET("https://api.github.com/user/repos")
    Call<List<RepositoryInfo>> getUserRepositories(
            @Header("Authorization") String authorization_str,
            @Query("affiliation") String affiliation,
            @Query("sort") String sort,
            @Query("per_page") Integer pageSize,
            @Query("page") Integer pageNum
    );

    @Headers("Accept: application/vnd.github+json")
    @GET
    Call<List<Branch>> getRepositoryBranches(
            @Url String url,
            @Query("per_page") Integer pageSize,
            @Query("page") Integer pageNum
    );

    @Headers("Accept: application/vnd.github+json")
    @GET
    Call<List<Commits>> getRepositoryCommits(@Url String url);

    @Headers("Accept: application/vnd.github+json")
    @GET
    Call<List<Commits>> getCommitsByBranch(
            @Url String url,
            @Query("sha") String sha,
            @Query("per_page") Integer pageSize,
            @Query("page") Integer pageNum
    );
}
