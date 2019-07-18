package com.openclassrooms.realestatemanager.login;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.facebook.stetho.Stetho;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.MainActivity;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        Stetho.initializeWithDefaults(this);
        this.configureViewModel();
        this.listenerOnRegisterButton();
    }

    @Override
    public void onBackPressed() {
        this.returnCanceledInfoToLoginActivity();
    }

    private void returnCanceledInfoToLoginActivity (){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void configureViewModel(){
        ViewModelFactory viewModelFactory = Injections.provideViewModelFactory(this);
        mUserViewModel = ViewModelProviders.of(this,viewModelFactory).get(UserViewModel.class);
    }

    private void listenerOnRegisterButton(){
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserInDatabase();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void createUserInDatabase(){
        User user = new User();
        user.setPassword(mUsernameEditText.getText().toString());
        user.setUsername(mPasswordEditText.getText().toString());
        user.setEmail(mEmailEditText.getText().toString());
        mUserViewModel.createUser(user);
    }
}
