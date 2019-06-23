package com.openclassrooms.realestatemanager.controllers;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

import static com.openclassrooms.realestatemanager.utils.MyApp.getContext;

public class AddARealEstateActivity extends AppCompatActivity {

    public static final String TAG = "DEBUG";
    @BindView(R.id.spinner_realestate_type)
    Spinner mSpinner;
    @BindView(R.id.image_header)
    ImageView mHeader;
    @BindView(R.id.user_photo)
    ImageView mUserPhoto;
    @BindView(R.id.fab_add_real_estate)
    FloatingActionButton mFABAddRealEstate;
    private RealEstateViewModel mRealEstateViewModel;
    private RealEstate mRealEstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_areal_estate);
        ButterKnife.bind(this);

        this.configureViewModel();
        this.configureImageHeader();
        this.configureUserPhoto(null);
        this.configureTypeSpinner();
        this.getSpinnerInfo();
        this.listenerOnFAB();
    }

    //configure viewmodel to keep datas
    private void configureViewModel() {
        Log.d(TAG, "configureViewModel: ");
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RealEstateViewModel.class);
        this.mRealEstateViewModel.init(mRealEstateViewModel.getRealEstate());
    }

    //create a realEstate object with all informations fetch from differents widgets(spinner,textview...)
    private void setRealEstateInfos() {
        mRealEstate = mRealEstateViewModel.getRealEstate();
        //TODO: récuperer userID automatiquement en fonction de l'utilisateur connecté
        mRealEstate.setUserId(1);
        mRealEstate.setCategory(mSpinner.getSelectedItem().toString());
    }

    //listener on FAB for adding or updating real estate in database
    private void listenerOnFAB() {
        mFABAddRealEstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRealEstateInfos();
                mRealEstateViewModel.insertOrUpdate(mRealEstate);
            }
        });
    }
    //load image into header with glide
    private void configureImageHeader() {
        Glide.with(this)
                .load(getResources().getDrawable(R.drawable.keys))
                .centerCrop()
                .into(mHeader);
    }
    //load image into header with glide
    private void configureUserPhoto(@Nullable Object url) {
        if (url == null) {
            url = getResources().getDrawable(R.drawable.user);
        }
        Glide.with(this)
                .load(url)
                .circleCrop()
                .into(mUserPhoto);
    }
    //configure spinner to choose type of real estate
    private void configureTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.type_list,R.layout.custom_item_spinner);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(mRealEstateViewModel.getSpinnerPos());
    }
    //Set a listener on spinner and extrude type of realEstate
    private void getSpinnerInfo() {
        mRealEstateViewModel.setSpinnerPos(0);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mRealEstateViewModel.setSpinnerPos(position);
                setRealEstateInfos();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    //listener for price edittext, set price in viewmodel's datas
    @OnTextChanged(value = R.id.price_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void priceChanged(CharSequence text) {
        try {
            mRealEstateViewModel.setPrice(Integer.parseInt(text.toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    //listener for rooms edittext, set number of rooms in viewmodel's datas
    @OnTextChanged(value = R.id.rooms_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void numberOfRoomsChanged(CharSequence text) {
        try {
            mRealEstateViewModel.setRooms(Integer.parseInt(text.toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
