package com.example.priyanka.crowdfire.crowdfirehelper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class CrowdFireUtils {
    public static boolean isStoragePermissionGranted(Context context) {
        boolean returnValue = false;

        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                returnValue = true;
            }
        }
        else {
            returnValue = true;
        }

        return returnValue;
    }
}
