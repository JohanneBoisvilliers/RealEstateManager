package com.openclassrooms.realestatemanager.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    public static final int REGISTER_REQUEST = 1;
    @BindView(R.id.signup_btn)Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        this.listenerForSignUpButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REGISTER_REQUEST) {
            if(resultCode == Activity.RESULT_OK){
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this,"TEST",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void listenerForSignUpButton(){
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUpActivity();
            }
        });
    }

    private void launchSignUpActivity(){
        startActivityForResult(new Intent(this,RegisterActivity.class),REGISTER_REQUEST);
    }
}
