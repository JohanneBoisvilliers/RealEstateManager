package com.openclassrooms.realestatemanager.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String mUrl;

    public Photo() {}

    // GETTERS
    public long getId() {
        return id;
    }
    public String getmUrl() {
        return mUrl;
    }

    //SETTERS

    public void setId(long id) {
        this.id = id;
    }
    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
