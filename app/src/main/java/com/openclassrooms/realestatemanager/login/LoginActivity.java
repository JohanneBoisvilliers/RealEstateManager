package com.openclassrooms.realestatemanager.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.MainActivity;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    public static final int REGISTER_REQUEST = 1;
    public static final int REQUEST_CODE_ONE = 0x03;
    @BindView(R.id.signup_btn)Button mSignUpButton;
    @BindView(R.id.login_btn)
    Button mSignInButton;
    @BindView(R.id.coordinator_login)
    View mCoordinator;

    // -------------------------------- LIFE CYCLE --------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        this.checkPermissions();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REGISTER_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
                Utils.showSnackBar(mCoordinator, getString(R.string.registration_success),
                        BaseTransientBottomBar.LENGTH_LONG);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Utils.showSnackBar(mCoordinator, getString(R.string.cancel_registration),
                        BaseTransientBottomBar.LENGTH_LONG);
            }
        }
    }


    // ---------------------------------- LISTENERS ----------------------------------

    //launch activity to register a new user
    private void listenerForSignUpButton(){
        mSignUpButton.setOnClickListener(v -> launchSignUpActivity());
    }
    //launch activity for login
    private void listenerForSignInButton() {
        mSignInButton.setOnClickListener(v -> launchSignInActivity());
    }

    // ----------------------------------- UTILS -----------------------------------

    private void launchSignUpActivity(){
        startActivityForResult(new Intent(this,RegisterActivity.class),REGISTER_REQUEST);
    }
    private void launchSignInActivity() {
        startActivity(new Intent(this, SignInActivity.class));
    }
    private void launchMainActivity(Long userId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userId", userId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            this.checkLastUserLogged();
            this.listenerForSignUpButton();
            this.listenerForSignInButton();
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_ONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ONE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    this.checkLastUserLogged();
                    this.listenerForSignUpButton();
                    this.listenerForSignInButton();
                } else {
                    ActivityCompat.requestPermissions(LoginActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_ONE);
                }
            }
        }
    }

    // ------------------------------------ DATA ------------------------------------

    private void checkLastUserLogged() {
        Long Userid;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Userid = prefs.getLong("userId", 0);

        if (Userid != 0) {
            this.launchMainActivity(Userid);
        }
    }
}
