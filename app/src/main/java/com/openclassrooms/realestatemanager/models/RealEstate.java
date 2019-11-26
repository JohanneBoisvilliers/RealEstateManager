package com.openclassrooms.realestatemanager.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.content.ContentValues;

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
    private String address;
    private String pointsOfInterest;
    private String upForSale;
    private String soldSince;

    public RealEstate() {
    }

    public RealEstate(long id, long userId) {
        this.id = id;
        this.userId = userId;
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
    public String getPointsOfInterest() {
        return pointsOfInterest;
    }
    public String getUpForSale() {
        return upForSale;
    }
    public String getSoldSince() {
        return soldSince;
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
    public void setPointsOfInterest(String pointsOfInterest) {
        this.pointsOfInterest = pointsOfInterest;
    }
    public void setUpForSale(String upForSale) {
        this.upForSale = upForSale;
    }

    public void setSoldSince(String soldSince) {
        this.soldSince = soldSince;
    }

    public static RealEstate fromContentValues(ContentValues values) {
        final RealEstate item = new RealEstate();
        if (values.containsKey("userId")) item.setUserId(values.getAsLong("userId"));
        if (values.containsKey("id")) item.setId(values.getAsLong("id"));
        if (values.containsKey("userId")) item.setUserId(values.getAsLong("userId"));
        if (values.containsKey("category")) item.setCategory(values.getAsString("category"));
        if (values.containsKey("price")) item.setPrice(values.getAsInteger("price"));
        if (values.containsKey("isSold")) item.setSold(values.getAsBoolean("isSold"));
        if (values.containsKey("surface")) item.setSurface(values.getAsInteger("surface"));
        if (values.containsKey("nbreOfRoom")) item.setNbreOfRoom(values.getAsInteger("nbreOfRoom"));
        if (values.containsKey("description")) item.setDescription(values.getAsString("description"));
        if (values.containsKey("address")) item.setAddress(values.getAsString("address"));
        if (values.containsKey("pointsOfInterest")) item.setPointsOfInterest(values.getAsString("pointsOfInterest"));
        if (values.containsKey("upForSale")) item.setUpForSale(values.getAsString("upForSale"));
        if (values.containsKey("soldSince")) item.setSoldSince(values.getAsString("soldSince"));
        return item;
    }
}
