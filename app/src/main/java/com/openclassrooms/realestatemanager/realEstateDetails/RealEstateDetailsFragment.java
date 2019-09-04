package com.openclassrooms.realestatemanager.realEstateDetails;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.AddARealEstateActivity;
import com.openclassrooms.realestatemanager.login.RegisterActivity;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.utils.getPrice;

import java.io.Serializable;
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
    @BindView(R.id.btShowmore) Button mButtonMoreDescription;
    @BindView(R.id.btnLocationMore) Button mButtonMoreLocation;
    @Nullable
    @BindView(R.id.modify_button_tablet_mode)
    Button mModifyTabletMode;
    @Nullable
    @BindView(R.id.sold_button_tablet_mode)
    Button mSetSoldTabletMode;
    @Nullable
    @BindView(R.id.simul_button_tablet_mode)
    Button mSimulTabletMode;
    @BindView(R.id.information_surface) TextView mInformationSurface;
    @BindView(R.id.information_room) TextView mInformationRoom;
    @BindView(R.id.information_agent) TextView mInformationAgent;
    @BindView(R.id.point_of_interest)
    TextView mPoIField;
    @BindView(R.id.information_starting_date)
    TextView mUpForSale;
    @BindView(R.id.information_sold)
    TextView mSoldSince;
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
    @BindView(R.id.map)
    ImageView mMapContainer;
    @Nullable
    @BindView(R.id.background_start) ImageView mBackgroundWhenStarting;
    @Nullable
    @BindView(R.id.sold_out_bg) View mSoldOut;

    private static final String TAG = "DEBUG";
    public static final int MODIFY_REQUEST = 1234;
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
            this.configureModifyTabletMode();
            this.configureSoldTabletMode();
            this.configureSimulTabletMode();
        }else{
            this.configureViewPager();
            this.configureFABMenu();
            this.configureModifyButton();
        }
        this.configureExpandDescription();
        this.configureExpandLocation();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MODIFY_REQUEST && resultCode == RegisterActivity.RESULT_OK) {
            if (data != null) {
                //TODO gerer les 2 mode tablette et tel
                long realEstateId = data.getLongExtra("realEstateId", 0);
                mRealEstateViewModel.getSpecificEstate(realEstateId).observe(getActivity(),
                        item -> {
                            mRealEstateViewModel.select(item);
                            getRealEstateToConfigure();
                        });

            }
        }
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
        ItemClickSupport.addTo(mRecyclerViewForPhotos, R.layout.fragment_real_estate_list)
                .setOnItemClickListener((recyclerView1, position, v) -> {
                    FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                    Fragment prev = getChildFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);

                    // Create and show the dialog.
                    GalleryDialog dialogFragment = new GalleryDialog();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("photos", (Serializable) mRealEstatePhotos);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(ft, "dialog");
                });

    }
    //get RealEstate from Activity and configure view details in fragment
    private void configureDetails(RealEstateWithPhotos realEstate) {
        if (realEstate != null) {
            this.getRealEstatesPhotos(realEstate.getRealEstate().getId());
            mRealEstate = realEstate;
            updateRealEstatePhotos(mRealEstatePhotos);
            this.getMap(mRealEstate.getRealEstate().getAddress());
            this.getUserInCharge();
            mRealEstateCategory.setText(realEstate.getRealEstate().getCategory());
            setRealEstatePrice(realEstate, mRealEstatePrice);
            mRealEstateDescription.setText(realEstate.getRealEstate().getDescription());
            mRealEstateDescriptionFade.setText(realEstate.getRealEstate().getDescription());
            mInformationSurface.setText(getResources().getString((R.string.real_estate_surface), realEstate.getRealEstate().getSurface()));
            mInformationRoom.setText(getResources().getString((R.string.real_estate_room),
                    realEstate.getRealEstate().getNbreOfRoom()));
            mPoIField.setText(getResources().getString((R.string.point_of_interest),
                    realEstate.getRealEstate().getPointsOfInterest()));
            mUpForSale.setText(getResources().getString((R.string.real_estate_starting_date),
                    realEstate.getRealEstate().getUpForSale()));
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
    //Hide or show sold out img depending sold state
    private void setSoldState(RealEstate realEstate) {
        if (mSoldOut != null) {
            if (realEstate.getSold()) {
                mSoldOut.setVisibility(View.VISIBLE);
                mSoldSince.setText(getResources().getString((R.string.real_estate_sold),
                        realEstate.getSoldSince()));
            } else {
                mSoldOut.setVisibility(View.INVISIBLE);
                mSoldSince.setText(R.string.real_estate_not_sold);
            }
        }
    }

    // ---------------------------------- LISTENERS ----------------------------------

    //set a click listener to open FAB menu
    private void configureFABMenu(){
        mMainFAB.setOnClickListener(view -> {
            if (!isFABOpen) {
                showFABMenu();
                configureFABchangeStatus();
            } else {
                closeFABMenu();
            }
        });
    }
    //set a listener on sold button to change sold status
    private void configureFABchangeStatus(){
        mStatusFAB.setOnClickListener(view -> changeRealEstateStatus());
    }
    //configure button to expand real estate location
    private void configureExpandLocation() {
        mButtonMoreLocation.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(mRealEstate.getRealEstate().getAddress())
                    .setTitle("Address");
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
    //set a listener on modify button to change differents fields of real estates
    private void configureModifyButton() {
        mModifyEstate.setOnClickListener(v -> startModifyActivity());
    }
    //we don't have fab in tablet mode so we have to set a button to modify a real estate
    private void configureModifyTabletMode() {
        DrawableCompat.setTint(mModifyTabletMode.getCompoundDrawables()[1],
                ContextCompat.getColor(getActivity(), R.color.primaryTextColor));
        mModifyTabletMode.setOnClickListener(view -> startModifyActivity());
    }
    private void configureSoldTabletMode() {
        DrawableCompat.setTint(mSetSoldTabletMode.getCompoundDrawables()[1],
                ContextCompat.getColor(getActivity(), R.color.primaryTextColor));
        mSetSoldTabletMode.setOnClickListener(view -> changeRealEstateStatus());
    }
    private void configureSimulTabletMode() {
        DrawableCompat.setTint(mSimulTabletMode.getCompoundDrawables()[1],
                ContextCompat.getColor(getActivity(), R.color.primaryTextColor));
    }

    // ------------------------------------ DATA ------------------------------------

    //request photo for a specific real estate and put an observer to update photos
    private void getRealEstatesPhotos(long realEstateId){
        this.mRealEstateViewModel.getRealEstatePhotos(realEstateId).observe(this, this::updateRealEstatePhotos);
    }
    private void getMap(String address) {
        if (TextUtils.isEmpty(address) || address.equals("null")) {
            Glide.with(this).load(getResources().getDrawable(R.drawable.background_start)).centerInside().into(mMapContainer);
        } else {
            Glide.with(this).load("https://maps.googleapis.com/maps/api/staticmap?markers=" +
                    address +
                    "zoom=12&size=400x400&key=" + getString(R.string.APIKEY))
                    .centerCrop()
                    .into(mMapContainer);
        }
    }
    //get user who is in charge of this estate
    private void getUserInCharge() {
        mRealEstateViewModel.getUser(mRealEstate.getRealEstate().getUserId()).observe(this,
                user -> mInformationAgent.setText(user.getUsername()));
    }

    // ----------------------------------- UTILS -----------------------------------

    //configure viewmodel
    private void configureViewModel() {
        this.mRealEstateViewModel = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);
    }
    //get from database if one pane layout or from shared viewmodel if two panes layout
    private void getRealEstateToConfigure() throws NullPointerException {
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
            mRealEstatePhotos.clear();
            mRealEstatePhotos.addAll(realEstatePhotos);
        }
    }
    //change the sold status
    private void changeRealEstateStatus() {
        Boolean isSold = mRealEstate.getRealEstate().getSold();
        isSold = !isSold;
        mRealEstate.getRealEstate().setSold(isSold);
        mRealEstate.getRealEstate().setSoldSince(Utils.getTodayDate());
        mRealEstateViewModel.updateItem(mRealEstate.getRealEstate());
        setSoldState(mRealEstate.getRealEstate());
    }
    private void startModifyActivity() {
        Intent intent = new Intent(getContext(), AddARealEstateActivity.class);
        intent.putExtra("comefrom", getClass().getSimpleName());
        intent.putExtra("realEstateId", mRealEstate.getRealEstate().getId());
        startActivityForResult(intent, MODIFY_REQUEST);
    }
}
