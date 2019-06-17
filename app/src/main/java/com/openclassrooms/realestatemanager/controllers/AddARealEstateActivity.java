package com.openclassrooms.realestatemanager.controllers;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.viewModels.AddOrModifyViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class AddARealEstateActivity extends AppCompatActivity {

    public static final String TAG = "DEBUG";
    @BindView(R.id.spinner_realestate_type)
    Spinner mSpinner;
    @BindView(R.id.image_header)
    ImageView mHeader;
    @BindView(R.id.user_photo)
    ImageView mUserPhoto;
    private AddOrModifyViewModel mAddOrModifyViewModel;

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
    }

    //configure viewmodel to keep datas
    private void configureViewModel() {
        mAddOrModifyViewModel = ViewModelProviders.of(this).get(AddOrModifyViewModel.class);
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
        mSpinner.setSelection(mAddOrModifyViewModel.getSpinnerPos());
    }

    //Set a listener on spinner and extrude type of realEstate
    private void getSpinnerInfo() {
        mAddOrModifyViewModel.setSpinnerPos(0);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAddOrModifyViewModel.setSpinnerPos(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //listener for price edittext, set price in viewmodel's datas
    @OnTextChanged(value = R.id.price_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void priceChanged(CharSequence text) {
        try {
            mAddOrModifyViewModel.setPrice(Integer.parseInt(text.toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //listener for rooms edittext, set number of rooms in viewmodel's datas
    @OnTextChanged(value = R.id.rooms_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void numberOfRoomsChanged(CharSequence text) {
        try {
            mAddOrModifyViewModel.setRooms(Integer.parseInt(text.toString()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}
