package com.example.super_ludo.utils;

import android.content.Context;

/**
 * Dummy SoundManager - does nothing but prevents errors
 */
public class SoundManager {

    // Sound constants (kept for compatibility)
    public static final int SOUND_DICE = 0;
    public static final int SOUND_ROLL = 1;
    public static final int SOUND_SELECT = 2;
    public static final int SOUND_MOVE = 3;
    public static final int SOUND_CAPTURE = 4;
    public static final int SOUND_BLACK_HOLE = 5;
    public static final int SOUND_METEOR = 6;
    public static final int SOUND_WORMHOLE = 7;
    public static final int SOUND_BOOST = 8;
    public static final int SOUND_ALIEN = 9;
    public static final int SOUND_VICTORY = 10;

    public SoundManager(Context context) {
        // Do nothing
    }

    public void playSound(int soundId) {
        // Do nothing
    }

    public void pauseAll() {
        // Do nothing
    }

    public void resumeAll() {
        // Do nothing
    }

    public void release() {
        // Do nothing
    }

    public void setSoundEnabled(boolean enabled) {
        // Do nothing
    }

    public boolean isSoundEnabled() {
        return false;
    }
}