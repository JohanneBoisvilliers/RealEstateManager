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

    public LiveData<List<Photo>> getUriPhotos(long realEstateId){return this.mPhotoDao.getUriPhotos(realEstateId);}

    public void createPhotos(Photo[] photoList) {
        mPhotoDao.insertPhotos(photoList);
    }

    public void updatePhoto(long photoId, String description) {
        mPhotoDao.updatePhoto(photoId, description);
    }

    public void deleteAllPhotos(long realEstateId) {
        mPhotoDao.deleteAllPhotos(realEstateId);
    }
}
