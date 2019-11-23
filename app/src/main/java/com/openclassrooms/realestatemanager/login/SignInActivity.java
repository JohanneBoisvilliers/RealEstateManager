package com.openclassrooms.realestatemanager.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.MainActivity;
import com.openclassrooms.realestatemanager.utils.AgentAsyncTask;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity implements AgentAsyncTask.Listeners {

    @BindView(R.id.textInputEditTextUsernameLogin)
    TextInputEditText mUsername;
    @BindView(R.id.textInputEditTextMdpLogin)
    TextInputEditText mPassword;
    @BindView(R.id.signin_btn)
    Button mSignInButton;
    @BindView(R.id.signin_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.coordinator_sign_in)
    View mCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        this.listenerOnSignInButton();
    }

    // ---------------------------------- LISTENERS ----------------------------------

    private void listenerOnSignInButton() {
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAsync();
            }
        });
    }

    // ----------------------------------- ASYNC -----------------------------------

    //launch async task to check if user exist in database
    private void startAsync() {
        new AgentAsyncTask(this, mUsername.getText().toString(),
                mPassword.getText().toString()).execute();
    }
    @Override
    public void onPreExecute() {
        this.updateUIBeforeTask();
    }
    @Override
    public void doInBackground() {
    }
    @Override
    public void onPostExecute(Long success) {
        this.updateUIAfterTask();
        if (success != null && success != 0) {
            startMainActivity(success);
            saveUserAuth(success);
        } else {
            Utils.showSnackBar(mCoordinator, getString(R.string.error), BaseTransientBottomBar.LENGTH_LONG);
        }
    }

    // ------------------------------------ UI ------------------------------------

    //show progress bar and hide button sign in in during the database's checking
    public void updateUIBeforeTask() {
        mProgressBar.setVisibility(View.VISIBLE);
        mSignInButton.setVisibility(View.GONE);
    }
    //hide progress bar and show sign in button when request is over
    public void updateUIAfterTask() {
        mProgressBar.setVisibility(View.GONE);
        mSignInButton.setVisibility(View.VISIBLE);
    }

    // ----------------------------------- UTILS -----------------------------------

    public void startMainActivity(Long requestResult) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("userId", requestResult);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // ------------------------------------ DATA ------------------------------------

    // save user id for auto login function
    public void saveUserAuth(Long requestResult) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putLong("userId", requestResult).commit();
    }

}
