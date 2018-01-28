package com.example.priyanka.crowdfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.example.priyanka.crowdfire.db.CrowdFireDBHelper;
import com.example.priyanka.crowdfire.screens.shirtscreen.ShirtFragment;
import com.example.priyanka.crowdfire.screens.trouserscreen.TrouserFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ShirtFragment.ShirtFragmentCallbacks, TrouserFragment.TrouserFragmentCallbacks {

    private String mShirtSelectedKey;
    private String mTrouserSelectedKey;

    private ShirtFragment mShirtFragment;
    private TrouserFragment mTrouserFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShirtFragment = new ShirtFragment();
        mShirtFragment.setShirtFragmentCallbacks(this);
        Bundle shirtBundle = new Bundle();
        if (getIntent() != null && getIntent().getExtras() != null
                && getIntent().getExtras().containsKey("shirt_section_pos")) {
            shirtBundle.putInt("shirt_section_pos",
                    getIntent().getExtras().getInt("shirt_section_pos"));
            getIntent().getExtras().remove("shirt_section_pos");
        }
        mShirtFragment.setArguments(shirtBundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.top_container, mShirtFragment)
                .commit();


        mTrouserFragment = new TrouserFragment();
        mTrouserFragment.setTrouserFragmentCallbacks(this);
        Bundle trouserBundle = new Bundle();
        if (getIntent() != null && getIntent().getExtras() != null
                && getIntent().getExtras().containsKey("trouser_section_pos")) {
            trouserBundle.putInt("trouser_section_pos",
                    getIntent().getExtras().getInt("trouser_section_pos"));
            getIntent().getExtras().remove("trouser_section_pos");
        }
        mTrouserFragment.setArguments(trouserBundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.bottom_container, mTrouserFragment)
                .commit();

        // set on favourite icon click listener
        findViewById(R.id.favourite_wrapper).setOnClickListener(this);

        // set on shuffle icon click
        findViewById(R.id.shuffle_iv).setOnClickListener(this);

    }

    @Override
    public void onShirtSelected(String key) {
        mShirtSelectedKey = key;

        handleActionOnDressCombination();
    }

    @Override
    public void onTrouserSelected(String key) {
        mTrouserSelectedKey = key;

        handleActionOnDressCombination();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.favourite_wrapper:
                handleActionOnFavouriteClick();
                break;

            case R.id.shuffle_iv:
                handleActionOnShuffleClick();
                break;
        }
    }

    private void handleActionOnDressCombination() {
        if (!TextUtils.isEmpty(mShirtSelectedKey) && !TextUtils.isEmpty(mTrouserSelectedKey)) {
            String key = mShirtSelectedKey + mTrouserSelectedKey;

            ImageView imageView = findViewById(R.id.fav_un_fav_iv);

            boolean isRecordExists = CrowdFireDBHelper.getInstance(this).isFavouriteRecordExists(key);
            if (isRecordExists) {
                boolean isFavourite = CrowdFireDBHelper.getInstance(this).isFavourite(key);
                if (isFavourite) {
                    imageView.setImageResource(R.drawable.favourite);
                } else {
                    imageView.setImageResource(R.drawable.unfavourite);
                }
            } else {
                imageView.setImageResource(R.drawable.unfavourite);
            }
        }
    }

    private void handleActionOnFavouriteClick() {
        if (!TextUtils.isEmpty(mShirtSelectedKey) && !TextUtils.isEmpty(mTrouserSelectedKey)) {
            String key = mShirtSelectedKey + mTrouserSelectedKey;

            ImageView imageView = findViewById(R.id.fav_un_fav_iv);

            boolean isRecordExists = CrowdFireDBHelper.getInstance(this).isFavouriteRecordExists(key);
            if (isRecordExists) {
                boolean isFavourite = CrowdFireDBHelper.getInstance(this).isFavourite(key);
                if (isFavourite) {
                    imageView.setImageResource(R.drawable.unfavourite);
                    CrowdFireDBHelper.getInstance(this).updateFavouriteCombination(key, false);
                } else {
                    imageView.setImageResource(R.drawable.favourite);
                    CrowdFireDBHelper.getInstance(this).updateFavouriteCombination(key, true);
                }
            } else {
                imageView.setImageResource(R.drawable.favourite);
                CrowdFireDBHelper.getInstance(this).insertFavouriteCombination(key, true);
            }
        }
    }

    private void handleActionOnShuffleClick() {
        if (mShirtFragment != null) {
            int randomPosition = (int) (Math.random() * mShirtFragment.imageList().size());
            mShirtFragment.setCurrentPosition(randomPosition);
        }

        if (mTrouserFragment != null) {
            int randomPosition = (int) (Math.random() * mTrouserFragment.imageList().size());
            mTrouserFragment.setCurrentPosition(randomPosition);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null && intent.getExtras() != null) {
            if (mShirtFragment != null) {
                mShirtFragment.setCurrentPosition(
                        intent.getExtras().getInt("shirt_section_pos"));
            }

            if (mTrouserFragment != null) {
                mTrouserFragment.setCurrentPosition(
                        intent.getExtras().getInt("trouser_section_pos"));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mShirtFragment != null && mShirtFragment.getArguments() != null) {
            outState.putInt("shirt_section_pos",
                    mShirtFragment.getArguments().getInt("shirt_section_pos"));
        }

        if (mTrouserFragment != null && mTrouserFragment.getArguments() != null) {
            outState.putInt("trouser_section_pos",
                    mTrouserFragment.getArguments().getInt("trouser_section_pos"));
        }

        outState.putString("mShirtSelectedKey", mShirtSelectedKey);
        outState.putString("mTrouserSelectedKey", mTrouserSelectedKey);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            if (mShirtFragment != null) {
                mShirtFragment.setCurrentPosition(
                        savedInstanceState.getInt("shirt_section_pos"));
            }

            if (mTrouserFragment != null) {
                mTrouserFragment.setCurrentPosition(
                        savedInstanceState.getInt("trouser_section_pos"));
            }

            mShirtSelectedKey = savedInstanceState.getString("mShirtSelectedKey");
            mTrouserSelectedKey = savedInstanceState.getString("mTrouserSelectedKey");
        }
    }
}

