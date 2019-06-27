package com.openclassrooms.realestatemanager.controllers;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityAddArealEstateBinding;
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
    @BindView(R.id.price_edittext)
    EditText mPriceEditText;
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
    private String mSpinnerValue;
    private int mPriceValue;
    private int mNumberOfRooms;
    private String mDescriptionValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddArealEstateBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_areal_estate);
        ButterKnife.bind(this);

        this.configureViewModel();
        binding.setViewmodel(mRealEstateViewModel);

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
        mRealEstate.setCategory(mSpinnerValue);
        mRealEstate.setPrice(mPriceValue);
        mRealEstate.setNbreOfRoom(mNumberOfRooms);
        mRealEstate.setDescription(mDescriptionValue);
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
    }
    //Set a listener on spinner and extrude type of realEstate
    private void getSpinnerInfo() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSpinnerItemChanged(parent.getItemAtPosition(position).toString(), position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //on spinner change, set the type of real estate with "mSpinnerValue" and add data in viewmodel to keep spinner position when user rotate the screen
    private void onSpinnerItemChanged(String itemValue, int itemPosition) {
        mSpinnerValue = itemValue;
        mRealEstateViewModel.mSpinnerPos.set(itemPosition);
    }
    //listener for price edittext, set price in viewmodel's datas
    @OnTextChanged(value = R.id.price_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void priceChanged(CharSequence text) {
        mPriceEditText.setSelection(mPriceEditText.getText().length());
        if (text.length() < 1) {
            mPriceValue = 0;
        } else {
            mRealEstateViewModel.mPrice.set(Integer.parseInt(text.toString()));
            mPriceValue = Integer.parseInt(text.toString());
        }
    }
    //listener for rooms edittext, set number of rooms in viewmodel's datas
    @OnTextChanged(value = R.id.rooms_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void numberOfRoomsChanged(CharSequence text) {
        mPriceEditText.setSelection(mPriceEditText.getText().length());
        if (text.length() < 1) {
            mNumberOfRooms = 0;
        } else {
            mRealEstateViewModel.mRooms.set(Integer.parseInt(text.toString()));
            mNumberOfRooms = Integer.parseInt(text.toString());
        }
    }

    @OnTextChanged(value = R.id.description_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void descriptionChanged(CharSequence text) {
        mRealEstateViewModel.mDescription.set(text.toString());
        mDescriptionValue = text.toString();
    }
}
