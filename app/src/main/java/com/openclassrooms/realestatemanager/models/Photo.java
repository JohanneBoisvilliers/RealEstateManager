package com.openclassrooms.realestatemanager.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Photo {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long realEstateId;
    private String url;
    private String description;

    public Photo() {}

    // GETTERS
    public long getId() {
        return id;
    }
    public String getUrl() {
        return url;
    }
    public long getRealEstateId() {
        return realEstateId;
    }

    public String getDescription() {
        return description;
    }

    //SETTERS
    public void setId(long id) {
        this.id = id;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setRealEstateId(long realEstateId) {
        this.realEstateId = realEstateId;
    }
    public void setDescription(String mDescription) {
        this.description = mDescription;
    }
}
