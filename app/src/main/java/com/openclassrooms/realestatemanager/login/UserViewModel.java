package com.openclassrooms.realestatemanager.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.User;
import com.openclassrooms.realestatemanager.repositories.UserDataRepository;

import java.util.concurrent.Executor;

public class UserViewModel extends ViewModel {
    private final UserDataRepository mUserDataSource;
    public User currentUser;
    private final Executor executor;

    public UserViewModel(UserDataRepository userDataSource, Executor executor) {
        mUserDataSource = userDataSource;
        this.executor = executor;
    }

    public void createUser(User user) {
        executor.execute(() -> mUserDataSource.createUser(user));
    }

    public Long getUserForSignIn(String username, String password) {
        return mUserDataSource.getUserForSignIn(username, password);
    }

    public LiveData<User> getUser(Long userId) {
        return mUserDataSource.getUser(userId);
    }
}
