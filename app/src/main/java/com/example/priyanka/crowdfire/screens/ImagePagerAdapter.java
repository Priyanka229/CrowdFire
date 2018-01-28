package com.example.priyanka.crowdfire.screens;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ImagePagerAdapter extends FragmentPagerAdapter {
    private List<String> mEncodedStringList;

    public ImagePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setEncodedStringList(List<String> mEncodedStringList) {
        this.mEncodedStringList = mEncodedStringList;
    }

    @Override
    public Fragment getItem(int position) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("encodedImageString", mEncodedStringList.get(position));
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public int getCount() {
        return mEncodedStringList.size();
    }
}
