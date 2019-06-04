package com.openclassrooms.realestatemanager.realEstateDetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewpagerAdapter extends FragmentPagerAdapter {

    // 1 - Array of colors that will be passed to PageFragment
    private List<String> mRealEstatePhotos = new ArrayList<>();

    // 2 - Default Constructor
    public PhotoViewpagerAdapter(FragmentManager mgr, List<String> listOfPhotos) {
        super(mgr);
        this.mRealEstatePhotos = listOfPhotos;
    }

    @Override
    public Fragment getItem(int position) {
        return photoViewpagerFragment.newInstance(position, this.mRealEstatePhotos.get(position));
    }

    @Override
    public int getCount() {
        return mRealEstatePhotos.size();
    }

    public void updatePhotos(List<String> listOfPhotos){
        this.mRealEstatePhotos.clear();
        this.mRealEstatePhotos = listOfPhotos;
        this.notifyDataSetChanged();
    }
}
