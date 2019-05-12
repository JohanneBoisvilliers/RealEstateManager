package com.openclassrooms.realestatemanager.realEstateDetails;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class photoViewpagerFragment extends Fragment {


    @BindView(R.id.framelayout_test_viewpager) FrameLayout mFrameLayout;
    @BindView(R.id.photo_container) ImageView mPhotoContainer;

    private static final String KEY_POSITION="position";
    private static final String KEY_COLOR="color";

    public photoViewpagerFragment() {
        // Required empty public constructor
    }

    // 2 - Method that will create a new instance of PageFragment, and add data to its bundle.
    public static photoViewpagerFragment newInstance(int position, int color) {

        // 2.1 Create new fragment
        photoViewpagerFragment frag = new photoViewpagerFragment();

        // 2.2 Create bundle and add it some data
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_COLOR, color);
        frag.setArguments(args);

        return(frag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 3 - Get layout of PageFragment
        View result = inflater.inflate(R.layout.fragment_photo_viewpager, container, false);

        ButterKnife.bind(this,result);

        // 5 - Get data from Bundle (created in method newInstance)
        int position = getArguments().getInt(KEY_POSITION, -1);
        int color = getArguments().getInt(KEY_COLOR, -1);

        // 6 - Update widgets with it
        mPhotoContainer.setBackgroundColor(color);

        return result;
    }

}
