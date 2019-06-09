package com.openclassrooms.realestatemanager.realEstateDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewDetailsPhotoAdapter  extends RecyclerView.Adapter<RecyclerViewDetailsPhotoViewHolder>{

    private List<Photo> mRealEstatePhotos = new ArrayList<>();

    public RecyclerViewDetailsPhotoAdapter(List<Photo> listOfPhotos) {
        this.mRealEstatePhotos = listOfPhotos;
    }

    @NonNull
    @Override
    public RecyclerViewDetailsPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_item_for_photo, parent, false);

        return new RecyclerViewDetailsPhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewDetailsPhotoViewHolder holder, int position) {
        holder.updatePhotoInRecyclerView(this.mRealEstatePhotos.get(position));
    }

    @Override
    public int getItemCount() {
        return mRealEstatePhotos.size();
    }

    public void updatePhotos(List<Photo> listOfPhotos){
        this.mRealEstatePhotos.clear();
        this.mRealEstatePhotos = listOfPhotos;
        this.notifyDataSetChanged();
    }
}
