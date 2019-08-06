package com.openclassrooms.realestatemanager.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.openclassrooms.realestatemanager.models.Photo;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM Photo WHERE realEstateId =:realEstateId")
    LiveData<List<Photo>> getUriPhotos(long realEstateId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPhotos(Photo... photos);

    @Query("DELETE FROM Photo WHERE realEstateId =:realEstateId")
    void deleteAllPhotos(long realEstateId);
}
