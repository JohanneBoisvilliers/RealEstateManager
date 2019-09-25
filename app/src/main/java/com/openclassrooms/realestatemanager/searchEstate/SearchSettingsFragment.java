package com.openclassrooms.realestatemanager.searchEstate;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchSettingsBinding;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateListFragment;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchSettingsFragment extends Fragment {

    @BindView(R.id.search_type_spinner)
    Spinner mSpinner;
    @BindView(R.id.search_btn)
    Button mSubmitButton;

    private String mRealEstateType = "Loft";
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
        this.btnSubmitListener();

        return view;
    }

    // ------------------------------------ UI ------------------------------------

    private void configureSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.type_list, R.layout.custom_item_spinner_search);
        mSpinner.setAdapter(adapter);
    }

    //replace search fragment with result fragment to show all real estate after filtering
    private void swapFragment() {
        RealEstateListFragment resultSearchFragment = new RealEstateListFragment();
        FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.search_fragment_container, resultSearchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    private void btnSubmitListener() {
        mSubmitButton.setOnClickListener(v -> {
            mRealEstateViewModel.category.set(mRealEstateType);
            swapFragment();
        });
    }

    // ------------------------------------ DATA ------------------------------------

    private void configureViewModel() {
        this.mRealEstateViewModel = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);
    }

    // ----------------------------------- UTILS -----------------------------------

    //on spinner change, set the type of real estate with "mSpinnerValue" and add data in viewmodel to keep spinner position when user rotate the screen
    private void onSpinnerItemChanged(String itemValue, int itemPosition) {
        mRealEstateType = itemValue;
        mRealEstateViewModel.spinnerPos.set(itemPosition);
    }
}
