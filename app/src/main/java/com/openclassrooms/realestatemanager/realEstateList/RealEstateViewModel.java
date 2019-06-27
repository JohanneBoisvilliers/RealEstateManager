package com.openclassrooms.realestatemanager.realEstateList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;
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
    public final ObservableField<Integer> mPrice = new ObservableField<>();
    public final ObservableField<Integer> mRooms = new ObservableField<>();
    public final ObservableField<Integer> mSpinnerPos = new ObservableField<>();
    private String mDescription;
    private long mRealEstateId;


    public RealEstateViewModel(RealEstateDataRepository realEstateDataSource,PhotoDataRepository photoDataSource, Executor executor) {
        this.mRealEstateDataSource = realEstateDataSource;
        this.mPhotoDataSource = photoDataSource;
        this.executor = executor;
    }


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

    /*SETTERS*/

    public void setDescription(String description) {
        mDescription = description;
    }

    /*GETTERS*/
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
