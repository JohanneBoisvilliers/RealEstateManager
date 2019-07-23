package com.openclassrooms.realestatemanager.login;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.user_photo_register)
    ImageView mUserPhoto;
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
    private int mCanceled = Activity.RESULT_CANCELED;
    private int mSuccessed = Activity.RESULT_OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        this.configureViewModel();
        Utils.configureUserPhoto(null, this, mUserPhoto);
        this.listenerOnRegisterButton();
    }

    @Override
    public void onBackPressed() {
        this.returnInfoToLoginActivity(mCanceled);
    }

    private void returnInfoToLoginActivity(int returnType) {
        setResult(returnType);
        finish();
    }

    private void configureViewModel(){
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this);
        mUserViewModel = ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
    }
    //add user in database and launch mainActivity
    private void listenerOnRegisterButton(){
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserInDatabase();
                returnInfoToLoginActivity(mSuccessed);
            }
        });
    }
    //create a new user in database and set his params with edittext entries
    private void createUserInDatabase(){
        User user = new User();
        user.setUsername(mUsernameEditText.getText().toString());
        user.setPassword(mPasswordEditText.getText().toString());
        user.setEmail(mEmailEditText.getText().toString());
        mUserViewModel.createUser(user);
    }
}
