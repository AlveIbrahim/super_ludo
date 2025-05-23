package com.example.super_ludo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Utility class for handling application preferences.
 */
public class PreferenceUtils {

    private static final String KEY_FIRST_RUN = "first_run";
    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    private static final String KEY_ANIMATION_SPEED = "animation_speed";

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Check if this is the first run of the application.
     */
    public static boolean isFirstRun(Context context) {
        SharedPreferences prefs = getPreferences(context);
        boolean firstRun = prefs.getBoolean(KEY_FIRST_RUN, true);

        if (firstRun) {
            // Set first run to false
            prefs.edit().putBoolean(KEY_FIRST_RUN, false).apply();
        }

        return firstRun;
    }

    /**
     * Check if sound is enabled.
     */
    public static boolean isSoundEnabled(Context context) {
        return getPreferences(context).getBoolean(KEY_SOUND_ENABLED, true);
    }

    /**
     * Set sound enabled state.
     */
    public static void setSoundEnabled(Context context, boolean enabled) {
        getPreferences(context).edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply();
    }

    /**
     * Check if vibration is enabled.
     */
    public static boolean isVibrationEnabled(Context context) {
        return getPreferences(context).getBoolean(KEY_VIBRATION_ENABLED, true);
    }

    /**
     * Set vibration enabled state.
     */
    public static void setVibrationEnabled(Context context, boolean enabled) {
        getPreferences(context).edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply();
    }

    /**
     * Get the animation speed (percentage).
     */
    public static int getAnimationSpeed(Context context) {
        return getPreferences(context).getInt(KEY_ANIMATION_SPEED, 100);
    }

    /**
     * Set the animation speed.
     */
    public static void setAnimationSpeed(Context context, int speed) {
        getPreferences(context).edit().putInt(KEY_ANIMATION_SPEED, speed).apply();
    }
}
