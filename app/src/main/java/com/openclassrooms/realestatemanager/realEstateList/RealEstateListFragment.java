package com.openclassrooms.realestatemanager.realEstateList;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.RealEstateDetailsActivity;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateListFragment extends Fragment {

    @BindView(R.id.real_estate_recycler_view) RecyclerView mRealEstateRecyclerView;

    private RealEstateAdapter mRealEstateAdapter;
    private List<RealEstate> mRealEstateList = new ArrayList<>();
    private ListItemViewModel mRealEstateViewModel;
    private OnItemClickedListener mCallback;

    public RealEstateListFragment() {}

    //interface that will be implemented by any container activity
    public interface OnItemClickedListener {
        public void onRecyclerViewClicked(RealEstate realEstate);
    }

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.createCallbackToParentActivity();
    }

    private void configureRecyclerView(){
        //Create adapter passing the list of restaurant
        this.mRealEstateAdapter = new RealEstateAdapter(this.mRealEstateList);
        //Attach the adapter to the recyclerview to populate items
        this.mRealEstateRecyclerView.setAdapter(this.mRealEstateAdapter);
        //Set layout manager to position the items
        this.mRealEstateRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        ItemClickSupport.addTo(mRealEstateRecyclerView, R.layout.fragment_real_estate_list)
                .setOnItemClickListener((recyclerView1, position, v) -> this.mCallback.onRecyclerViewClicked(this.mRealEstateAdapter.getRealEstate(position)));
    }

    private void configureViewModel(){
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ListItemViewModel.class);
    }

    private void getRealEstates(){
        this.mRealEstateViewModel.getRealEstate().observe(this, this::updateItemsList);
    }

    private void updateItemsList(List<RealEstate> realEstateList){
        this.mRealEstateAdapter.updateData(realEstateList);
    }
    //Create callback to parent activity
    private void createCallbackToParentActivity(){
        try {
            //Parent activity will automatically subscribe to callback
            mCallback = (OnItemClickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString()+ " must implement OnItemClickedListener");
        }
    }
}
