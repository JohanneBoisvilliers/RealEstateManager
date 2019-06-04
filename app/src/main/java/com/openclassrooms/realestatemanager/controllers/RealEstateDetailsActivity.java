package com.openclassrooms.realestatemanager.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.realEstateDetails.RealEstateDetailsFragment;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;

public class RealEstateDetailsActivity extends AppCompatActivity {

    private RealEstateDetailsFragment mRealEstateDetailsFragment;
    private long mRealEstateId;
    private RealEstateViewModel mRealEstateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_details);

        mRealEstateId = this.getRealEstateIdFromIntent();
        this.configureRealEstateDetailsFragment();
    }

    private void configureRealEstateDetailsFragment(){
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mRealEstateDetailsFragment = (RealEstateDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container_real_estate_detail);

        if (mRealEstateDetailsFragment == null) {
            // B - Create new main fragment
            mRealEstateDetailsFragment = new RealEstateDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putLong("realEstate", mRealEstateId);
            mRealEstateDetailsFragment.setArguments(bundle);
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_real_estate_detail, mRealEstateDetailsFragment)
                    .commit();
        }
    }

    private long getRealEstateIdFromIntent(){
        long id;
        Intent intent = getIntent();
        id = intent.getLongExtra("realEstate",0);
        return id;
    }

}
