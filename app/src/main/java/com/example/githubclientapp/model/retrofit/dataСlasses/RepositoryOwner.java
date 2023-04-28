package com.example.githubclientapp.model.retrofit.dataСlasses;

import com.google.gson.annotations.SerializedName;

public class RepositoryOwner {
    @SerializedName("login")
    public String login;
    @SerializedName("avatar_url")
    public String avatar_url;
}
