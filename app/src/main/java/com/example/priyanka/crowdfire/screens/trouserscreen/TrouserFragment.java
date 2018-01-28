package com.example.priyanka.crowdfire.screens.trouserscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.priyanka.crowdfire.R;
import com.example.priyanka.crowdfire.db.CrowdFireDBHelper;
import com.example.priyanka.crowdfire.screens.MasterFragment;

public class TrouserFragment extends MasterFragment {
    private TrouserFragmentCallbacks mTrouserFragmentCallbacks;

    public interface TrouserFragmentCallbacks {
        void onTrouserSelected(String key);
    }

    public void setTrouserFragmentCallbacks(TrouserFragmentCallbacks trouserFragmentCallbacks) {
        this.mTrouserFragmentCallbacks = trouserFragmentCallbacks;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImagesEncodedList = CrowdFireDBHelper.getInstance(getActivity()).getTrouserImagePathListFromDB();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trouser, container, false);
    }

    @Override
    public void onPageSelected(int position) {
        if (mTrouserFragmentCallbacks != null && position < mImagesEncodedList.size()) {
            mTrouserFragmentCallbacks.onTrouserSelected(mImagesEncodedList.get(position));

            if (getArguments() != null) {
                getArguments().putInt("trouser_section_pos", position);
            }
        }
    }

    @Override
    protected void storeImageIntoDB(String picturePath) {
        CrowdFireDBHelper.getInstance(getActivity()).insertTrouserImagePathIntoDB(picturePath);
    }

    public void setCurrentPosition(int position) {
        super.setCurrentPosition(position);
    }

    protected int getDefaultSelectedPosition() {
        int returnValue = 0;
        if (getArguments() != null) {
            returnValue = getArguments().getInt("trouser_section_pos", 0);
        }
        return returnValue;
    }
}
