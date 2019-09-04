package com.openclassrooms.realestatemanager.realEstateDetails;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Photo;

import java.util.ArrayList;
import java.util.List;

public class GalleryDialog extends DialogFragment {
    ViewPager viewPager;
    private List<Photo> mRealEstatePhotos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.gallery_dialog, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
        viewPager = (ViewPager) rootview.findViewById(R.id.masterViewPager);
        mRealEstatePhotos = (ArrayList<Photo>) getArguments().getSerializable("photos");
        PhotoViewpagerAdapter adapter = new PhotoViewpagerAdapter(getChildFragmentManager(), mRealEstatePhotos);
        viewPager.setAdapter(adapter);
        return rootview;
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        super.onResume();

    }
}
