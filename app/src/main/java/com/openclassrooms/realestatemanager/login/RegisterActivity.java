package com.openclassrooms.realestatemanager.login;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.stetho.Stetho;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.User;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.utils.MyApp.getContext;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.user_photo_register)
    ImageView mUserPhoto;
    public static final String TAG = "DEBUG";
    @BindView(R.id.textInputEditTextUsername)
    TextInputEditText mUsernameEditText;
    @BindView(R.id.textInputEditTextEmail)
    TextInputEditText mEmailEditText;
    @BindView(R.id.textInputEditTextMdp)
    TextInputEditText mPasswordEditText;
    @BindView(R.id.register_btn)
    Button mRegisterButton;
    @BindView(R.id.coordinator_add_user)
    View mCoordinator;
    private UserViewModel mUserViewModel;
    public static final int CANCELED = Activity.RESULT_CANCELED;
    public static final int SUCCESS = Activity.RESULT_OK;
    public static final int PICK_FROM_GALLARY = 1;
    @BindView(R.id.change_photo_btn)
    ImageView mChangeUserPhoto;
    private String mUrlUserPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        this.configureViewModel();
        Utils.configureUserPhoto(null, this, mUserPhoto);
        this.listenerOnEditPhotoBtn();
        this.listenerOnRegisterButton();
    }

    @Override
    public void onBackPressed() {
        this.returnInfoToLoginActivity(CANCELED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        if (requestCode == PICK_FROM_GALLARY && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                Uri outPutUri = data.getData();
                this.extrudeUrlFromGallery(filePathColumn, outPutUri);
            }
            Utils.configureUserPhoto(mUrlUserPhoto, this, mUserPhoto);
        }
    }

    // ----------- //
    // ---UTILS--- //
    // ----------- //

    private void returnInfoToLoginActivity(int returnType) {
        setResult(returnType);
        finish();
    }

    //access to gallery app
    private void extrudeUrlFromGallery(String[] filePathColumn, Uri uri) {
        String fileId = DocumentsContract.getDocumentId(uri);
        String id = fileId.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String selector = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, selector, new String[]{id}, null);
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        if (cursor.moveToFirst()) {
            mUrlUserPhoto = cursor.getString(columnIndex);
        }
        cursor.close();
    }

    private void configureViewModel(){
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this);
        mUserViewModel = ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
    }

    // --------------- //
    // ---LISTENERS--- //
    // --------------- //

    //add user in database and launch mainActivity
    private void listenerOnRegisterButton(){
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserInDatabase();
                returnInfoToLoginActivity(SUCCESS);
            }
        });
    }

    //open photo gallery to pick real estate photos
    private void listenerOnEditPhotoBtn() {
        mChangeUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), PICK_FROM_GALLARY);
            }
        });
    }

    // ---------- //
    // ---DATA--- //
    // ---------- //

    //create a new user in database and set his params with edittext entries
    private void createUserInDatabase(){
        User user = new User();
        user.setUsername(mUsernameEditText.getText().toString());
        user.setPassword(mPasswordEditText.getText().toString());
        user.setEmail(mEmailEditText.getText().toString());
        if (mUrlUserPhoto != null) {
            user.setPhotoUrl(mUrlUserPhoto);
        }
        mUserViewModel.createUser(user);
    }
}
