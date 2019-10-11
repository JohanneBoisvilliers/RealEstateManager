package com.openclassrooms.realestatemanager.searchEstate;


import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchSettingsBinding;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateListFragment;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class SearchSettingsFragment extends Fragment {

    @BindView(R.id.search_type_spinner)
    Spinner mSpinner;
    @BindView(R.id.input_from)
    EditText mStartValue;
    @BindView(R.id.search_btn)
    Button mSubmitButton;
    @BindView(R.id.input_room_search)
    EditText mNumberOfRoomInput;
    @BindView(R.id.input_size_search_from)
    EditText mSizeFromInput;
    @BindView(R.id.input_size_search_to)
    EditText mSizeToInput;

    private String mRealEstateType = "Loft";
    private int mRealEstateStartPrice = 0;
    private int mRealEstateEndPrice = 0;
    private int mNumberOfRoom = 0;
    private int mSurfaceStart = 0;
    private int mSurfaceEnd = 0;
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
            mRealEstateViewModel.startPrice.set(mRealEstateStartPrice);
            mRealEstateViewModel.endPrice.set(mRealEstateEndPrice);
            mRealEstateViewModel.rooms.set(mNumberOfRoom);
            mRealEstateViewModel.surfacestart.set(mSurfaceStart);
            mRealEstateViewModel.surfaceEnd.set(mSurfaceEnd);
            swapFragment();
        });
    }

    @OnTextChanged(value = R.id.input_from, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void startValueChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mRealEstateStartPrice = 0;
        } else {
            mRealEstateStartPrice = Integer.parseInt(text.toString());
        }
    }

    @OnTextChanged(value = R.id.input_to, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void EndValueChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mRealEstateEndPrice = 0;
        } else {
            mRealEstateEndPrice = Integer.parseInt(text.toString());
        }
    }

    @OnTextChanged(value = R.id.input_room_search, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void roomValueChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mNumberOfRoom = 0;
        } else {
            mNumberOfRoom = Integer.parseInt(text.toString());
        }
    }

    @OnTextChanged(value = R.id.input_size_search_from, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void sizeFromValueChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mSurfaceStart = 0;
        } else {
            mSurfaceStart = Integer.parseInt(text.toString());
        }
    }

    @OnTextChanged(value = R.id.input_size_search_to, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void sizeToValueChanged(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            mSurfaceEnd = 0;
        } else {
            mSurfaceEnd = Integer.parseInt(text.toString());
        }
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
