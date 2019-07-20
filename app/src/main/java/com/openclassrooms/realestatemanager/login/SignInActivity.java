package com.openclassrooms.realestatemanager.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.MainActivity;
import com.openclassrooms.realestatemanager.utils.AgentAsyncTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements AgentAsyncTask.Listeners {

    @BindView(R.id.textInputEditTextUsernameLogin)
    TextInputEditText mUsername;
    @BindView(R.id.textInputEditTextMdpLogin)
    TextInputEditText mPassword;
    @BindView(R.id.signin_btn)
    Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        this.listenerOnSignInButton();
    }

    private void listenerOnSignInButton() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("debug", "onClick: ");
                startAsync();
            }
        });
    }

    //launch async task to check if user exist in database
    private void startAsync() {
        new AgentAsyncTask(this, mUsername.getText().toString(),
                mPassword.getText().toString()).execute();
    }

    @Override
    public void onPreExecute() {
    }

    @Override
    public void doInBackground() {

    }

    @Override
    public void onPostExecute(Long success) {
        if (success == 1) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "ERREUR", Toast.LENGTH_LONG).show();
        }
    }
}
