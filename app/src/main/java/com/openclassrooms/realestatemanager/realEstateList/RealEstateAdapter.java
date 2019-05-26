package com.openclassrooms.realestatemanager.realEstateList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstate;

import java.util.ArrayList;
import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateViewHolder> {

    private List<RealEstate> mRealEstateList = new ArrayList<>();

    public RealEstateAdapter(List<RealEstate> mRealEstateList) {
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

    public RealEstate getRealEstate(int position){
        return this.mRealEstateList.get(position);
    }

    public void updateData(List<RealEstate> realEstateList){
        this.mRealEstateList = realEstateList;
        this.notifyDataSetChanged();
    }
}
