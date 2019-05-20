package com.openclassrooms.realestatemanager;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

import com.openclassrooms.realestatemanager.models.RealEstate;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<RealEstate> selected = new MutableLiveData<RealEstate>();

    public void select(RealEstate item) {
        selected.setValue(item);
    }

    public LiveData<RealEstate> getSelected() {
        return selected;
    }
}
