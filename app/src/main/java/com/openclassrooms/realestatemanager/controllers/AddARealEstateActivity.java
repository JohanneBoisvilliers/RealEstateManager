package com.openclassrooms.realestatemanager.controllers;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityAddArealEstateBinding;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.login.RegisterActivity;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.SingletonSession;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

import static com.openclassrooms.realestatemanager.utils.MyApp.getContext;

public class AddARealEstateActivity extends AppCompatActivity {

    public static final String TAG = "DEBUG";
    @BindView(R.id.explanation_text)
    TextView mExplanationText;
    @BindView(R.id.price_edittext)
    EditText mPriceEditText;
    @BindView(R.id.surface_edittext)
    EditText mSurfaceEditText;
    @BindView(R.id.rooms_edittext)
    EditText mRoomsEditText;
    @BindView(R.id.address_editText)
    EditText mAddressEditText;
    @BindView(R.id.number_of_photo_picked)
    TextView mNumberOfPhoto;
    @BindView(R.id.description_edittext)
    EditText mDescriptionEditText;
    @BindView(R.id.spinner_realestate_type)
    Spinner mSpinner;
    @BindView(R.id.image_header)
    ImageView mHeader;
    @BindView(R.id.user_photo)
    ImageView mUserPhoto;
    @BindView(R.id.fab_add_real_estate)
    FloatingActionButton mFABAddRealEstate;
    @BindView(R.id.photo_from_device)
    ViewGroup mGetPhotoFromDevice;
    @BindView(R.id.take_photo)
    ViewGroup mTakePhoto;
    @BindView(R.id.coordinator_add_a_realEstate)
    View mCoordinator;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_GALLARY = 2;
    private List<String> mImageEncodedList = new ArrayList<>();
    private List<String> mGalleryPhotos = new ArrayList<>();
    private List<String> mCameraPhotos = new ArrayList<>();
    private Photo[] mFinalPhotoList;
    private RealEstateViewModel mRealEstateViewModel;
    private RealEstate mRealEstate;
    private RealEstateWithPhotos mRealEstateWithPhotos;
    private String mSpinnerValue;
    private String mAddresValue;
    private String mDescriptionValue;
    private String mImageFilePath;
    private String mComeFrom;
    private int mPriceValue;
    private int mSurfaceValue;
    private int mNumberOfRooms;

    // -------------------------------- LIFE CYCLE --------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddArealEstateBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_areal_estate);
        ButterKnife.bind(this);

        this.configureViewModel();
        binding.setViewmodel(mRealEstateViewModel);

        Utils.configureImageHeader(this, mHeader);

        this.configureUser();
        this.configureTypeSpinner();
        this.getSpinnerInfo();
        this.listenerOnFAB();
        this.listenerOnGetPhotoDevice();
        this.listenerOnTakePhoto();
        this.mComeFrom = getIntent().getStringExtra("comefrom");
        this.getRealEstateForModifyFunction(mComeFrom, savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        String imageEncoded = "";
        switch (requestCode) {
            case PICK_FROM_GALLARY:
                mGalleryPhotos.clear();
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.getData() != null) {
                        Uri outPutUri = data.getData();
                        this.extrudeUrlFromGallery(filePathColumn, imageEncoded, outPutUri);
                    } else {
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                this.extrudeUrlFromGallery(filePathColumn, imageEncoded, uri);
                            }
                        }
                    }
                    mImageEncodedList.clear();
                    mImageEncodedList.addAll(mCameraPhotos);
                    mImageEncodedList.addAll(mGalleryPhotos);
                    mRealEstateViewModel.numberOfPhoto.set(getResources().getString((R.string.number_of_photo), String.valueOf(mImageEncodedList.size())));
                    mRealEstateViewModel.selecturlList(mImageEncodedList);
                } else {
                    Utils.showSnackBar(mCoordinator,
                            getString(R.string.snack_bar_no_photo),
                            BaseTransientBottomBar.LENGTH_LONG);
                }
                break;

            case PICK_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    mImageEncodedList.clear();
                    mCameraPhotos.add(mImageFilePath);
                    mImageEncodedList.addAll(mCameraPhotos);
                    mImageEncodedList.addAll(mGalleryPhotos);
                    mRealEstateViewModel.numberOfPhoto.set(getResources().getString((R.string.number_of_photo), String.valueOf(mImageEncodedList.size())));
                    mRealEstateViewModel.selecturlList(mImageEncodedList);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Utils.showSnackBar(mCoordinator,
                            getResources().getString(R.string.no_photo),
                            BaseTransientBottomBar.LENGTH_LONG);
                }

                break;
        }
    }

    // ------------------------------------ DATA ------------------------------------

    //configure viewmodel to keep datas
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RealEstateViewModel.class);
        this.mRealEstateViewModel.init(mRealEstateViewModel.getRealEstate());
    }
    //create a realEstate object with all informations fetch from differents widgets(spinner,textview...)
    private void setRealEstateInfos() {
        if (TextUtils.isEmpty(mComeFrom)) {
            mRealEstate = mRealEstateViewModel.getRealEstate();
        }
        mRealEstate.setUserId(SingletonSession.Instance().getUser().getId());
        mRealEstate.setCategory(mSpinnerValue);
        mRealEstate.setPrice(mPriceValue);
        mRealEstate.setSurface(mSurfaceValue);
        mRealEstate.setSold(false);
        mRealEstate.setNbreOfRoom(mNumberOfRooms);
        mRealEstate.setDescription(mDescriptionValue);
        mRealEstate.setAddress(mAddresValue);
    }
    //transform list of photo into array for request
    private void setPhotoForRealEstate(List<String> urlList) {
        ArrayList<Photo> listPhoto = new ArrayList<>();
        for (String url : urlList) {
            Photo photo = new Photo();
            photo.setUrl(url);
            listPhoto.add(photo);
        }
        mFinalPhotoList = new Photo[listPhoto.size()];
        listPhoto.toArray(mFinalPhotoList);
    }
    //in case of existing photo (when we want to modify a real estate)
    private void updatePhotoList(List<Photo> photoList) {
        mFinalPhotoList = new Photo[photoList.size()];
        photoList.toArray(mFinalPhotoList);
    }
    //on spinner change, set the type of real estate with "mSpinnerValue" and add data in viewmodel to keep spinner position when user rotate the screen
    private void onSpinnerItemChanged(String itemValue, int itemPosition) {
        mSpinnerValue = itemValue;
        mRealEstateViewModel.spinnerPos.set(itemPosition);
    }
    //access to gallery app
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
            mGalleryPhotos.add(imageEncoded);
        }
        cursor.close();
    }
    //when user came to this activity for modify a real estate, we get this real estate in database and set UI
    private void getRealEstateForModifyFunction(String comeFrom, Bundle bundle) {
        if (!TextUtils.isEmpty(comeFrom)) {
            this.mRealEstateViewModel
                    .getSpecificEstate(getIntent().getLongExtra("realEstateId", 0))
                    .observe(this, item -> {
                        mRealEstateWithPhotos = item;
                        mRealEstate = item.getRealEstate();
                        mRealEstateViewModel.setRealEstateId(mRealEstate.getId());
                        if (bundle == null) {
                            for (int i = 0; i < mRealEstateWithPhotos.getPhotoList().size(); i++) {
                                mImageEncodedList.add(mRealEstateWithPhotos.getPhotoList().get(i).getUrl());
                                mGalleryPhotos.add(mRealEstateWithPhotos.getPhotoList().get(i).getUrl());
                            }
                        } else {
                            mRealEstateViewModel.getUrlList().observe(this,
                                    list -> mImageEncodedList.addAll(list));
                        }
                        this.configureUIDependingToRealEstate(bundle, mRealEstateWithPhotos, mRealEstate);
                    });
        }
    }

    // ------------------------------------ UI ------------------------------------

    //load image into header with glide
    private void configureUser() {
        //String username = getIntent().getStringExtra("username");
        //Object photoUrl = getIntent().getStringExtra("photoUrl");
        Object photoUrl;
        mExplanationText.setText(getResources().getString((R.string.text_add_realestate),
                SingletonSession.Instance().getUser().getUsername()));
        if (SingletonSession.Instance().getUser().getPhotoUrl() == null) {
            photoUrl = getResources().getDrawable(R.drawable.user);
        } else {
            photoUrl = SingletonSession.Instance().getUser().getPhotoUrl();
        }
        Glide.with(this)
                .load(photoUrl)
                .circleCrop()
                .into(mUserPhoto);
    }
    //configure spinner to choose type of real estate
    private void configureTypeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.type_list,R.layout.custom_item_spinner);
        mSpinner.setAdapter(adapter);
    }

    //fill all the fields to see each real estate features
    private void configureUIDependingToRealEstate(Bundle bundle, RealEstateWithPhotos realEstateWithPhotos, RealEstate realEstate) {
        mRealEstateViewModel.spinnerPos.set(getIndex(mSpinner, realEstate.getCategory()));
        mRealEstateViewModel.price.set(realEstate.getPrice());
        mRealEstateViewModel.surface.set(realEstate.getSurface());
        mRealEstateViewModel.rooms.set(realEstate.getNbreOfRoom());
        mRealEstateViewModel.address.set(realEstate.getAddress());
        if (bundle == null) {
            mRealEstateViewModel.numberOfPhoto.set(getResources().getString((R.string.number_of_photo), String.valueOf(realEstateWithPhotos.getPhotoList().size())));
        }
        mRealEstateViewModel.description.set(realEstate.getDescription());
    }

    // ---------------------------------- LISTENERS ----------------------------------

    //listener on FAB for adding or updating real estate in database
    private void listenerOnFAB() {
        mFABAddRealEstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInfos()) {
                    Log.d(TAG, "onClick: imageEncoded : " + mImageEncodedList.size());
                    Log.d(TAG, "onClick: gallery : " + mGalleryPhotos.size());
                    Log.d(TAG, "onClick: camera :" + mCameraPhotos.size());
                    setPhotoForRealEstate(mImageEncodedList);
                    setRealEstateInfos();
                    mRealEstateViewModel.insertOrUpdate(mRealEstate, mFinalPhotoList);
                    returnToDetailsWithNewInfos();
                    finish();
                } else {
                    Utils.showSnackBar(mCoordinator,
                            //TODO modifier phrase de contrainte
                            getResources().getString(R.string.no_photo),
                            BaseTransientBottomBar.LENGTH_LONG);
                }
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
    //open camera for taking a picture
    private void listenerOnTakePhoto() {
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (captureIntent.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        Log.e(TAG, "onClick: ", ex);
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getContext(), "com.openclassrooms.realestatemanager.provider", photoFile);
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(captureIntent, PICK_FROM_CAMERA);
                    }
                }
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
            mRealEstateViewModel.price.set(Integer.parseInt(text.toString()));
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
            mRealEstateViewModel.rooms.set(Integer.parseInt(text.toString()));
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
            mRealEstateViewModel.surface.set(Integer.parseInt(text.toString()));
            mSurfaceValue = Integer.parseInt(text.toString());
        }
    }
    //listener for address edit text, set description in viewmodel's datas
    @OnTextChanged(value = R.id.address_editText, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void addressChanged(CharSequence text) {
        mRealEstateViewModel.address.set(text.toString());
        mAddresValue = text.toString();
    }
    //listener for description edit text, set description in viewmodel's datas
    @OnTextChanged(value = R.id.description_edittext, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void descriptionChanged(CharSequence text) {
        mRealEstateViewModel.description.set(text.toString());
        mDescriptionValue = text.toString();
    }

    /// ----------------------------------- UTILS -----------------------------------

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mImageFilePath = image.getAbsolutePath();
        return image;
    }
    //check if there are at least one photo and a description before updating database
    private Boolean checkInfos() {
        return (mImageEncodedList != null && mImageEncodedList.size() > 0 && !TextUtils.isEmpty(mDescriptionValue));
    }
    //return de realEstateDetailsFragment after updating real estate
    private void returnToDetailsWithNewInfos() {
        if (mComeFrom != null && mComeFrom.equals("RealEstateDetailsFragment")) {
            Intent intent = new Intent();
            intent.putExtra("realEstateId", mRealEstate.getId());
            setResult(RegisterActivity.SUCCESS, intent);
        }
    }
    //private method of your class
    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }

        }

        return 0;
    }
}
