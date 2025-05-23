package com.example.super_ludo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling runtime permissions.
 */
public class PermissionUtils {

    public static final int REQUEST_CODE_PERMISSIONS = 100;

    /**
     * Check if a permission is granted.
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Check if all permissions are granted.
     */
    public static boolean areAllPermissionsGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (!isPermissionGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the list of permissions that need to be requested.
     */
    public static String[] getPermissionsToRequest(Context context, String[] permissions) {
        List<String> permissionsToRequest = new ArrayList<>();

        for (String permission : permissions) {
            if (!isPermissionGranted(context, permission)) {
                permissionsToRequest.add(permission);
            }
        }

        return permissionsToRequest.toArray(new String[0]);
    }

    /**
     * Request permissions.
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * Check if permission rationale should be shown.
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}
