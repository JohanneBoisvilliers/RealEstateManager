package com.openclassrooms.realestatemanager.controllers;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.realEstateDetails.RealEstateDetailsFragment;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateListFragment;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RealEstateListFragment.OnItemClickedListener{

    @BindView(R.id.activity_main_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView mNavigationView;

    private RealEstateListFragment mRealEstateListFragment;
    private RealEstateDetailsFragment mRealEstateDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Stetho.initializeWithDefaults(this);
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureRealEstateListFragment();
    }

    private void configureToolbar(){
        // Sets the Toolbar
        setSupportActionBar(mToolbar);
    }
    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_all_estate:
                break;
            case R.id.activity_main_drawer_around:
                break;
            case R.id.activity_main_drawer_settings:
                break;
            default:
                break;
        }

        this.mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    //Configure Drawer Layout
    private void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.secondaryTextColor));
        toggle.syncState();
    }
    private void configureNavigationView(){
        mNavigationView.setNavigationItemSelectedListener(this);
    }
    //configure recyclerview fragment for one pane layout
    private void configureRealEstateListFragment(){
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mRealEstateListFragment = (RealEstateListFragment) getSupportFragmentManager().findFragmentById(R.id.container_real_estate_recycler_view);

        if (mRealEstateListFragment == null) {
            // B - Create new main fragment
            mRealEstateListFragment = new RealEstateListFragment();
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_real_estate_recycler_view, mRealEstateListFragment)
                    .commit();
        }
    }
    //callback method to know what to do when user cliked on recyclerview
    @Override
    public void onRecyclerViewClicked(RealEstate realEstate) {
        Log.i("DEBUG_FRAG",String.valueOf(mRealEstateDetailsFragment == null));
        mRealEstateDetailsFragment = (RealEstateDetailsFragment)getSupportFragmentManager().findFragmentById(R.id.container_real_estate_detail);
        if (mRealEstateDetailsFragment != null){
            this.configureRealEstateDetailsFragment();
        }else{
            Intent intent = new Intent(this,RealEstateDetailsActivity.class);
            intent.putExtra("realEstate",realEstate);
            startActivity(intent);
        }
    }
    //configure the detail fragment for two panes layout
    private void configureRealEstateDetailsFragment(){
        // Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mRealEstateDetailsFragment = (RealEstateDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container_real_estate_detail);

        if (mRealEstateDetailsFragment == null && findViewById(R.id.container_real_estate_detail) != null) {
            mRealEstateDetailsFragment = new RealEstateDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("realEstate",this.getRealEstateFromIntent());
            mRealEstateDetailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_real_estate_detail, mRealEstateDetailsFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
    //extrude real estate from recyclerview click
    private RealEstate getRealEstateFromIntent(){
        Intent intent = getIntent();
        RealEstate realEstate = intent.getParcelableExtra("realEstate");
        return realEstate;
    }
}
