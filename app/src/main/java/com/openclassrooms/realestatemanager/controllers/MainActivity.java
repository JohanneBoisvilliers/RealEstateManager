package com.openclassrooms.realestatemanager.controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.realEstateDetails.RealEstateDetailsFragment;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateListFragment;
import com.openclassrooms.realestatemanager.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.activity_main_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView mNavigationView;

    private RealEstateListFragment mRealEstateListFragment;
    private RealEstateDetailsFragment mRealEstateDetailsFragment;
    private static final int WRITE_PERMISSION = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.requestWritePermission();
        Stetho.initializeWithDefaults(this);
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureRealEstateListFragment();
        this.configureRealEstateDetailsFragment();
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
    //configure the detail fragment for two panes layout
    private void configureRealEstateDetailsFragment(){
        // Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mRealEstateDetailsFragment = (RealEstateDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.container_real_estate_detail);

        if (mRealEstateDetailsFragment == null && findViewById(R.id.container_real_estate_detail) != null) {
            mRealEstateDetailsFragment = new RealEstateDetailsFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_real_estate_detail, mRealEstateDetailsFragment)
                    .commit();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You must allow permission write external storage to your mobile device.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void requestWritePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
            }
        }
    }
}
