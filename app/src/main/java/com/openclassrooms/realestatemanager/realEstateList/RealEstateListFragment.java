package com.openclassrooms.realestatemanager.realEstateList;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.realEstateDetails.RealEstateDetailsFragment;
import com.openclassrooms.realestatemanager.viewModels.SharedViewModel;
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

        this.getRealEstates();

        return view;
    }

    private void configureRecyclerView(){
        //Create adapter passing the list of restaurant
        this.mRealEstateAdapter = new RealEstateAdapter(this.mRealEstateList,this.mRealEstatePhoto);
        //Attach the adapter to the recyclerview to populate items
        this.mRealEstateRecyclerView.setAdapter(this.mRealEstateAdapter);
        //Set layout manager to position the items
        this.mRealEstateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //shared viewmodel for two panes layout
        mSharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        ItemClickSupport.addTo(mRealEstateRecyclerView, R.layout.fragment_real_estate_list)
                .setOnItemClickListener((recyclerView1, position, v) -> {
                    this.mSharedViewModel.select(this.mRealEstateAdapter.getRealEstate(position));
                    View view = getActivity().findViewById(R.id.container_real_estate_detail);
                    if(view == null){//one pane layout
                        this.swapFragment();
                    }
                });
    }
    //configure viewmodel for requests
    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RealEstateViewModel.class);
    }
    //request all real estates from database and put an observer to update list if there is any change
    private void getRealEstates(){
        this.mRealEstateViewModel.getRealEstate().observe(this, this::updateItemsList);
    }
    //request photo for a specific real estate and put an observer to update photos
    private void getRealEstatesPhotos(RealEstate realEstate){
        this.mRealEstateViewModel.getRealEstatePhotos(realEstate.getId()).observe(this, this::updateRealEstatePhotos);
    }
    //get photos for all real estates et notify adapter for new real estate list
    private void updateItemsList(List<RealEstate> realEstateList){
        for (RealEstate realEstate:realEstateList) {
            this.getRealEstatesPhotos(realEstate);
        }
        this.mRealEstateAdapter.updateData(realEstateList);
    }
    //notify adapter for the new list of photos
    private void updateRealEstatePhotos(List<String> realEstatePhotos){
        mRealEstateAdapter.updatePhotos(realEstatePhotos);
    }
    //if one pane layout, change list fragment for a detail fragment
    private void swapFragment(){
        RealEstateDetailsFragment realEstateDetailsFragment = new RealEstateDetailsFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_real_estate_recycler_view, realEstateDetailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
