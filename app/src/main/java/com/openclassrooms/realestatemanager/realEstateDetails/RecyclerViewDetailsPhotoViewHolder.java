package com.openclassrooms.realestatemanager.realEstateDetails;

import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Photo;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewDetailsPhotoViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    @BindView(R.id.container_photo_details) ImageView mPhotoContainer;
    @BindView(R.id.description_recyclerview) TextView mPhotoDescription;

    public RecyclerViewDetailsPhotoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updatePhotoInRecyclerView(Photo photo){
        mPhotoContainer.setImageBitmap(BitmapFactory.decodeFile(photo.getUrl()));
        mPhotoDescription.setText(photo.getDescription());

    }
}
