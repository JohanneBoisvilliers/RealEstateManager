package com.openclassrooms.realestatemanager.realEstateDetails;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewDetailsPhotoViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    @BindView(R.id.container_photo_details) ImageView mPhotoContainer;

    public RecyclerViewDetailsPhotoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updatePhotoInRecyclerView(int color){
        mPhotoContainer.setBackgroundColor(color);
    }
}
