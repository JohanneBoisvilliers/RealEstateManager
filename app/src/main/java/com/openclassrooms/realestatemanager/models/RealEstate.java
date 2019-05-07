package com.openclassrooms.realestatemanager.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.List;

@Entity/*(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId"))*/
public class RealEstate implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @Ignore private long userId;

    private String category;
    @Ignore private int price;
    @Ignore private Boolean isSold;
    @Ignore private int surface;
    @Ignore private int nbreOfRoom;
    @Ignore private String description;
    @Ignore private String address;
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RealEstate createFromParcel(Parcel in) {
            return new RealEstate(in);
        }

        public RealEstate[] newArray(int size) {
            return new RealEstate[size];
        }
    };

    public RealEstate() {
    }

    public RealEstate(Parcel in) {
        this.category = in.readString();
    }

    public RealEstate(long id, long userId, String category, int price, Boolean isSold, int surface, int nbreOfRoom, String description, String address) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.price = price;
        this.isSold = isSold;
        this.surface = surface;
        this.nbreOfRoom = nbreOfRoom;
        this.description = description;
        this.address = address;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.category);
    }
}
