package com.openclassrooms.realestatemanager.realEstateList;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.viewModels.SharedViewModel;
import com.openclassrooms.realestatemanager.controllers.RealEstateDetailsActivity;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateListFragment extends Fragment {

    @BindView(R.id.real_estate_recycler_view) RecyclerView mRealEstateRecyclerView;

    private RealEstateAdapter mRealEstateAdapter;
    private List<RealEstate> mRealEstateList = new ArrayList<>();
    private List<String> mRealEstatePhoto = new ArrayList<>();
    private RealEstateViewModel mRealEstateViewModel;
    private SharedViewModel mSharedViewModel;

    public RealEstateListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_real_estate_list, container, false);
        ButterKnife.bind(this,view);

        this.configureRecyclerView();
        this.configureViewModel();
        Log.d("DEBUG", "onCreateView: "+mRealEstateList.size());
        this.getRealEstates();

        return view;
    }


    private void configureRecyclerView(){
        //Create adapter passing the list of restaurant
        this.mRealEstateAdapter = new RealEstateAdapter(this.mRealEstateList);
        //Attach the adapter to the recyclerview to populate items
        this.mRealEstateRecyclerView.setAdapter(this.mRealEstateAdapter);
        //Set layout manager to position the items
        this.mRealEstateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        ItemClickSupport.addTo(mRealEstateRecyclerView, R.layout.fragment_real_estate_list)
                .setOnItemClickListener((recyclerView1, position, v) -> {
                            this.mSharedViewModel.select(this.mRealEstateAdapter.getRealEstate(position));
                            View view = getActivity().findViewById(R.id.container_real_estate_detail);
                            if(view == null){//one pane layout
                                Intent intent = new Intent(getContext(), RealEstateDetailsActivity.class);
                                intent.putExtra("realEstate",this.mRealEstateAdapter.getRealEstate(position));
                                startActivity(intent);
                            }
                });
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RealEstateViewModel.class);
    }

    private void getRealEstates(){
        this.mRealEstateViewModel.getRealEstate().observe(this, this::updateItemsList);
    }

    private void getRealEstatePhotos(RealEstate realEstate){
        this.mRealEstateViewModel.getRealEstatePhotos(realEstate.getId()).observe(this,this::updatePhoto);
    }

    private void updateItemsList(List<RealEstate> realEstateList){
        for (RealEstate realEstate:realEstateList) {
            this.getRealEstatePhotos(realEstate);
            realEstate.getPhotoList().addAll(mRealEstatePhoto);
        }
        this.mRealEstateAdapter.updateData(realEstateList);
    }
    private void updatePhoto(List<String> realEstateList){
        this.mRealEstatePhoto.clear();
        this.mRealEstatePhoto.addAll(realEstateList);
        Log.d("DEBUG", "updatePhoto: "+mRealEstatePhoto.size());
    }
}
