package com.example.super_ludo.viewmodels;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * ViewModel for managing game settings.
 */
public class SettingsViewModel extends AndroidViewModel {

    private static final String KEY_SOUND_ENABLED = "sound_enabled";
    private static final String KEY_VIBRATION_ENABLED = "vibration_enabled";
    private static final String KEY_ANIMATION_SPEED = "animation_speed";

    private MutableLiveData<Boolean> soundEnabledLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> vibrationEnabledLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> animationSpeedLiveData = new MutableLiveData<>();

    private SharedPreferences preferences;

    public SettingsViewModel(@NonNull Application application) {
        super(application);

        // Get preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(application);

        // Load settings
        loadSettings();
    }

    /**
     * Load settings from SharedPreferences.
     */
    private void loadSettings() {
        boolean soundEnabled = preferences.getBoolean(KEY_SOUND_ENABLED, true);
        boolean vibrationEnabled = preferences.getBoolean(KEY_VIBRATION_ENABLED, true);
        int animationSpeed = preferences.getInt(KEY_ANIMATION_SPEED, 100); // 100% speed

        soundEnabledLiveData.setValue(soundEnabled);
        vibrationEnabledLiveData.setValue(vibrationEnabled);
        animationSpeedLiveData.setValue(animationSpeed);
    }

    /**
     * Save settings to SharedPreferences.
     */
    private void saveSettings() {
        SharedPreferences.Editor editor = preferences.edit();

        Boolean soundEnabled = soundEnabledLiveData.getValue();
        Boolean vibrationEnabled = vibrationEnabledLiveData.getValue();
        Integer animationSpeed = animationSpeedLiveData.getValue();

        if (soundEnabled != null) {
            editor.putBoolean(KEY_SOUND_ENABLED, soundEnabled);
        }

        if (vibrationEnabled != null) {
            editor.putBoolean(KEY_VIBRATION_ENABLED, vibrationEnabled);
        }

        if (animationSpeed != null) {
            editor.putInt(KEY_ANIMATION_SPEED, animationSpeed);
        }

        editor.apply();
    }

    /**
     * Set whether sound is enabled.
     */
    public void setSoundEnabled(boolean enabled) {
        soundEnabledLiveData.setValue(enabled);
        saveSettings();
    }

    /**
     * Set whether vibration is enabled.
     */
    public void setVibrationEnabled(boolean enabled) {
        vibrationEnabledLiveData.setValue(enabled);
        saveSettings();
    }

    /**
     * Set the animation speed (percentage).
     */
    public void setAnimationSpeed(int speed) {
        animationSpeedLiveData.setValue(speed);
        saveSettings();
    }

    /**
     * Get whether sound is enabled as LiveData.
     */
    public LiveData<Boolean> isSoundEnabled() {
        return soundEnabledLiveData;
    }

    /**
     * Get whether vibration is enabled as LiveData.
     */
    public LiveData<Boolean> isVibrationEnabled() {
        return vibrationEnabledLiveData;
    }

    /**
     * Get the animation speed as LiveData.
     */
    public LiveData<Integer> getAnimationSpeed() {
        return animationSpeedLiveData;
    }
}
