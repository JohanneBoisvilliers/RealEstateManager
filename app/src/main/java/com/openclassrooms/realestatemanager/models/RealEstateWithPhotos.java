package com.openclassrooms.realestatemanager.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class RealEstateWithPhotos{
    @Embedded
    private RealEstate realEstate;
    @Relation(parentColumn = "id", entityColumn = "realEstateId", entity = Photo.class)
    private List<Photo> photoList;

    public List<Photo> getPhotoList() {
        return photoList;
    }

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }
}
