package com.openclassrooms.realestatemanager.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String username;
    private String password;
    private String email;
    private String mPhotoUrl;

    public User(long id, String username, String password, String email, String photoUrl) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.mPhotoUrl = photoUrl;
    }

    public User() {
    }

    // -- GETTERS

    public long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }
    public String getEmail() {
        return email;
    }

    // -- SETTERS

    public void setId(long id) {
        this.id = id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
