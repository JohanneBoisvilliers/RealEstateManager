package com.openclassrooms.realestatemanager.realEstateList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.POWER_SERVICE;
import static com.openclassrooms.realestatemanager.utils.MyApp.getContext;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateViewHolder> {

    private List<RealEstateWithPhotos> mRealEstateList;
    public final String TAG = "DEBUG";

    public RealEstateAdapter(List<RealEstateWithPhotos> mRealEstateList) {
        this.mRealEstateList = mRealEstateList;
    }

    @Override
    public RealEstateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.real_estate_cardview, parent, false);

        return new RealEstateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RealEstateViewHolder holder, int position) {
        holder.updateRealEstateCardView(mRealEstateList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mRealEstateList.size();
    }

    public RealEstateWithPhotos getRealEstate(int position){
        return this.mRealEstateList.get(position);
    }

    public void updateData(List<RealEstateWithPhotos> realEstateList){
        this.mRealEstateList.clear();
        this.mRealEstateList.addAll(realEstateList);

        this.notifyDataSetChanged();
    }
}
