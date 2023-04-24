package com.example.githubclientapp.model.retrofit.dataСlasses;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("login")
    public String login;
    @SerializedName("email")
    public String email;

    @SerializedName("avatar_url")
    public String avatar_url;
    @SerializedName("name")
    public String name;
    @SerializedName("bio")
    public String bio;
    @SerializedName("followers")
    public Integer followers;
    @SerializedName("following")
    public Integer following;

    @SerializedName("repos_url")
    public String repos_url;

}
