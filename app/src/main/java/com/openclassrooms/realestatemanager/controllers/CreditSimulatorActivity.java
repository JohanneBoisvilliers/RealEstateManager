package com.openclassrooms.realestatemanager.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreditSimulatorActivity extends AppCompatActivity {

    @BindView(R.id.price_container)
    EditText mRealEstatePriceEditText;
    @BindView(R.id.contribution_container)
    EditText mUserContribution;
    @BindView(R.id.rate_container)
    EditText mRateInput;
    @BindView(R.id.years_container)
    EditText mYearsInput;
    @BindView(R.id.btn_submit)
    Button mSubmitBtn;
    @BindView(R.id.result_under_submit_btn)
    TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_simulator);
        ButterKnife.bind(this);

        mRealEstatePriceEditText.setText(String.valueOf(getIntent().getIntExtra("realEstatePrice"
                , 0)));
    }
}
