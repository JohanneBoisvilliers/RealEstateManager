package com.openclassrooms.realestatemanager.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;

public interface getPrice {

    //method to call to get the good price depending to settings
    public default void setRealEstatePrice(RealEstateWithPhotos realEstateWithPhotos,
                                           TextView textView) {
        textView.setText(textView.getContext().getResources().getString((R.string.real_estate_price),
                this.getPriceDependingCurrencyChosen(realEstateWithPhotos, textView),
                checkCurrency(textView)));
    }

    //check in shared preferences if user want to see real estate price in dollars or euros
    public default String checkCurrency(TextView textView) {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(textView.getContext());
        if (pref.getString("realEstateCurrency", "Dollars").equals("Euros")) {
            return textView.getContext().getResources().getString((R.string.real_estate_price_euro));
        } else {
            return textView.getContext().getResources().getString((R.string.real_estate_price_dollar));
        }
    }

    //convert price depending to exchange rate
    public default int convertPrice(TextView textView, int actualPrice) {
        if (checkCurrency(textView).equals(textView.getContext().getResources().getString((R.string.real_estate_price_euro)))) {
            return Utils.convertEuroToDollars(actualPrice);
        } else {
            return Utils.convertDollarToEuro(actualPrice);
        }
    }

    //check if currency chosen is euros. If it is convert price to get dollars convert in euros
    public default int getPriceDependingCurrencyChosen(RealEstateWithPhotos realEstateWithPhotos, TextView textView) {
        if (checkCurrency(textView).equals(textView.getContext().getResources().getString((R.string.real_estate_price_euro)))) {
            return convertPrice(textView,
                    realEstateWithPhotos.getRealEstate().getPrice());
        } else {
            return realEstateWithPhotos.getRealEstate().getPrice();
        }
    }
}
