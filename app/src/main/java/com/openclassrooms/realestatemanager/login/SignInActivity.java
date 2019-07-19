package com.openclassrooms.realestatemanager.login;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.MainActivity;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.textInputEditTextUsernameLogin)
    TextInputEditText mUsername;
    @BindView(R.id.textInputEditTextMdpLogin)
    TextInputEditText mPassword;
    @BindView(R.id.signin_btn)
    Button mSignInButton;
    UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        this.configureViewModel();
        this.listenerOnSignInButton();
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this);
        mUserViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    private void listenerOnSignInButton() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }
}
