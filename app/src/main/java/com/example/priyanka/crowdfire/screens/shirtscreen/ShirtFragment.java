package com.example.priyanka.crowdfire.screens.shirtscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.priyanka.crowdfire.R;
import com.example.priyanka.crowdfire.db.CrowdFireDBHelper;
import com.example.priyanka.crowdfire.screens.MasterFragment;

public class ShirtFragment extends MasterFragment {
    private ShirtFragmentCallbacks mShirtFragmentCallbacks;

    public interface ShirtFragmentCallbacks {
        void onShirtSelected(String key);
    }

    public void setShirtFragmentCallbacks(ShirtFragmentCallbacks shirtFragmentCallbacks) {
        this.mShirtFragmentCallbacks = shirtFragmentCallbacks;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImagesEncodedList = CrowdFireDBHelper.getInstance(getActivity()).getShirtImagePathListFromDB();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shirt, container, false);
    }

    @Override
    public void onPageSelected(int position) {
        if (mShirtFragmentCallbacks != null && position < mImagesEncodedList.size()) {
            mShirtFragmentCallbacks.onShirtSelected(mImagesEncodedList.get(position));

            if (getArguments() != null) {
                getArguments().putInt("shirt_section_pos", position);
            }
        }
    }

    @Override
    protected void storeImageIntoDB(String picturePath) {
        CrowdFireDBHelper.getInstance(getActivity()).insertShirtImagePathIntoDB(picturePath);
    }

    public void setCurrentPosition(int position) {
        super.setCurrentPosition(position);
    }

    protected int getDefaultSelectedPosition() {
        int returnValue = 0;
        if (getArguments() != null) {
            returnValue = getArguments().getInt("shirt_section_pos", 0);
        }
        return returnValue;
    }
}
