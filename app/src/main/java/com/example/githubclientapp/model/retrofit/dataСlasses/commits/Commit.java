package com.example.githubclientapp.model.retrofit.dataСlasses.commits;

import com.google.gson.annotations.SerializedName;

public class Commit {

    @SerializedName("message")
    public String message;

    public Committer committer;
}
