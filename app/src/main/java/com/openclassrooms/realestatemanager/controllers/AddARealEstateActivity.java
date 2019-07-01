package com.openclassrooms.realestatemanager.controllers;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

import static com.openclassrooms.realestatemanager.utils.MyApp.getContext;

public class AddARealEstateActivity extends AppCompatActivity {

    public static final String TAG = "DEBUG";
    @BindView(R.id.price_edittext)
    EditText mPriceEditText;
    @BindView(R.id.surface_edittext)
    EditText mSurfaceEditText;
    @BindView(R.id.rooms_edittext)
    EditText mRoomsEditText;
    @BindView(R.id.spinner_realestate_type)
    Spinner mSpinner;
    @BindView(R.id.image_header)
    ImageView mHeader;
    @BindView(R.id.user_photo)
    ImageView mUserPhoto;
    @BindView(R.id.fab_add_real_estate)
    FloatingActionButton mFABAddRealEstate;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLARY = 2;
    @BindView(R.id.photo_from_device)
    ViewGroup mGetPhotoFromDevice;
    @BindView(R.id.coordinator_add_a_realEstate)
    View mCoordinator;
    private List<String> mImageEncodedList;
    private RealEstateViewModel mRealEstateViewModel;
    private RealEstate mRealEstate;
    private String mSpinnerValue;
    private String mDescriptionValue;
    private int mPriceValue;
    private int mSurfaceValue;
    private int mNumberOfRooms;


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
        this.listenerOnGetPhotoDevice();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case PICK_FROM_GALLARY:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    String imageEncoded = "";
                    if (data.getData() != null) {
                        Uri outPutUri = data.getData();
                        this.extrudeUrlFromGallery(filePathColumn, imageEncoded, outPutUri);
                    } else {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            mImageEncodedList = new ArrayList<>();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                this.extrudeUrlFromGallery(filePathColumn, imageEncoded, uri);
                                mImageEncodedList.add(imageEncoded);
                            }
                        }
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinator, getString(R.string.snack_bar_no_photo), BaseTransientBottomBar.LENGTH_LONG);
                    snackbar.show();
                }
                break;

            case PICK_FROM_CAMERA:
                break;
        }
    }

    // --- DATA

    //configure viewmodel to keep datas
    private void configureViewModel() {
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
        mRealEstate.setSurface(mSurfaceValue);
        mRealEstate.setSold(false);
        mRealEstate.setNbreOfRoom(mNumberOfRooms);
        mRealEstate.setDescription(mDescriptionValue);
    }

    //on spinner change, set the type of real estate with "mSpinnerValue" and add data in viewmodel to keep spinner position when user rotate the screen
    private void onSpinnerItemChanged(String itemValue, int itemPosition) {
        mSpinnerValue = itemValue;
        mRealEstateViewModel.mSpinnerPos.set(itemPosition);
    }

    //acces to gallery app
    private void extrudeUrlFromGallery(String[] filePathColumn, String imageEncoded, Uri uri) {
        String fileId = DocumentsContract.getDocumentId(uri);
        String id = fileId.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String selector = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, selector, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        if (cursor.moveToFirst()) {
            imageEncoded = cursor.getString(columnIndex);
        }
        cursor.close();
    }

    // --- UI

    //load image into header with glide
    private void configureImageHeader() {
        Glide.with(this)
                .load(getResources().getDrawable(R.drawable.gradient_bleu))
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

    // --- LISTENERS

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

    //open photo gallery to pick real estate photos
    private void listenerOnGetPhotoDevice() {
        mGetPhotoFromDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_FROM_GALLARY);
            }
        });
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
        mRoomsEditText.setSelection(mRoomsEditText.getText().length());
        if (text.length() < 1) {
            mNumberOfRooms = 0;
        } else {
            mRealEstateViewModel.mRooms.set(Integer.parseInt(text.toString()));
            mNumberOfRooms = Integer.parseInt(text.toString());
        }
    }

    //listener for surface edittext, set surface in viewmodel's datas
    @OnTextChanged(value = R.id.surface_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void surfaceChanged(CharSequence text) {
        mSurfaceEditText.setSelection(mSurfaceEditText.getText().length());
        if (text.length() < 1) {
            mSurfaceValue = 0;
        } else {
            mRealEstateViewModel.mSurface.set(Integer.parseInt(text.toString()));
            mSurfaceValue = Integer.parseInt(text.toString());
        }
    }

    //listener for description edit text, set description in viewmodel's datas
    @OnTextChanged(value = R.id.description_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void descriptionChanged(CharSequence text) {
        mRealEstateViewModel.mDescription.set(text.toString());
        mDescriptionValue = text.toString();
    }
}
