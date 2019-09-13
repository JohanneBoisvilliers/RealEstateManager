package com.openclassrooms.realestatemanager.realEstateList;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.utils.getPrice;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateViewHolder extends RecyclerView.ViewHolder implements getPrice {

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
        Glide.with(mRealEstatePhoto.getContext())
                .load(realEstate.getPhotoList().get(0).getUrl())
                .centerCrop()
                .into(mRealEstatePhoto);
        this.mRealEstateType.setText(realEstate.getRealEstate().getCategory());
        setRealEstatePrice(realEstate, mRealEstatePrice);
        if (realEstate.getRealEstate().getSold()) {
            mSoldOut.setVisibility(View.VISIBLE);
        }else{
            mSoldOut.setVisibility(View.INVISIBLE);
        }
    }
}
