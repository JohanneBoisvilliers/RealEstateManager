package com.openclassrooms.realestatemanager.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;

import java.util.List;

@Dao
public interface RealEstateDao {
    @Query("SELECT * FROM RealEstate" +
            " LEFT JOIN User ON User.id = RealEstate.userId ")
    LiveData<List<RealEstate>> getRealEstates();

    @Query("SELECT * FROM RealEstate WHERE id = :realEstateId")
    RealEstateWithPhotos getSpecificRealEstate(long realEstateId);

    @Query("SELECT * FROM RealEstate")
    LiveData<List<RealEstateWithPhotos>> getRealEstateWithPhotos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRealEstate(RealEstate realEstate);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateRealEstate(RealEstate realEstate);

    @Query("DELETE FROM RealEstate WHERE id = :realEstateId")
    int deleteItem(long realEstateId);
}

