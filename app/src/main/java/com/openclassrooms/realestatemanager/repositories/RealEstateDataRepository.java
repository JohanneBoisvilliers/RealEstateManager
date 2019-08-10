package com.openclassrooms.realestatemanager.repositories;

import android.arch.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;

import java.util.List;

public class RealEstateDataRepository {

    private final RealEstateDao mRealEstateDao;

    public RealEstateDataRepository(RealEstateDao realEstateDao) { this.mRealEstateDao = realEstateDao; }

    // --- GET ---

    public LiveData<RealEstateWithPhotos> getSpecificRealEstate(long id) {
        return this.mRealEstateDao.getSpecificRealEstate(id);
    }
    public LiveData<List<RealEstateWithPhotos>> getRealEstatesWithPhotos(){ return this.mRealEstateDao.getRealEstateWithPhotos(); }

    // --- CREATE ---

    public long createRealEstate(RealEstate realEstate) {
        return mRealEstateDao.insertRealEstate(realEstate);
    }

    // --- DELETE ---
    public void deleteRealEstate(long itemId){ mRealEstateDao.deleteItem(itemId); }

    // --- UPDATE ---
    public void updateRealEstate(RealEstate realEstate){ mRealEstateDao.updateRealEstate(realEstate); }
}
