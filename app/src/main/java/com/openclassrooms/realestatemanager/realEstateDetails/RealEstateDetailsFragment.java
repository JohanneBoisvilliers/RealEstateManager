package com.openclassrooms.realestatemanager.realEstateDetails;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.SharedViewModel;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.realEstateList.ListItemViewModel;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateDetailsFragment extends Fragment {

    @BindView(R.id.real_estate_category) TextView mRealEstateCategory;
    @BindView(R.id.real_estate_price) TextView mRealEstatePrice;
    @BindView(R.id.real_estate_description) TextView mRealEstateDescription;
    @BindView(R.id.real_estate_description_fade) TextView mRealEstateDescriptionFade;
    @BindView(R.id.information_location) TextView mRealEstateLocation;
    @BindView(R.id.btShowmore) Button mButtonMoreDescription;
    @BindView(R.id.btnLocationMore) Button mButtonMoreLocation;
    @BindView(R.id.information_surface) TextView mInformationSurface;
    @BindView(R.id.information_room) TextView mInformationRoom;
    @BindView(R.id.information_agent) TextView mInformationAgent;
    @BindView(R.id.real_estate_photo) ViewPager mPhotoViewpager;
    @BindView(R.id.dot_indicator) TabLayout mDotIndicator;
    @BindView(R.id.main_fab) FloatingActionButton mMainFAB;
    @BindView(R.id.status_fab) FloatingActionButton mStatusFAB;
    @BindView(R.id.modify_real_estate_fab) FloatingActionButton mModifyEstate;
    @BindView(R.id.add_photo_fab) FloatingActionButton mAddPhoto;
    @Nullable
    @BindView(R.id.real_estate_recycler_view) RecyclerView mRealEstateRecyclerView;

    private int mNumberOfLine;
    private RealEstate mRealEstate;
    private Boolean isFABOpen = false;

    public RealEstateDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_real_estate_details, container, false);
        ButterKnife.bind(this,view);

        this.getRealEstateToConfigure();
        this.configureViewPager();
        this.configureFABMenu();
        this.configureExpandDescription();
        this.configureExpandLocation();

        return view;
    }
    //configure viewpager which contain photos
    private void configureViewPager(){
        mPhotoViewpager.setAdapter(new PhotoViewpagerAdapter(getChildFragmentManager(),getResources().getIntArray(R.array.colorPagesViewPager)));
        mDotIndicator.setupWithViewPager(mPhotoViewpager,true);
    }
    //set a click listener to open FAB menu
    private void configureFABMenu(){
        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
    }
    //show FAB menu
    private void showFABMenu(){
        isFABOpen=true;
        mStatusFAB.animate().translationY(-getResources().getDimension(R.dimen.FAB_elevation_55));
        mModifyEstate.animate().translationY(-getResources().getDimension(R.dimen.FAB_elevation_105));
        mAddPhoto.animate().translationY(-getResources().getDimension(R.dimen.FAB_elevation_155));
    }
    //hide FAB menu
    private void closeFABMenu(){
        isFABOpen=false;
        mStatusFAB.animate().translationY(0);
        mModifyEstate.animate().translationY(0);
        mAddPhoto.animate().translationY(0);
    }
    //get RealEstate from Activity and configure view details in fragment
    private void configureDetails(RealEstate realEstate){
        mRealEstateCategory.setText(realEstate.getCategory());
        mRealEstatePrice.setText(String.valueOf(realEstate.getPrice()));
        mRealEstateDescription.setText(realEstate.getDescription());
        mRealEstateDescriptionFade.setText(realEstate.getDescription());
        mInformationSurface.setText(getResources().getString((R.string.real_estate_surface),realEstate.getSurface()));
        mInformationRoom.setText(getResources().getString((R.string.real_estate_room),realEstate.getNbreOfRoom()));
    }
    private void getRealEstateToConfigure(){
        mRealEstateRecyclerView = getActivity().findViewById(R.id.real_estate_recycler_view);
        if(mRealEstateRecyclerView == null){//one pane layout
            Log.d("DEBGU", "one pane layout");
            Intent intent = getActivity().getIntent();
            mRealEstate = intent.getParcelableExtra("realEstate");
            configureDetails(mRealEstate);
        }else{//two panes layout
            Log.d("DEBGU", "two panes layout");
            SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
            model.getSelected().observe(this,  item -> {
                mRealEstate = item;
                if (mRealEstate==null) {
                    mRealEstate = ((RealEstateAdapter)mRealEstateRecyclerView.getAdapter()).getRealEstate(0);
                }
                configureDetails(mRealEstate);
            });

        }
    }
    //add a button to open description when it's longer than 3 lines
    private void configureExpandDescription(){
        mRealEstateDescription.post(new Runnable() {
            @Override
            public void run() {
                mNumberOfLine = mRealEstateDescription.getLineCount();
                if (mNumberOfLine>2){
                    mButtonMoreDescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (mButtonMoreDescription.getText().toString().equalsIgnoreCase(getResources().getString(R.string.button_more)))
                            {
                                mRealEstateDescription.setMaxLines(Integer.MAX_VALUE);
                                mButtonMoreDescription.setText(getResources().getString(R.string.button_close));
                            }
                            else
                            {
                                mRealEstateDescription.setMaxLines(2);
                                mButtonMoreDescription.setText(getResources().getString(R.string.button_more));
                            }
                        }
                    });
                }else{
                    mButtonMoreDescription.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    //configure button to expand real estate location
    private void configureExpandLocation(){
        mButtonMoreLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mButtonMoreLocation.getText().toString().equalsIgnoreCase(getResources().getString(R.string.button_more)))
                {
                    mRealEstateLocation.setMaxLines(Integer.MAX_VALUE);
                    mButtonMoreLocation.setText(getResources().getString(R.string.button_close));
                }
                else
                {
                    mRealEstateLocation.setMaxLines(1);
                    mButtonMoreLocation.setText(getResources().getString(R.string.button_more));
                }
            }
        });
    }
}
