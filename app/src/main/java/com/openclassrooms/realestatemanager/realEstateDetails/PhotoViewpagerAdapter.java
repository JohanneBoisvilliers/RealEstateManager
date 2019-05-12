package com.openclassrooms.realestatemanager.realEstateDetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PhotoViewpagerAdapter extends FragmentPagerAdapter {

    // 1 - Array of colors that will be passed to PageFragment
    private int[] colors;

    // 2 - Default Constructor
    public PhotoViewpagerAdapter(FragmentManager mgr, int[] colors) {
        super(mgr);
        this.colors = colors;
    }

    @Override
    public Fragment getItem(int position) {
        return(photoViewpagerFragment.newInstance(position, this.colors[position]));
    }

    @Override
    public int getCount() {
        return 3;
    }
}
