package com.example.githubclientapp.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.githubclientapp.model.Repository;
import com.example.githubclientapp.model.retrofit.dataСlasses.User;

public class MainViewModel extends AndroidViewModel implements RepositoryViewModel {

    private String tag = "MainViewModel";
    private final Repository repository = new Repository(this);

    public final MutableLiveData<User> mutableUserInfo = new MutableLiveData<>();
    public LiveData<User> stateUserInfo = mutableUserInfo;

    public MainViewModel(@NonNull Application application) {
        super(application);

        Log.i(tag, "create viewModel");
    }

    public void getAccessToken(String accessToken) {
        repository.getAccessToken(accessToken);
    }


    @Override
    public void setUserInfo(User data, Integer code) {
        if (code == 200) {
            mutableUserInfo.setValue(data);
            Log.i(tag, data.name);
        }
    }

    public User getUserInfo() {
        return mutableUserInfo.getValue();
    }
}
