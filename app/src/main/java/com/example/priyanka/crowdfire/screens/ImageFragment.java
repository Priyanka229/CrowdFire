package com.example.priyanka.crowdfire.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.priyanka.crowdfire.R;

public class ImageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadImage();
    }

    private void loadImage() {
        if (getArguments() != null && getView() != null) {
            String encodedImageString = getArguments().getString("encodedImageString");
            if (!TextUtils.isEmpty(encodedImageString)) {
                ImageView imageView = getView().findViewById(R.id.image_iv);

                Glide.with(this)
                        .load(encodedImageString)
                        .into(imageView);
            }
        }
    }
}
