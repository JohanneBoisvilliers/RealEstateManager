package com.openclassrooms.realestatemanager.realEstateDetails;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class RealEstateDetailsFragment extends Fragment {

    @BindView(R.id.real_estate_category) TextView mRealEstateCategory;
    @BindView(R.id.real_estate_price) TextView mRealEstatePrice;
    @BindView(R.id.real_estate_description) TextView mRealEstateDescription;
    @BindView(R.id.real_estate_description_fade) TextView mRealEstateDescriptionFade;
    @BindView(R.id.information_location) TextView mRealEstateLocation;
    @BindView(R.id.btShowmore) Button mButtonMoreDescription;
    @BindView(R.id.btnLocationMore) Button mButtonMoreLocation;
    @BindView(R.id.information_surface) TextView mInformationSurface;
    @BindView(R.id.information_room) TextView mInformationRoom;
    @BindView(R.id.information_agent) TextView mInformationAgent;

    private int mNumberOfLine;

    public RealEstateDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_real_estate_details, container, false);
        ButterKnife.bind(this,view);

        this.configureDetails();
        this.configureExpandDescription();
        this.configureExpandLocation();

        return view;
    }

    //get RealEstate from Activity and configure view details in fragment
    private void configureDetails(){
        RealEstate realestatetoshow = getArguments().getParcelable("realEstate");
        mRealEstateCategory.setText(realestatetoshow.getCategory());
        mRealEstatePrice.setText(String.valueOf(realestatetoshow.getPrice()));
        mRealEstateDescription.setText(realestatetoshow.getDescription());
        mRealEstateDescriptionFade.setText(realestatetoshow.getDescription());
        mInformationSurface.setText(getResources().getString((R.string.real_estate_surface),realestatetoshow.getSurface()));
        mInformationRoom.setText(getResources().getString((R.string.real_estate_room),realestatetoshow.getNbreOfRoom()));
        //mInformationRoom.setText(getResources().getString((R.string.real_estate_agent),realestatetoshow.getUsername()));
    }
    //add a button to open description when it's longer than 3 lines
    private void configureExpandDescription(){
        mRealEstateDescription.post(new Runnable() {
            @Override
            public void run() {
                mNumberOfLine = mRealEstateDescription.getLineCount();
                if (mNumberOfLine>2){
                    mButtonMoreDescription.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (mButtonMoreDescription.getText().toString().equalsIgnoreCase(getResources().getString(R.string.button_more)))
                            {
                                mRealEstateDescription.setMaxLines(Integer.MAX_VALUE);
                                mButtonMoreDescription.setText(getResources().getString(R.string.button_close));
                            }
                            else
                            {
                                mRealEstateDescription.setMaxLines(2);
                                mButtonMoreDescription.setText(getResources().getString(R.string.button_more));
                            }
                        }
                    });
                }else{
                    mButtonMoreDescription.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    //configure button to expand real estate location
    private void configureExpandLocation(){
        mButtonMoreLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mButtonMoreLocation.getText().toString().equalsIgnoreCase(getResources().getString(R.string.button_more)))
                {
                    mRealEstateLocation.setMaxLines(Integer.MAX_VALUE);
                    mButtonMoreLocation.setText(getResources().getString(R.string.button_close));
                }
                else
                {
                    mRealEstateLocation.setMaxLines(1);
                    mButtonMoreLocation.setText(getResources().getString(R.string.button_more));
                }
            }
        });
    }

}
