package com.openclassrooms.realestatemanager.viewModels;

import android.arch.lifecycle.ViewModel;

public class AddOrModifyViewModel extends ViewModel {

    private int mPrice;
    private int mRooms;
    private int mSpinnerPos;

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public int getRooms() {
        return mRooms;
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
}
