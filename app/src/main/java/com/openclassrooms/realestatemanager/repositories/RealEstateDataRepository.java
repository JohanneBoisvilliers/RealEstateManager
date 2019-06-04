package com.openclassrooms.realestatemanager.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.List;

public class RealEstateDataRepository {

    private final RealEstateDao mRealEstateDao;

    public RealEstateDataRepository(RealEstateDao realEstateDao) { this.mRealEstateDao = realEstateDao; }

    // --- GET ---

    public LiveData<List<RealEstate>> getRealEstates(){ return this.mRealEstateDao.getRealEstates(); }
    public LiveData<RealEstate> getSpecificRealEstate(long id){ return this.mRealEstateDao.getSpecificRealEstate(id); }

    // --- CREATE ---

    public void createRealEstate(RealEstate realEstate){ mRealEstateDao.insertRealEstate(realEstate); }

    // --- DELETE ---
    public void deleteRealEstate(long itemId){ mRealEstateDao.deleteItem(itemId); }

    // --- UPDATE ---
    public void updateRealEstate(RealEstate realEstate){ mRealEstateDao.updateRealEstate(realEstate); }
}
