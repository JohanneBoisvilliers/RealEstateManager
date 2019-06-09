package com.openclassrooms.realestatemanager.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<RealEstateWithPhotos> selected = new MutableLiveData<RealEstateWithPhotos>();

    public void select(RealEstateWithPhotos item) {
        selected.setValue(item);
    }

    public LiveData<RealEstateWithPhotos> getSelected() {
        return selected;
    }
}
