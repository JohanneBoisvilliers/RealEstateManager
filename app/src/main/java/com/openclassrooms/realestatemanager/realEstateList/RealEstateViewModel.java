package com.openclassrooms.realestatemanager.realEstateList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.repositories.PhotoDataRepository;
import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;

import java.util.List;
import java.util.concurrent.Executor;

import static com.openclassrooms.realestatemanager.realEstateList.RealEstateListFragment.TAG;

public class RealEstateViewModel extends ViewModel {

    // REPOSITORIES
    private final RealEstateDataRepository mRealEstateDataSource;
    private final PhotoDataRepository mPhotoDataSource;
    private final Executor executor;
    private final MutableLiveData<RealEstateWithPhotos> selected = new MutableLiveData<RealEstateWithPhotos>();
    private RealEstate mRealEstate;
    private int mPrice;
    private int mRooms;
    private long mRealEstateId;

    public RealEstateViewModel(RealEstateDataRepository realEstateDataSource,PhotoDataRepository photoDataSource, Executor executor) {
        this.mRealEstateDataSource = realEstateDataSource;
        this.mPhotoDataSource = photoDataSource;
        this.executor = executor;
    }

    private int mSpinnerPos;

    // -------------
    // FOR REAL ESTATE
    // -------------

    public void init(RealEstate realEstate) {
        if (realEstate == null) {
            mRealEstate = new RealEstate();
        }
    }

    public void select(RealEstateWithPhotos item) {
        selected.setValue(item);
    }

    public LiveData<RealEstateWithPhotos> getSelected() {
        return selected;
    }

    public LiveData<List<RealEstateWithPhotos>> getRealEstatewithPhotos() {
        return mRealEstateDataSource.getRealEstatesWithPhotos();
    }

    public void createItem(RealEstate realEstate) {
        executor.execute(() -> {
            mRealEstateId = mRealEstateDataSource.createRealEstate(realEstate);
            realEstate.setId(mRealEstateId);
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

    // -------------
    // FOR ADD OR MODIFY REAL ESTATE
    // -------------

    public void setPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public void setRooms(int mRooms) {
        this.mRooms = mRooms;
    }

    public int getSpinnerPos() {
        return mSpinnerPos;
    }

    public void setSpinnerPos(int spinnerPos) {
        mSpinnerPos = spinnerPos;
    }

    public RealEstate getRealEstate() {
        return mRealEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.mRealEstate = realEstate;
    }

    public void insertOrUpdate(RealEstate realEstate) {
        Log.d(TAG, "createItem: " + realEstate.getId());
        executor.execute(() -> {
            if (realEstate.getId() != mRealEstateId || realEstate.getId() == 0) {
                createItem(realEstate);
                Log.d(TAG, "insertOrUpdate: item created !");
            } else {
                updateItem(realEstate);
                Log.d(TAG, "insertOrUpdate: item updated !");
            }
        });
    }

}
