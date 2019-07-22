package com.openclassrooms.realestatemanager.realEstateList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

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

    private final MutableLiveData<RealEstateWithPhotos> selected = new MutableLiveData<RealEstateWithPhotos>();
    private RealEstate mRealEstate;
    public final ObservableField<Integer> mPrice = new ObservableField<>();
    public final ObservableField<Integer> mRooms = new ObservableField<>();
    public final ObservableField<Integer> mSpinnerPos = new ObservableField<>();
    public final ObservableField<Integer> mSurface = new ObservableField<>();
    public final ObservableField<String> mDescription = new ObservableField<>();
    public final ObservableField<String> mNumberOfPhoto = new ObservableField<>();
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

    public void insertPhotos(Photo[] photos) {
        executor.execute(() -> {
            mPhotoDataSource.createPhotos(photos);
        });
    }

    // -------------
    // FOR ADD OR MODIFY REAL ESTATE
    // -------------

    /*GETTERS*/
    public RealEstate getRealEstate() {
        return mRealEstate;
    }

    public void insertOrUpdate(RealEstate realEstate, Photo[] listOfPhotos) {
        if (realEstate.getId() != mRealEstateId || realEstate.getId() == 0) {
            createItem(realEstate);
        } else {
            updateItem(realEstate);
        }
        executor.execute(() -> {
            for (Photo listOfPhoto : listOfPhotos) {
                listOfPhoto.setRealEstateId(mRealEstateId);
            }
        });
        executor.execute(() -> {
            insertPhotos(listOfPhotos);
        });
    }

}
