package com.openclassrooms.realestatemanager.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId"))
public class RealEstate {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long userId;

    private String category;
    private int price;
    private Boolean isSold;
    private int surface;
    private int nbreOfRoom;
    private String description;
    @Ignore private List<String> mPhotoList = new ArrayList<>();
    @Ignore private String address;

    public RealEstate() {
    }

    // -- GETTERS
    public long getId() {
        return id;
    }
    public long getUserId() {
        return userId;
    }
    public String getCategory() {
        return category;
    }
    public int getPrice() {
        return price;
    }
    public Boolean getSold() {
        return isSold;
    }
    public int getSurface() {
        return surface;
    }
    public int getNbreOfRoom() {
        return nbreOfRoom;
    }
    public String getDescription() {
        return description;
    }
    public String getAddress() {
        return address;
    }
    public List<String> getPhotoList() {
        return mPhotoList;
    }

    // -- SETTERS
    public void setId(long id) {
        this.id = id;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setSold(Boolean sold) {
        isSold = sold;
    }
    public void setSurface(int surface) {
        this.surface = surface;
    }
    public void setNbreOfRoom(int nbreOfRoom) {
        this.nbreOfRoom = nbreOfRoom;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setPhotoList(List<String> mPhotoList) {
        this.mPhotoList = mPhotoList;
    }

}
