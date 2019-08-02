package com.openclassrooms.realestatemanager.realEstateDetails;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.getPrice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateDetailsFragment extends Fragment implements getPrice {

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
    @Nullable
    @BindView(R.id.real_estate_photo) ViewPager mPhotoViewpager;
    @BindView(R.id.dot_indicator) TabLayout mDotIndicator;
    @BindView(R.id.main_fab) FloatingActionButton mMainFAB;
    @BindView(R.id.status_fab) FloatingActionButton mStatusFAB;
    @BindView(R.id.modify_real_estate_fab) FloatingActionButton mModifyEstate;
    @BindView(R.id.add_photo_fab) FloatingActionButton mAddPhoto;
    @BindView(R.id.recyclerView_details_tablet) RecyclerView mRecyclerViewForPhotos;
    @Nullable
    @BindView(R.id.real_estate_recycler_view) RecyclerView mRealEstateRecyclerView;
    @Nullable
    @BindView(R.id.show_background_start) View mEntireView;
    @Nullable
    @BindView(R.id.background_start) ImageView mBackgroundWhenStarting;
    @Nullable
    @BindView(R.id.sold_out_bg) View mSoldOut;


    private static final String TAG = "DEBUG";
    private int mNumberOfLine;
    private RealEstateWithPhotos mRealEstate;
    private RecyclerViewDetailsPhotoAdapter mRecyclerViewPhotoAdapter;
    private Boolean isFABOpen = false;
    private Boolean isTwoPanesLayout = false;
    private RealEstateViewModel mRealEstateViewModel;
    private List<Photo> mRealEstatePhotos = new ArrayList<>();
    private PhotoViewpagerAdapter mPhotoViewPagerAdapter;


    public RealEstateDetailsFragment() {
    }

    // -------------------------------- LIFE CYCLE --------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_real_estate_details, container, false);
        ButterKnife.bind(this,view);

        this.configureViewModel();
        this.getRealEstateToConfigure();
        if (isTwoPanesLayout) {
            configureRecyclerView();
        }else{
            this.configureViewPager();
            this.configureFABMenu();
        }
        this.configureExpandDescription();
        this.configureExpandLocation();

        return view;
    }

    // ------------------------------------ UI ------------------------------------

    //configure viewpager which contain photos
    private void configureViewPager(){
        mPhotoViewPagerAdapter =new PhotoViewpagerAdapter(getChildFragmentManager(),mRealEstatePhotos);
        mPhotoViewpager.setAdapter(mPhotoViewPagerAdapter);
        mDotIndicator.setupWithViewPager(mPhotoViewpager,true);
    }
    //configure recyclerview for two panes layout case
    private void configureRecyclerView(){
        //Create adapter passing the list of restaurant
        this.mRecyclerViewPhotoAdapter = new RecyclerViewDetailsPhotoAdapter(mRealEstatePhotos);
        //Attach the adapter to the recyclerview to populate items
        this.mRecyclerViewForPhotos.setAdapter(this.mRecyclerViewPhotoAdapter);
        //Set layout manager to position the items
        this.mRecyclerViewForPhotos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
    //get RealEstate from Activity and configure view details in fragment
    private void configureDetails(RealEstateWithPhotos realEstate) {
        if (realEstate != null) {
            this.getRealEstatesPhotos(realEstate.getRealEstate().getId());
            mRealEstate = realEstate;
            updateRealEstatePhotos(mRealEstatePhotos);
            mRealEstateCategory.setText(realEstate.getRealEstate().getCategory());
            setRealEstatePrice(realEstate, mRealEstatePrice);
            mRealEstateDescription.setText(realEstate.getRealEstate().getDescription());
            mRealEstateDescriptionFade.setText(realEstate.getRealEstate().getDescription());
            mInformationSurface.setText(getResources().getString((R.string.real_estate_surface), realEstate.getRealEstate().getSurface()));
            mInformationRoom.setText(getResources().getString((R.string.real_estate_room), realEstate.getRealEstate().getNbreOfRoom()));
            this.setSoldState(realEstate.getRealEstate());
        }
    }
    //show FAB menu
    private void showFABMenu() {
        isFABOpen = true;
        mStatusFAB.animate().translationY(-getResources().getDimension(R.dimen.FAB_elevation_55));
        mModifyEstate.animate().translationY(-getResources().getDimension(R.dimen.FAB_elevation_105));
        mAddPhoto.animate().translationY(-getResources().getDimension(R.dimen.FAB_elevation_155));
    }
    //hide FAB menu
    private void closeFABMenu() {
        isFABOpen = false;
        mStatusFAB.animate().translationY(0);
        mModifyEstate.animate().translationY(0);
        mAddPhoto.animate().translationY(0);
    }
    //add a button to open description when it's longer than 3 lines
    private void configureExpandDescription() {
        mRealEstateDescription.post(new Runnable() {
            @Override
            public void run() {
                mNumberOfLine = mRealEstateDescription.getLineCount();
                if (mNumberOfLine >= 2) {
                    mButtonMoreDescription.setVisibility(View.VISIBLE);
                    mButtonMoreDescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mButtonMoreDescription.getText().toString().equalsIgnoreCase(getResources().getString(R.string.button_more))) {
                                mRealEstateDescription.setMaxLines(Integer.MAX_VALUE);
                                mButtonMoreDescription.setText(getResources().getString(R.string.button_close));
                            } else {
                                mRealEstateDescription.setMaxLines(2);
                                mButtonMoreDescription.setText(getResources().getString(R.string.button_more));
                            }
                        }
                    });
                } else {
                    mButtonMoreDescription.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    //configure button to expand real estate location
    private void configureExpandLocation() {
        mButtonMoreLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mButtonMoreLocation.getText().toString().equalsIgnoreCase(getResources().getString(R.string.button_more))) {
                    mRealEstateLocation.setMaxLines(Integer.MAX_VALUE);
                    mButtonMoreLocation.setText(getResources().getString(R.string.button_close));
                } else {
                    mRealEstateLocation.setMaxLines(1);
                    mButtonMoreLocation.setText(getResources().getString(R.string.button_more));
                }
            }
        });
    }
    //Hide or show sold out img depending sold state
    private void setSoldState(RealEstate realEstate) {
        if (mSoldOut != null) {
            if (realEstate.getSold()) {
                mSoldOut.setVisibility(View.VISIBLE);
            } else {
                mSoldOut.setVisibility(View.INVISIBLE);
            }
        }
    }

    // ---------------------------------- LISTENERS ----------------------------------

    //set a click listener to open FAB menu
    private void configureFABMenu(){
        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                    configureFABchangeStatus();
                }else{
                    closeFABMenu();
                }
            }
        });
    }
    //set a listener on sold button to change sold status
    private void configureFABchangeStatus(){
        mStatusFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isSold = mRealEstate.getRealEstate().getSold();
                isSold = !isSold;
                mRealEstate.getRealEstate().setSold(isSold);
                mRealEstateViewModel.updateItem(mRealEstate.getRealEstate());
                setSoldState(mRealEstate.getRealEstate());
            }
        });
    }

    // ------------------------------------ DATA ------------------------------------

    //request photo for a specific real estate and put an observer to update photos
    private void getRealEstatesPhotos(long realEstateId){
        this.mRealEstateViewModel.getRealEstatePhotos(realEstateId).observe(this, this::updateRealEstatePhotos);
    }

    // ----------------------------------- UTILS -----------------------------------

    //configure viewmodel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(getActivity(), mViewModelFactory).get(RealEstateViewModel.class);
    }
    //get from database if one pane layout or from shared viewmodel if two panes layout
    private void getRealEstateToConfigure(){
        mRealEstateRecyclerView = getActivity().findViewById(R.id.real_estate_recycler_view);
        if(mRealEstateRecyclerView != null){//two panes layout
            isTwoPanesLayout = true;
        }
        mRealEstateViewModel.getSelected().observe(this, item -> {
            mRealEstate = item;
            if (mRealEstate!=null) {
                configureDetails(mRealEstate);
            }
        });
    }
    //notify adapter for the new list of photos
    private void updateRealEstatePhotos(List<Photo> realEstatePhotos) {
        if (mRealEstateRecyclerView == null) {//one pane layout
            mPhotoViewPagerAdapter.updatePhotos(realEstatePhotos);
        } else {//two panes layout
            mRecyclerViewPhotoAdapter.updatePhotos(realEstatePhotos);
        }
    }

}
