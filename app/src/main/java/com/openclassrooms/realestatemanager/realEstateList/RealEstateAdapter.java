package com.openclassrooms.realestatemanager.realEstateList;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injections;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.RealEstateWithPhotos;
import com.openclassrooms.realestatemanager.utils.MyApp;

import java.util.List;

public class RealEstateAdapter extends RecyclerView.Adapter<RealEstateViewHolder> {

    private List<RealEstateWithPhotos> mRealEstateList;
    public static final int DARK_COLOR =
            MyApp.getContext().getResources().getColor(R.color.colorPrimaryDark);
    public static final int LIGHT_COLOR =
            MyApp.getContext().getResources().getColor(R.color.primaryTextColor);
    private RealEstateViewModel mRealEstateViewModel;
    private Boolean isOnePaneLayout;
    public final String TAG = "DEBUG";
    private int mSelectedItem;
    private Context mContext;

    public RealEstateAdapter(List<RealEstateWithPhotos> mRealEstateList, Boolean oneOrTwoPanes,
                             Context context) {
        this.mRealEstateList = mRealEstateList;
        this.mSelectedItem = 0;
        this.isOnePaneLayout = oneOrTwoPanes;
        this.mContext = context;
        this.configureViewModel();
    }

    @Override
    public RealEstateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.real_estate_cardview, parent, false);

        return new RealEstateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RealEstateViewHolder holder, int position) {
        holder.updateRealEstateCardView(mRealEstateList.get(position));
        if (mRealEstateViewModel.selectedItemPos.get() != null) {
            mSelectedItem = mRealEstateViewModel.selectedItemPos.get();
        }
        if (!isOnePaneLayout) {
            if (mSelectedItem == position) {
                this.setColorOfSelectedItem(holder, DARK_COLOR, LIGHT_COLOR);
            } else {
                this.setColorOfSelectedItem(holder, LIGHT_COLOR, DARK_COLOR);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.mRealEstateList.size();
    }

    public RealEstateWithPhotos getRealEstate(int position){
        return this.mRealEstateList.get(position);
    }

    //notify recyclerview when real estates are fetch
    public void updateData(List<RealEstateWithPhotos> realEstateList){
        this.mRealEstateList.clear();
        this.mRealEstateList.addAll(realEstateList);

        this.notifyDataSetChanged();
    }

    //configure viewmodel for requests
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injections.provideViewModelFactory(mContext);
        this.mRealEstateViewModel =
                ViewModelProviders.of((FragmentActivity) mContext, mViewModelFactory).get(RealEstateViewModel.class);
    }

    // ------------------------------------ UI ------------------------------------

    //keep in memory the position of the selected item
    public void selectedListItem(int pos) {
        int previousItem = mSelectedItem;
        mSelectedItem = pos;

        notifyItemChanged(previousItem);
        notifyItemChanged(pos);
    }

    //highlight the selected element in two panes layouts
    private void setColorOfSelectedItem(RealEstateViewHolder holder, int backgroundColor,
                                        int colorAccent) {
        holder.mTransparentBackground.setBackgroundColor(backgroundColor);
        holder.mRealEstateType.setTextColor(colorAccent);
        holder.mLineSeparator.setBackgroundColor(colorAccent);
        holder.mRealEstatePrice.setTextColor(colorAccent);
    }
}
