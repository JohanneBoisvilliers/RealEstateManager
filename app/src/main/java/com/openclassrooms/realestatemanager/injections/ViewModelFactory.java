package com.openclassrooms.realestatemanager.injections;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.openclassrooms.realestatemanager.realEstateList.ListItemViewModel;
import com.openclassrooms.realestatemanager.repositories.RealEstateDataRepository;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RealEstateDataRepository mRealEstateDataSource;
    private final Executor executor;

    public ViewModelFactory(RealEstateDataRepository realEstateDataRepository, Executor executor) {
        this.mRealEstateDataSource = realEstateDataRepository;
        this.executor = executor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListItemViewModel.class)) {
            return (T) new ListItemViewModel(mRealEstateDataSource, executor);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
