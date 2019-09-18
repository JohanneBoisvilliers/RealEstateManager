package com.openclassrooms.realestatemanager.controllers;

import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class CreditSimulatorActivity extends AppCompatActivity {

    @BindView(R.id.coordinator_loan_simulator)
    CoordinatorLayout mCoordinatorLayout;
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

    private int mContributionValue;
    private int mYearsValue;
    private double mRateValue;
    private int mPriceValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_simulator);
        ButterKnife.bind(this);

        mRealEstatePriceEditText.setText(String.valueOf(getIntent().getIntExtra("realEstatePrice"
                , 0)));
        mPriceValue = getIntent().getIntExtra("realEstatePrice"
                , 0);
        this.listenerOnSubmitBtn();
    }

    // ----------------------------------- UTILS -----------------------------------
    private Boolean isFieldOk() {
        if (!TextUtils.isEmpty(mRealEstatePriceEditText.getText().toString()) &&
                !TextUtils.isEmpty(mRateInput.getText().toString()) &&
                !TextUtils.isEmpty(mYearsInput.getText().toString()) &&
                !TextUtils.isEmpty(mUserContribution.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    private double monthlyCost(int price, double rate, int years, int contribution) {
        return ((price - contribution) * (rate / 100 / 12))
                / (1 - (Math.pow(1 + (rate / 100 / 12), -(years * 12))));
    }

    private double totalCost(int years, double monthlyCost, int price) {
        return (12 * years * monthlyCost) - price;
    }

    // ---------------------------------- LISTENERS ----------------------------------
    @OnTextChanged(value = R.id.contribution_container, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void contributionChanged(CharSequence text) throws NumberFormatException {
        try {
            mContributionValue = Integer.parseInt(text.toString());
        } catch (NumberFormatException e) {
            Log.w("EditText length", "contributionChanged: wrong value! ");
        }
    }

    @OnTextChanged(value = R.id.years_container, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void yearsChanged(CharSequence text) throws NumberFormatException {
        try {
            mYearsValue = Integer.parseInt(text.toString());
        } catch (NumberFormatException e) {
            Log.w("EditText length", "yearsChanged: wrong value! ");
        }
    }

    @OnTextChanged(value = R.id.rate_container, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void rateChanged(CharSequence text) throws NumberFormatException {
        try {
            mRateValue = Double.parseDouble(text.toString());
        } catch (NumberFormatException e) {
            Log.w("EditText length", "rateChanged: wrong value! ");
        }
    }

    @OnTextChanged(value = R.id.price_container, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void priceChanged(CharSequence text) throws NumberFormatException {
        try {
            mPriceValue = Integer.parseInt(text.toString());
        } catch (NumberFormatException e) {
            Log.w("EditText length", "priceChanged: wrong value! ");
        }
    }

    private void listenerOnSubmitBtn() {
        mSubmitBtn.setOnClickListener(v -> {
            if (isFieldOk()) {
                mResult.setVisibility(View.VISIBLE);
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(2);
                String total = numberFormat.format(totalCost(mYearsValue,
                        monthlyCost(mPriceValue, mRateValue, mYearsValue, mContributionValue),
                        (mPriceValue - mContributionValue)));
                String month = numberFormat.format(monthlyCost(mPriceValue, mRateValue, mYearsValue, mContributionValue));
                mResult.setText(getString((R.string.result_loan_simulator), total, month));
            } else {
                Utils.showSnackBar(mCoordinatorLayout, getString(R.string.fields_error), BaseTransientBottomBar.LENGTH_LONG);
            }
        });
    }
}
