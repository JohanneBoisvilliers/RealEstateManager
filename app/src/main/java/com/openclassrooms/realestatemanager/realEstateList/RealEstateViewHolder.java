package com.openclassrooms.realestatemanager.realEstateList;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_cardview) ImageView mRealEstatePhoto;
    @BindView(R.id.type_cardview) TextView mRealEstateType;

    public RealEstateViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updateRealEstateCardView(RealEstate realEstate){
        this.mRealEstateType.setText(realEstate.getCategory());
    }

}
