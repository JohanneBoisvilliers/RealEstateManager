package com.openclassrooms.realestatemanager.realEstateList;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_cardview) ImageView mRealEstatePhoto;
    @BindView(R.id.type_cardview) TextView mRealEstateType;
    @BindView(R.id.price_cardview) TextView mRealEstatePrice;
    @Nullable
    @BindView(R.id.fond_transparent)
    ImageView mTransparentBackground;
    @BindView(R.id.line_separator)
    View mLineSeparator;
    @Nullable
    @BindView(R.id.sold_out_bg) View mSoldOut;

    public RealEstateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updateRealEstateCardView(RealEstateWithPhotos realEstate){
        if (getPhotoFromUri(realEstate)!=null) {
            this.mRealEstatePhoto.setImageBitmap(getPhotoFromUri(realEstate));
        }
        this.mRealEstateType.setText(realEstate.getRealEstate().getCategory());
        this.setRealEstatePrice(realEstate);
        if (realEstate.getRealEstate().getSold()) {
            mSoldOut.setVisibility(View.VISIBLE);
        }else{
            mSoldOut.setVisibility(View.INVISIBLE);
        }
    }

    public Bitmap getPhotoFromUri(RealEstateWithPhotos realEstate){
        if (realEstate.getPhotoList().size()>0){
            return BitmapFactory.decodeFile(realEstate.getPhotoList().get(0).getUrl());
        }else{
            return null;
        }
    }

    //check in shared preferences if user want to see real estate price in dollars or euros
    private String checkCurrency() {
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
        if (pref.getString("realEstateCurrency", "Dollars").equals("Euros")) {
            return mRealEstatePrice.getContext().getResources().getString((R.string.real_estate_price_euro));
        } else {
            return mRealEstatePrice.getContext().getResources().getString((R.string.real_estate_price_dollar));
        }
    }

    private int convertPrice(int actualPrice) {
        if (checkCurrency().equals(mRealEstatePrice.getContext().getResources().getString((R.string.real_estate_price_euro)))) {
            return Utils.convertEuroToDollars(actualPrice);
        } else {
            return Utils.convertDollarToEuro(actualPrice);
        }
    }

    private void setRealEstatePrice(RealEstateWithPhotos realEstateWithPhotos) {
        this.mRealEstatePrice.setText(mRealEstatePrice.getContext().getResources().getString((R.string.real_estate_price),
                this.getPriceDependingCurrencyChosen(realEstateWithPhotos),
                this.checkCurrency()));
    }

    private int getPriceDependingCurrencyChosen(RealEstateWithPhotos realEstateWithPhotos) {
        if (checkCurrency().equals(mRealEstatePrice.getContext().getResources().getString((R.string.real_estate_price_euro)))) {
            return convertPrice(realEstateWithPhotos.getRealEstate().getPrice());
        } else {
            return realEstateWithPhotos.getRealEstate().getPrice();
        }
    }
}
