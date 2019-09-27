package com.openclassrooms.realestatemanager.realEstateList;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.databinding.ObservableField;

import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.models.User;
import com.openclassrooms.realestatemanager.repositories.PhotoDataRepository;
import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;
import com.openclassrooms.realestatemanager.repositories.UserDataRepository;
import com.openclassrooms.realestatemanager.utils.NotificationsService;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RealEstateViewModel extends ViewModel {

    // REPOSITORIES
    private final RealEstateDataRepository mRealEstateDataSource;
    private final PhotoDataRepository mPhotoDataSource;
    private final UserDataRepository mUserDataRepository;
    private final Executor executor;
    private final MutableLiveData<RealEstateWithPhotos> selected = new MutableLiveData<RealEstateWithPhotos>();
    private final MutableLiveData<List<String>> urlList = new MutableLiveData<>();
    private final MutableLiveData<LinkedHashMap<Long, String>> addressesList = new MutableLiveData<>();
    private final MutableLiveData<boolean[]> mActualState = new MutableLiveData<>();
    private RealEstate mRealEstate;
    public final ObservableField<Integer> price = new ObservableField<>();
    public final ObservableField<Integer> startPrice = new ObservableField<>();
    public final ObservableField<Integer> endPrice = new ObservableField<>();
    public final ObservableField<Integer> rooms = new ObservableField<>();
    public final ObservableField<Integer> spinnerPos = new ObservableField<>();
    public final ObservableField<Integer> surface = new ObservableField<>();
    public final ObservableField<Integer> surfacestart = new ObservableField<>();
    public final ObservableField<Integer> surfaceEnd = new ObservableField<>();
    public final ObservableField<Integer> selectedItemPos = new ObservableField<>();
    public final ObservableField<String> category = new ObservableField<>();
    public final ObservableField<String> description = new ObservableField<>();
    public final ObservableField<String> numberOfPhoto = new ObservableField<>();
    public final ObservableField<String> address = new ObservableField<>();
    public final ObservableField<String> pointOfInterest = new ObservableField<>();
    private long mRealEstateId;

    public RealEstateViewModel(RealEstateDataRepository realEstateDataSource,
                               PhotoDataRepository photoDataSource,
                               UserDataRepository userDataSource, Executor executor) {
        this.mRealEstateDataSource = realEstateDataSource;
        this.mPhotoDataSource = photoDataSource;
        this.mUserDataRepository = userDataSource;
        this.executor = executor;
    }

    // FOR REAL ESTATE

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
    public LiveData<RealEstateWithPhotos> getSpecificEstate(Long id) {
        return mRealEstateDataSource.getSpecificRealEstate(id);
    }

    public LiveData<List<RealEstateWithPhotos>> getResultSearchRaw(SimpleSQLiteQuery query) {
        return mRealEstateDataSource.getResultSearchRaw(query);
    }
    public void updateRealEstate(RealEstate realEstate) {
        executor.execute(() -> mRealEstateDataSource.updateRealEstate(realEstate));
    }
    public void createItem(RealEstate realEstate) {
        executor.execute(() -> {
            mRealEstateId = mRealEstateDataSource.createRealEstate(realEstate);
            NotificationsService.sendNotification();
        });
    }
    public void updateItem(RealEstate realEstate) {
        executor.execute(() -> {
            mRealEstateDataSource.updateRealEstate(realEstate);
        });
    }

    // FOR PHOTOS

    public LiveData<List<Photo>> getRealEstatePhotos(long realEstateId) {
        return mPhotoDataSource.getUriPhotos(realEstateId);
    }
    public void insertPhotos(Photo[] photos) {
        executor.execute(() -> {
            mPhotoDataSource.createPhotos(photos);
        });
    }
    public void deleteAllPhotos(long realEstateId) {
        executor.execute(() -> mPhotoDataSource.deleteAllPhotos(realEstateId));
    }
    public void updatePhotos(long realEstateId, Photo[] photos) {
        Completable deleteAllCompletable = Completable.fromAction(() -> deleteAllPhotos(realEstateId));
        Completable setPhotosId = Completable.fromAction(() -> {
            for (Photo listOfPhoto : photos) {
                listOfPhoto.setRealEstateId(mRealEstateId);
            }
        });
        Completable insertUserCompletable = Completable.fromAction(() -> insertPhotos(photos));

        deleteAllCompletable
                .andThen(setPhotosId)
                .andThen(insertUserCompletable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe();
    }
    public void updateSpecificPhoto(long photoId, String description) {
        executor.execute(() -> mPhotoDataSource.updatePhoto(photoId, description));
    }

    // FOR ADD OR MODIFY REAL ESTATE

    /*GETTERS*/
    public RealEstate getRealEstate() {
        return mRealEstate;
    }
    public void insertOrUpdate(RealEstate realEstate, Photo[] listOfPhotos) {
        if (realEstate.getId() != mRealEstateId || realEstate.getId() == 0) {
            realEstate.setUpForSale(Utils.getTodayDate());
            createItem(realEstate);
            executor.execute(() -> {
                for (Photo listOfPhoto : listOfPhotos) {
                    listOfPhoto.setRealEstateId(mRealEstateId);
                }
            });
            executor.execute(() -> {
                insertPhotos(listOfPhotos);
            });
        } else {
            updateItem(realEstate);
            updatePhotos(mRealEstateId, listOfPhotos);
        }
    }
    public void setRealEstateId(long realEstateId) {
        mRealEstateId = realEstateId;
    }
    public void selecturlList(List<String> item) {
        urlList.setValue(item);
    }
    public LiveData<List<String>> getUrlList() {
        return urlList;
    }
    public LiveData<boolean[]> getActualState() {
        return mActualState;
    }
    public void selectActualState(boolean[] booleans) {
        mActualState.setValue(booleans);
    }

    // FOR USER

    public LiveData<User> getUser(long userId) {
        return mUserDataRepository.getUser(userId);
    }

    //FOR MAP

    public void selectAddressesList(LinkedHashMap<Long, String> addressesList) {
        this.addressesList.setValue(addressesList);
    }

    public LiveData<LinkedHashMap<Long, String>> getAddressesList() {
        return this.addressesList;
    }
}
