package com.openclassrooms.realestatemanager.realEstateDetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.openclassrooms.realestatemanager.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoViewpagerAdapter extends FragmentStatePagerAdapter {

    // 1 - Array of colors that will be passed to PageFragment
    private List<Photo> mRealEstatePhotos = new ArrayList<>();
    public static final String TAG = "DEBUG";

    // 2 - Default Constructor
    public PhotoViewpagerAdapter(FragmentManager mgr, List<Photo> listOfPhotos) {
        super(mgr);
        this.mRealEstatePhotos = listOfPhotos;
    }

    @Override
    public Fragment getItem(int position) {
        return photoViewpagerFragment.newInstance(this.mRealEstatePhotos.get(position).getUrl(),mRealEstatePhotos.get(position).getDescription());
    }

    @Override
    public int getCount() {
        return mRealEstatePhotos.size();
    }

    public void updatePhotos(List<Photo> listOfPhotos){
        this.mRealEstatePhotos.clear();
        this.mRealEstatePhotos = listOfPhotos;
        this.notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
