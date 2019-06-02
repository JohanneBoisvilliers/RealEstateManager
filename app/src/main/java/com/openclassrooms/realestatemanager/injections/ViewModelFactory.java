package com.openclassrooms.realestatemanager.injections;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;
import com.openclassrooms.realestatemanager.repositories.PhotoDataRepository;
import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RealEstateDataRepository mRealEstateDataSource;
    private final PhotoDataRepository mPhotoDataSource;
    private final Executor executor;

    public ViewModelFactory(RealEstateDataRepository realEstateDataRepository,PhotoDataRepository photoDataSource, Executor executor) {
        this.mRealEstateDataSource = realEstateDataRepository;
        this.mPhotoDataSource = photoDataSource;
        this.executor = executor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RealEstateViewModel.class)) {
            return (T) new RealEstateViewModel(mRealEstateDataSource,mPhotoDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
