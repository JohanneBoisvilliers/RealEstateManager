package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.models.User;

public class SingletonSession {

    private static SingletonSession instance;
    private User mCurrentUser;

    //no outer class can initialize this class's object
    private SingletonSession() {
    }

    public static SingletonSession Instance() {
        //if no instance is initialized yet then create new instance
        //else return stored instance
        if (instance == null) {
            instance = new SingletonSession();
        }
        return instance;
    }

    public User getUser() {
        return mCurrentUser;
    }

    public void setUser(User user) {
        this.mCurrentUser = user;
    }
}
