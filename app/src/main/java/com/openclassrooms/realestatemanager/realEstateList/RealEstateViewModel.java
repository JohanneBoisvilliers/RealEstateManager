package com.openclassrooms.realestatemanager.realEstateList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.repositories.PhotoDataRepository;
import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class RealEstateViewModel extends ViewModel {

    // REPOSITORIES
    private final RealEstateDataRepository mRealEstateDataSource;
    private final PhotoDataRepository mPhotoDataSource;
    private final Executor executor;

    public RealEstateViewModel(RealEstateDataRepository realEstateDataSource,PhotoDataRepository photoDataSource, Executor executor) {
        this.mRealEstateDataSource = realEstateDataSource;
        this.mPhotoDataSource = photoDataSource;
        this.executor = executor;
    }

    // -------------
    // FOR REAL ESTATE
    // -------------

    public LiveData<List<RealEstate>> getRealEstate() {
        return mRealEstateDataSource.getRealEstates();
    }

    public LiveData<RealEstate> getSpecificRealEstate(long id) {
        return mRealEstateDataSource.getSpecificRealEstate(id);
    }

    public LiveData<List<RealEstateWithPhotos>> getRealEstatewithPhotos() {
        return mRealEstateDataSource.getRealEstatesWithPhotos();
    }

    public void createItem(RealEstate realEstate) {
        executor.execute(() -> {
            mRealEstateDataSource.createRealEstate(realEstate);
        });
    }

    public void deleteItem(long realEstateId) {
        executor.execute(() -> {
            mRealEstateDataSource.deleteRealEstate(realEstateId);
        });
    }

    public void updateItem(RealEstate realEstate) {
        executor.execute(() -> {
            mRealEstateDataSource.updateRealEstate(realEstate);
        });
    }

    // -------------
    // FOR PHOTOS
    // -------------

    public LiveData<List<Photo>> getRealEstatePhotos(long realEstateId) {
        return mPhotoDataSource.getUriPhotos(realEstateId);
    }
}
