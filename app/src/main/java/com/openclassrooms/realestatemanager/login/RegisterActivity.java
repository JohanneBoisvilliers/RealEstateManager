package com.openclassrooms.realestatemanager.login;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.register_btn)
    Button mRegisterButton;
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
                Toast.makeText(getBaseContext(),"USER INSERTED",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createUserInDatabase(){
        User user = new User();
        user.setPassword("testpass");
        user.setUsername("testuser");
        mUserViewModel.createUser(user);
    }
}
