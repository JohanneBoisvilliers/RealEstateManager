package com.openclassrooms.realestatemanager.realEstateList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.os.Environment.getExternalStoragePublicDirectory;

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

    public void updateRealEstateCardView(RealEstate realEstate){
        if (getPhotoFromUri(realEstate)!=null) {
            this.mRealEstatePhoto.setImageBitmap(getPhotoFromUri(realEstate));
        }
        this.mRealEstateType.setText(realEstate.getCategory());
        this.mRealEstatePrice.setText(mRealEstatePrice.getContext().getResources().getString((R.string.real_estate_price),
                        realEstate.getPrice(),
                        mRealEstatePrice.getContext().getResources().getString((R.string.real_estate_price_euro))));
        if (realEstate.getSold()) {
            mSoldOut.setVisibility(View.VISIBLE);
        }else{
            mSoldOut.setVisibility(View.INVISIBLE);
        }
    }

    public Bitmap getPhotoFromUri(RealEstate realEstate){
        if (realEstate.getPhotoList().size()>0){
            return BitmapFactory.decodeFile(realEstate.getPhotoList().get(0));
        }else{
            return null;
        }
    }

}
