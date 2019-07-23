package com.openclassrooms.realestatemanager.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    public static final int REGISTER_REQUEST = 1;
    @BindView(R.id.signup_btn)Button mSignUpButton;
    @BindView(R.id.login_btn)
    Button mSignInButton;
    @BindView(R.id.coordinator_login)
    View mCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        this.listenerForSignUpButton();
        this.listenerForSignInButton();
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

    // --------------- //
    // ---LISTENERS--- //
    // --------------- //

    //launch activity to register a new user
    private void listenerForSignUpButton(){
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUpActivity();
            }
        });
    }

    //TODO progressbar le temps de chercher dans la BD
    //launch activity for login
    private void listenerForSignInButton() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignInActivity();
            }
        });
    }

    // ----------- //
    // ---UTILS--- //
    // ----------- //

    private void launchSignUpActivity(){
        startActivityForResult(new Intent(this,RegisterActivity.class),REGISTER_REQUEST);
    }
    private void launchSignInActivity() {
        startActivity(new Intent(this, SignInActivity.class));
    }
}
