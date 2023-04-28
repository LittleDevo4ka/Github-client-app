package com.example.githubclientapp.model.retrofit.dataСlasses.commits;

import com.google.gson.annotations.SerializedName;

public class Committer {
    @SerializedName("name")
    public String name;
    @SerializedName("date")
    public String date;
}
