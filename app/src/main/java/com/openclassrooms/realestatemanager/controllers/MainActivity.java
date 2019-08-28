package com.openclassrooms.realestatemanager.controllers;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.login.LoginActivity;
import com.openclassrooms.realestatemanager.login.UserViewModel;
import com.openclassrooms.realestatemanager.models.User;
import com.openclassrooms.realestatemanager.realEstateDetails.RealEstateDetailsFragment;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateListFragment;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;
import com.openclassrooms.realestatemanager.utils.MyApp;
import com.openclassrooms.realestatemanager.utils.SingletonSession;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.views.HeaderViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.activity_main_toolbar) Toolbar mToolbar;
    @BindView(R.id.activity_main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.activity_main_nav_view) NavigationView mNavigationView;

    private RealEstateListFragment mRealEstateListFragment;
    private RealEstateDetailsFragment mRealEstateDetailsFragment;
    private static final int WRITE_PERMISSION = 0x01;
    public static final String TAG = "DEBUG";
    private static final int PERMISSION_ALL = 0x02;
    private UserViewModel mUserViewModel;
    private RealEstateViewModel mRealEstateViewModel;
    private View mNavHeader;
    private HeaderViewHolder mHeaderViewHolder;
    private Bundle mBundle;

    // -------------------------------- LIFE CYCLE --------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Stetho.initializeWithDefaults(this);

        this.configureViewModel();
        this.configureToolbar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.configureNavHeader();
        this.getCurrentUser(getUserId());
        this.requestWritePermission();
        this.setFragments(savedInstanceState);
    }
    @Override
    public void onBackPressed() {
        //  Handle back click to close menu
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        super.onBackPressed();
        MyApp.isInit = false;
    }

    // ------------------------------------ DATA ------------------------------------

    //configure viewmodel for requests
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(this);
        this.mUserViewModel = ViewModelProviders.of(this, mViewModelFactory).get(UserViewModel.class);
        this.mRealEstateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RealEstateViewModel.class);
    }
    //get user id from intent sent by sign in activity or register activity
    private Long getUserId() {
        if (checkLastUser() != 0) {
            return checkLastUser();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            return 0L;
        }
    }
    //get user logged to set infos in different place of application
    private void getCurrentUser(Long userId) {
        this.mUserViewModel.getUser(userId).observe(this, this::updateUser);
    }

    //show good fragment depending who call them
    private void setFragments(Bundle bundle) {
        String whoCallActivity = getIntent().getStringExtra("ComeFrom");
        if (whoCallActivity != null && whoCallActivity.equals("Notification")) {
            this.configureRealEstateDetailsFragment(R.id.container_real_estate_recycler_view);
        } else {
            if (bundle == null) {
                this.configureRealEstateListFragment();
            }
            this.configureRealEstateDetailsFragment(R.id.container_real_estate_detail);
        }
    }

    private Long checkLastUser() {
        Long Userid;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Userid = prefs.getLong("userId", 0);
        return Userid;
    }

    public void clearUserConnected() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putLong("userId", 0).commit();
    }

    // ---------------------------------- LISTENERS ----------------------------------

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.activity_main_drawer_all_estate:
                break;
            case R.id.activity_main_drawer_around:
                break;
            case R.id.activity_main_drawer_settings:
                this.startPreferencesActivity();
                break;
            case R.id.activity_main_drawer_disconnect:
                this.clearUserConnected();
                this.startLoginActivity();
                break;
            default:
                break;
        }

        this.mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.primaryTextColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle actions on menu items
        switch (item.getItemId()) {
            case R.id.menu_activity_main_add_realestate:
                this.startAddRealEstateActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // ----------------------------------- UTILS -----------------------------------

    private void startAddRealEstateActivity(){
        startActivity(new Intent(this, AddARealEstateActivity.class));
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void startPreferencesActivity() {
        startActivity(new Intent(this,
                PreferencesActivity.class));
    }

    // ------------------------------------ UI ------------------------------------

    private void configureToolbar() {
        // Sets the Toolbar
        setSupportActionBar(mToolbar);
    }
    //Configure Drawer Layout
    private void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.secondaryTextColor));
        toggle.syncState();
    }
    //load image into header with glide
    private void configureNavHeader() {
        mNavHeader = mNavigationView.getHeaderView(0);
        mHeaderViewHolder = new HeaderViewHolder(mNavHeader);
        Utils.configureImageHeader(this, mHeaderViewHolder.getBackgroundHeader());
        Utils.configureUserPhoto(null, getApplicationContext(), mHeaderViewHolder.getUserPhoto());
    }
    private void configureNavigationView(){
        mNavigationView.setNavigationItemSelectedListener(this);
    }
    //configure recyclerview fragment for one pane layout
    private void configureRealEstateListFragment(){
        //  Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mRealEstateListFragment = (RealEstateListFragment) getSupportFragmentManager().findFragmentById(R.id.container_real_estate_recycler_view);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (mRealEstateListFragment == null) {
            //  Create new main fragment
            mRealEstateListFragment = new RealEstateListFragment();
            //  Add it to FrameLayout container
            ft.add(R.id.container_real_estate_recycler_view, mRealEstateListFragment)
                    .commit();
        }
    }
    //configure the detail fragment for two panes layout
    private void configureRealEstateDetailsFragment(int id) {
        // Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        mRealEstateDetailsFragment =
                (RealEstateDetailsFragment) getSupportFragmentManager().findFragmentById(id);

        if (mRealEstateDetailsFragment == null && findViewById(id) != null) {
            mRealEstateDetailsFragment = new RealEstateDetailsFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(id, mRealEstateDetailsFragment)
                    .commit();
        }
    }
    //observe user logged in to configure navigation header
    private void updateUser(User user) {
        mHeaderViewHolder.getUserNameTxt().setText(user.getUsername());
        mHeaderViewHolder.getUserEmailTxt().setText(user.getEmail());
        Utils.configureUserPhoto(user.getPhotoUrl(), getApplicationContext(), mHeaderViewHolder.getUserPhoto());
        SingletonSession.Instance().setUser(user);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == WRITE_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                configureRealEstateListFragment();
            } else {
                requestWritePermission();
            }
        }
    }

    private void requestWritePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA
            };

            if (!this.hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            }
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
