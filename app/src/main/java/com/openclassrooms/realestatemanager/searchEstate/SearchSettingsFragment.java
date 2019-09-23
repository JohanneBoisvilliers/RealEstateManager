package com.openclassrooms.realestatemanager.searchEstate;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchSettingsBinding;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchSettingsFragment extends Fragment {

    @BindView(R.id.search_type_spinner)
    Spinner mSpinner;

    private String mRealEstateType;
    private RealEstateViewModel mRealEstateViewModel;

    public SearchSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSearchSettingsBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_search_settings, container, false);
        View view = binding.getRoot();
        ButterKnife.bind(this, view);

        this.configureViewModel();
        binding.setViewmodel(mRealEstateViewModel);
        this.configureSpinner();
        this.spinnerListener();

        return view;
    }

    // ------------------------------------ UI ------------------------------------

    private void configureSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.type_list, R.layout.custom_item_spinner_search);
        mSpinner.setAdapter(adapter);
    }

    // ---------------------------------- LISTENERS ----------------------------------

    private void spinnerListener() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onSpinnerItemChanged(parent.getItemAtPosition(position).toString(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // ------------------------------------ DATA ------------------------------------

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(getContext());
        this.mRealEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RealEstateViewModel.class);
    }

    // ----------------------------------- UTILS -----------------------------------

    //on spinner change, set the type of real estate with "mSpinnerValue" and add data in viewmodel to keep spinner position when user rotate the screen
    private void onSpinnerItemChanged(String itemValue, int itemPosition) {
        mRealEstateType = itemValue;
        mRealEstateViewModel.spinnerPos.set(itemPosition);
    }
}
