package com.openclassrooms.realestatemanager.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.realEstateDetails.RealEstateDetailsFragment;

public class RealEstateDetailsActivity extends AppCompatActivity {

    private RealEstateDetailsFragment mRealEstateDetailsFragment;
    private RealEstate mRealEstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_details);

        mRealEstate = this.getRealEstateFromIntent();
        this.configureRealEstateDetailsFragment();
    }

    private void configureRealEstateDetailsFragment(){
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mRealEstateDetailsFragment = (RealEstateDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container_real_estate_detail);



        if (mRealEstateDetailsFragment == null) {
            // B - Create new main fragment
            mRealEstateDetailsFragment = new RealEstateDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("realEstate",this.getRealEstateFromIntent());
            mRealEstateDetailsFragment.setArguments(bundle);
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_real_estate_detail, mRealEstateDetailsFragment)
                    .commit();
        }
    }

    private RealEstate getRealEstateFromIntent(){
        Intent intent = getIntent();
        RealEstate realEstate = intent.getParcelableExtra("realEstate");
        return realEstate;
    }
}
