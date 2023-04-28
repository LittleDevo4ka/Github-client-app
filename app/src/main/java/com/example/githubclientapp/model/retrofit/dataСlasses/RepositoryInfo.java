package com.example.githubclientapp.model.retrofit.dataСlasses;

import com.google.gson.annotations.SerializedName;

public class RepositoryInfo {

    @SerializedName("name")
    public String name;
    @SerializedName("owner")
    public RepositoryOwner owner;
    @SerializedName("commits_url")
    public String commits_url;
    @SerializedName("branches_url")
    public String branches_url;
    @SerializedName("languages_url")
    public String languages_url;
    @SerializedName("forks_count")
    public Integer forks_count;
    @SerializedName("watchers_count")
    public Integer watchers_count;
    @SerializedName("default_branch")
    public String default_branch;

}
