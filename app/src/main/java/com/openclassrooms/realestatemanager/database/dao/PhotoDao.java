package com.openclassrooms.realestatemanager.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM Photo WHERE Id =:realEstateId")
    LiveData<List<String>> getUriPhotos(long realEstateId);
}
