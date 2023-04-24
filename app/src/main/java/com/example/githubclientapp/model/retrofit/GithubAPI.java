package com.example.githubclientapp.model.retrofit;

import com.example.githubclientapp.model.retrofit.dataСlasses.AccessToken;
import com.example.githubclientapp.model.retrofit.dataСlasses.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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

}
