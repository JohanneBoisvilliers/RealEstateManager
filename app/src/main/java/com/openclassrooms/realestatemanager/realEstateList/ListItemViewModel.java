package com.openclassrooms.realestatemanager.realEstateList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

public class ListItemViewModel extends ViewModel {

    // REPOSITORIES
    private final RealEstateDataRepository mRealEstateDataSource;
    private final Executor executor;

    public ListItemViewModel(RealEstateDataRepository realEstateDataSource,Executor executor) {
        this.mRealEstateDataSource = realEstateDataSource;
        this.executor = executor;
    }

    // -------------
    // FOR REAL ESTATE
    // -------------

    public LiveData<List<RealEstate>> getRealEstate() {
        return mRealEstateDataSource.getRealEstates();
    }

    public void createItem(RealEstate realEstate) {
        executor.execute(() -> {
            mRealEstateDataSource.createRealEstate(realEstate);
        });
    }

    public void deleteItem(long itemId) {
        executor.execute(() -> {
            mRealEstateDataSource.deleteRealEstate(itemId);
        });
    }

    public void updateItem(RealEstate realEstate) {
        executor.execute(() -> {
            mRealEstateDataSource.updateRealEstate(realEstate);
        });
    }

}
