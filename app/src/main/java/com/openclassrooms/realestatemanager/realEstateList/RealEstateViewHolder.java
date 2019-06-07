package com.openclassrooms.realestatemanager.realEstateList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_cardview) ImageView mRealEstatePhoto;
    @BindView(R.id.type_cardview) TextView mRealEstateType;
    @BindView(R.id.price_cardview) TextView mRealEstatePrice;
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
        this.mRealEstatePrice.setText(mRealEstatePrice.getContext().getResources().getString((R.string.real_estate_price),
                        realEstate.getRealEstate().getPrice(),
                        mRealEstatePrice.getContext().getResources().getString((R.string.real_estate_price_euro))));
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

}
