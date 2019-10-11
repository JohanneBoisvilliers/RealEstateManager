package com.openclassrooms.realestatemanager.realEstateList;


import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.db.SimpleSQLiteQuery;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.realEstateDetails.RealEstateDetailsFragment;
import com.openclassrooms.realestatemanager.utils.ItemClickSupport;
import com.openclassrooms.realestatemanager.utils.MyApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealEstateListFragment extends Fragment {

    @BindView(R.id.real_estate_recycler_view) RecyclerView mRealEstateRecyclerView;

    private RealEstateAdapter mRealEstateAdapter;
    private List<RealEstateWithPhotos> mRealEstateList = new ArrayList<>();
    private LinkedHashMap<Long, String> mAddressList = new LinkedHashMap<>();
    private RealEstateViewModel mRealEstateViewModel;
    private Boolean isTrue = false;
    public static final String TAG = "DEBUG";

    public RealEstateListFragment() {}

    // -------------------------------- LIFE CYCLE --------------------------------

    @Override
    public void onStart() {
        mRealEstateRecyclerView.getAdapter().notifyDataSetChanged();
        super.onStart();
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

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

    // ------------------------------------ UI ------------------------------------

    //if one pane layout, change list fragment for a detail fragment
    private void swapFragment() {
        RealEstateDetailsFragment realEstateDetailsFragment = new RealEstateDetailsFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_real_estate_recycler_view, realEstateDetailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    // ------------------------------------ DATA ------------------------------------

    //configure viewmodel for requests
    private void configureViewModel(){
        this.mRealEstateViewModel = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);
    }

    //fetch all addresses of real estate for markers on map
    private LinkedHashMap<Long, String> fetchRealEstateAddress(List<RealEstateWithPhotos> realEstateList) {
        LinkedHashMap<Long, String> tempList = new LinkedHashMap<>();
        for (int i = 0; i < realEstateList.size(); i++) {
            tempList.put(realEstateList.get(i).getRealEstate().getId(),
                    realEstateList.get(i).getRealEstate().getAddress());
        }
        return tempList;
    }

    private String fetchEndPriceForSearch() {
        if (mRealEstateViewModel.endPrice.get() == 0) {
            return "(SELECT MAX(price) FROM RealEstate)";
        } else {
            return String.valueOf(mRealEstateViewModel.endPrice.get());
        }
    }

    private String fetchEndAreaForSearch() {
        if (mRealEstateViewModel.endPrice.get() == 0) {
            return "(SELECT MAX(surface) FROM RealEstate)";
        } else {
            return String.valueOf(mRealEstateViewModel.surfaceEnd.get());
        }
    }

    //fetch data from view model to construct query to search estate
    private String[] fetchDatasForQuery() {
        return new String[]{mRealEstateViewModel.category.get(),
                String.valueOf(mRealEstateViewModel.startPrice.get()),
                fetchEndPriceForSearch(),
                String.valueOf(mRealEstateViewModel.rooms.get()),
                String.valueOf(mRealEstateViewModel.surfacestart.get()),
                fetchEndAreaForSearch()};
    }

    // ----------------------------------- UTILS -----------------------------------

    private void configureRecyclerView() {
        this.mRealEstateAdapter = new RealEstateAdapter(this.mRealEstateList, isOnePaneLayout(),
                getContext());
        this.mRealEstateRecyclerView.setAdapter(this.mRealEstateAdapter);
        this.mRealEstateRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemClickSupport.addTo(mRealEstateRecyclerView, R.layout.fragment_real_estate_list)
                .setOnItemClickListener((recyclerView1, position, v) -> {
                    this.mRealEstateViewModel.select(this.mRealEstateAdapter.getRealEstate(position));
                    this.mRealEstateViewModel.selectedItemPos.set(position);
                    if (isOnePaneLayout()) {//phone mode
                        this.swapFragment();
                    } else {//tablet mode
                        mRealEstateAdapter.selectedListItem(position);
                    }
                });

    }
    //get photos for all real estates et notify adapter for new real estate list
    private void updateItemsList(List<RealEstateWithPhotos> realEstateList){
        this.mRealEstateList.clear();
        this.mRealEstateList.addAll(realEstateList);
        this.mRealEstateAdapter.updateData(realEstateList);
        mAddressList.putAll(this.fetchRealEstateAddress(realEstateList));
        mRealEstateViewModel.selectAddressesList(mAddressList);
        if (!isOnePaneLayout() && !MyApp.isInit) {//tablet mode
            MyApp.isInit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRealEstateRecyclerView.findViewHolderForAdapterPosition(0).itemView.performClick();
                }
            }, 0);
        }
        if (getArguments() != null && getArguments().getLong(
                "realEstateIdToClick") != 0) {
            this.mRealEstateRecyclerView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mRealEstateRecyclerView.findViewHolderForAdapterPosition(getPositionOfRealEstate(getArguments().getLong("realEstateIdToClick")))
                                    .itemView.performClick();
                            getArguments().clear();
                            mRealEstateRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
        }
    }
    private int getPositionOfRealEstate(Long realEstateId) {
        int position = 0;
        for (int i = 0; i < mRealEstateList.size(); i++) {
            if (mRealEstateList.get(i).getRealEstate().getId() == realEstateId) {
                position = i;
            }
        }
        return position;
    }
    //check if we are in phone or tablet mode
    private Boolean isOnePaneLayout() {
        View view = getActivity().findViewById(R.id.container_real_estate_detail);
        return view == null;
    }

    // ----------------------------------- ASYNC -----------------------------------

    //request all real estates from database and put an observer to update list if there is any change
    private void getRealEstatesWithPhotos(){
        if (getActivity().getClass().getSimpleName().equals("MainActivity")) {
            this.mRealEstateViewModel.getRealEstatewithPhotos().observe(this, this::updateItemsList);
        } else {
            Log.d(TAG, "getRealEstatesWithPhotos: " + Arrays.toString(fetchDatasForQuery()));
            SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM RealEstate" +
                    " WHERE RealEstate.category = ?" +
                    " AND RealEstate.price BETWEEN ? AND ?" +
                    " AND RealEstate.nbreOfRoom >= ?" +
                    " AND RealEstate.surface BETWEEN ? AND ?",
                    fetchDatasForQuery());
            this.mRealEstateViewModel.getResultSearchRaw(query).observe(this, this::updateItemsList);
        }
    }
}
