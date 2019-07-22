package com.openclassrooms.realestatemanager.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeaderViewHolder {

    @BindView(R.id.username_in_nav)
    TextView mUserNameTxt;
    @BindView(R.id.email_in_nav)
    TextView mUserEmailTxt;
    @BindView(R.id.user_photo_in_nav)
    ImageView mUserPhoto;
    @BindView(R.id.background_header_nav)
    ImageView mBackgroundHeader;

    public HeaderViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

    public TextView getUserNameTxt() {
        return mUserNameTxt;
    }

    public void setUserNameTxt(TextView userNameTxt) {
        mUserNameTxt = userNameTxt;
    }

    public TextView getUserEmailTxt() {
        return mUserEmailTxt;
    }

    public void setUserEmailTxt(TextView userEmailTxt) {
        mUserEmailTxt = userEmailTxt;
    }

    public ImageView getUserPhoto() {
        return mUserPhoto;
    }

    public void setUserPhoto(ImageView userPhoto) {
        mUserPhoto = userPhoto;
    }

    public ImageView getBackgroundHeader() {
        return mBackgroundHeader;
    }

    public void setBackgroundHeader(ImageView backgroundHeader) {
        mBackgroundHeader = backgroundHeader;
    }
}
