package com.example.githubclientapp.model.retrofit.dataСlasses.commits;

import com.google.gson.annotations.SerializedName;

public class Commits {
    @SerializedName("sha")
    public String sha;
    @SerializedName("commit")
    public Commit commit;


}
