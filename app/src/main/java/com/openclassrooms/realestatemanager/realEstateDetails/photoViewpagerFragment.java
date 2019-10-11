package com.openclassrooms.realestatemanager.realEstateDetails;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.realestatemanager.login.RegisterActivity.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class photoViewpagerFragment extends Fragment {


    @BindView(R.id.framelayout_test_viewpager) FrameLayout mFrameLayout;
    @BindView(R.id.photo_container) ImageView mPhotoContainer;
    @BindView(R.id.viewpager_description) TextView mPhotoDescription;

    private static final String KEY_DESCRIPTION="description";
    private static final String KEY_URL="photoUrl";
    private static final String KEY_ID = "photoId";
    private long mPhotoId;
    private RealEstateViewModel mRealEstateViewModel;

    // Method that will create a new instance of PageFragment, and add data to its bundle.
    public static photoViewpagerFragment newInstance(String photoUrl, String photoDescription,
                                                     long photoId) {

        // Create new fragment
        photoViewpagerFragment frag = new photoViewpagerFragment();

        // Create bundle and add it some data
        Bundle args = new Bundle();
        args.putString(KEY_DESCRIPTION,photoDescription);
        args.putString(KEY_URL, photoUrl);
        args.putLong(KEY_ID, photoId);
        frag.setArguments(args);

        return(frag);
    }

    // -------------------------------- LIFE CYCLE --------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get layout of PageFragment
        View result = inflater.inflate(R.layout.fragment_photo_viewpager, container, false);

        ButterKnife.bind(this,result);

        String photoUrl = (String)getArguments().get(KEY_URL);
        String photoDescription = (String)getArguments().get(KEY_DESCRIPTION);
        mPhotoId = (long) getArguments().get(KEY_ID);
        this.configureViewModel();
        Glide.with(this)
                .load(photoUrl)
                .centerCrop()
                .into(mPhotoContainer);
        mPhotoDescription.setText(photoDescription);
        this.listenerOnPhotoDescription();

        return result;
    }

    // ------------------------------------ DATA ------------------------------------

    //configure viewmodel
    private void configureViewModel() {
        this.mRealEstateViewModel = ViewModelProviders.of(getActivity()).get(RealEstateViewModel.class);
    }

    // ---------------------------------- LISTENERS ----------------------------------

    private void listenerOnPhotoDescription() {
        mPhotoDescription.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                            actionId == EditorInfo.IME_ACTION_DONE ||
                            event != null &&
                                    event.getAction() == KeyEvent.ACTION_DOWN &&
                                    event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                        if (event == null || !event.isShiftPressed()) {
                            // the user is done typing.
                            Log.d(TAG, "onEditorAction: ");
                            mRealEstateViewModel.updateSpecificPhoto(mPhotoId, mPhotoDescription.getText().toString());
                            InputMethodManager imm =
                                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            return true; // consume.
                        }
                    }
                    return false; // pass on to other listeners.
                }
        );
    }

}
