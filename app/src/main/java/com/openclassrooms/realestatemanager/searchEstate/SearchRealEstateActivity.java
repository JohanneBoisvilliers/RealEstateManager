package com.openclassrooms.realestatemanager.searchEstate;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchRealEstateActivity extends AppCompatActivity {

    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;

    private SearchSettingsFragment mSearchSettingsFragment;
    private RealEstateViewModel mRealEstateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_real_estate);
        ButterKnife.bind(this);
        this.configureViewModel();
        this.configureToolbar();
        this.configureSearchSettingsFragment();
    }

    // ----------------------------------- UTILS -----------------------------------
    private void configureToolbar() {
        // Sets the Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void configureSearchSettingsFragment() {
        mSearchSettingsFragment =
                (SearchSettingsFragment) getSupportFragmentManager().findFragmentById(R.id.search_fragment_container);

        if (mSearchSettingsFragment == null && findViewById(R.id.search_fragment_container) != null) {
            mSearchSettingsFragment = new SearchSettingsFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.search_fragment_container, mSearchSettingsFragment)
                    .commit();
        }
    }

    // ------------------------------------ DATA ------------------------------------

    //configure viewmodel for requests
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(this);
        this.mRealEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RealEstateViewModel.class);
    }

}
