package com.openclassrooms.realestatemanager.realEstateList;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
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
    private List<RealEstateWithPhotos> mRealEstateList = new ArrayList<>();
    private RealEstateViewModel mRealEstateViewModel;
    private SharedViewModel mSharedViewModel;
    public static final String TAG = "DEBUG";

    public RealEstateListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_real_estate_list, container, false);
        ButterKnife.bind(this,view);

        this.configureRecyclerView();
        this.configureViewModel();

        this.getRealEstatesWithPhotos();

        return view;
    }

    private void configureRecyclerView(){
        //Create adapter passing the list of restaurant
        this.mRealEstateAdapter = new RealEstateAdapter(this.mRealEstateList);
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
    //get photos for all real estates et notify adapter for new real estate list
    private void updateItemsList(List<RealEstateWithPhotos> realEstateList){
        this.mRealEstateAdapter.updateData(realEstateList);
    }
    //request all real estates from database and put an observer to update list if there is any change
    private void getRealEstatesWithPhotos(){
        this.mRealEstateViewModel.getRealEstatewithPhotos().observe(this, this::updateItemsList);
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
