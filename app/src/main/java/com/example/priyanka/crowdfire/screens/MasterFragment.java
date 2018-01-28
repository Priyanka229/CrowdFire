package com.example.priyanka.crowdfire.screens;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.priyanka.crowdfire.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public abstract class MasterFragment extends Fragment implements DialogInterface.OnClickListener, View.OnClickListener, ViewPager.OnPageChangeListener {
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private static final int CAMERA_AND_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 2;

    private File mImageStorageDirectory;
    private AlertDialog.Builder mImageAddBuilder;

    private static final int PICK_IMAGE_SINGLE_FROM_GALLERY = 1;
    private static final int PICK_IMAGE_SINGLE_FROM_CAMERA = 2;
    protected List<String> mImagesEncodedList;

    private ViewPager mViewPager;
    private ImagePagerAdapter mShirtPagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mImagesEncodedList = new ArrayList<>();

        mImageAddBuilder = new AlertDialog.Builder(getActivity());
        mImageAddBuilder.setTitle("Select Picture");
        CharSequence[] items = {"Select from camera", "Select from gallery"};
        mImageAddBuilder.setItems(items, this);

        mImageStorageDirectory = new File(Environment
                .getExternalStorageDirectory()
                + File.separator + "crowdFileImages" + File.separator);
        mImageStorageDirectory.mkdirs();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.shirt_view_pager);
        mShirtPagerAdapter = new ImagePagerAdapter(getChildFragmentManager());
        mShirtPagerAdapter.setEncodedStringList(mImagesEncodedList);
        mViewPager.setAdapter(mShirtPagerAdapter);

        mViewPager.addOnPageChangeListener(this);

        int position = getDefaultSelectedPosition();
        setCurrentPosition(position);

        ImageView addIv = view.findViewById(R.id.add_iv);
        addIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        handleAddImageClick();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                actionOnCameraClick();
                break;

            case 1:
                actionOnGalleryClick();
                break;
        }

        dialog.dismiss();
    }

    protected void handleAddImageClick() {
        if (mImageAddBuilder != null) {
            mImageAddBuilder.show();
        }
    }

    private void actionOnGalleryClick() {
        if (isStorageReadPermissionGranted()) {
            openGalleryForSingleImageSelection();
        } else {
            askForStoragePermission();
        }
    }

    private void actionOnCameraClick() {
        if (isCameraPermissionGranted() && isStorageReadPermissionGranted()) {
            openCameraForSingleImageSelection();
        } else {
            askForStorageAndCameraPermission();
        }
    }

    private void openGalleryForSingleImageSelection() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_SINGLE_FROM_GALLERY);
    }

    private void openCameraForSingleImageSelection() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mImageStorageDirectory, System.currentTimeMillis() + ".jpg")));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageStorageDirectory + "/" + System.currentTimeMillis() + ".jpg");

        startActivityForResult(cameraIntent, PICK_IMAGE_SINGLE_FROM_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_SINGLE_FROM_GALLERY && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);

                // add to ui list
                mImagesEncodedList.add(picturePath);

                // added into db
                storeImageIntoDB(picturePath);

                // notify
                imageListUpdateNotify();

                cursor.close();
            } else if (requestCode == PICK_IMAGE_SINGLE_FROM_CAMERA && resultCode == RESULT_OK && data != null) {
                String picturePath = getRealPathFromURI(getImageUri(getActivity(), (Bitmap) data.getExtras().get("data")));

                // add to ui list
                mImagesEncodedList.add(picturePath);

                // added into db
                storeImageIntoDB(picturePath);

                // notify
                imageListUpdateNotify();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void imageListUpdateNotify() {
        if (mShirtPagerAdapter != null) {
            mShirtPagerAdapter.notifyDataSetChanged();
        }

        setCurrentPosition(mImagesEncodedList.size() - 1);
    }

    public void setCurrentPosition(int position) {
        if (mViewPager != null && mViewPager.getAdapter() != null
                && mViewPager.getAdapter().getCount() > position) {
            if (mViewPager.getCurrentItem() == position) {
                mViewPager.setCurrentItem(position, true);
                onPageSelected(position);
            } else {
                mViewPager.setCurrentItem(position, true);
            }
        }
    }

    protected abstract void storeImageIntoDB(String picturePath);

    public List<String> imageList() {
        return mImagesEncodedList;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    protected int getDefaultSelectedPosition() {
        return 0;
    }


    private boolean isStorageReadPermissionGranted() {
        boolean returnValue;
        if (Build.VERSION.SDK_INT >= 23 && getActivity() != null) {
            returnValue = getActivity().checkSelfPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        else {
            returnValue = true;
        }

        return returnValue;
    }

    private boolean isCameraPermissionGranted() {
        boolean returnValue;
        if (Build.VERSION.SDK_INT >= 23 && getActivity() != null) {
            returnValue = getActivity().checkSelfPermission(
                    Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        else {
            returnValue = true;
        }

        return returnValue;
    }

    private void askForStorageAndCameraPermission() {
        requestPermissions(new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_AND_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE);
    }

    private void askForStoragePermission() {
        requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_AND_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                if (isCameraPermissionGranted() && isStorageReadPermissionGranted()) {
                    openCameraForSingleImageSelection();
                } else {
                    Toast.makeText(getActivity(), "Camera and external Storage permission allows us to do load images. Please allow these permission in App Settings.", Toast.LENGTH_LONG).show();
                }
                break;

            case WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (isStorageReadPermissionGranted()) {
                    openGalleryForSingleImageSelection();
                } else {
                    Toast.makeText(getActivity(), "External Storage permission allows us to do load images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
