package com.openclassrooms.realestatemanager.repositories;

import android.arch.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.database.dao.PhotoDao;
import com.openclassrooms.realestatemanager.models.Photo;

import java.util.List;

public class PhotoDataRepository {

    private final PhotoDao mPhotoDao;

    public PhotoDataRepository(PhotoDao mPhotoDao) {
        this.mPhotoDao = mPhotoDao;
    }

    // GET

    public LiveData<List<Photo>> getUriPhotos(long realEstateId){return this.mPhotoDao.getUriPhotos(realEstateId);}
}
