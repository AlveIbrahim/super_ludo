package com.example.super_ludo.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.example.super_ludo.R;

/**
 * Utility class for managing game sounds.
 */
public class SoundManager {

    // Sound constants
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

    private SoundPool soundPool;
    private SparseIntArray soundMap;
    private boolean soundEnabled = true;
    private Context context;

    public SoundManager(Context context) {
        this.context = context;
        initSoundPool();
        loadSounds();
    }

    private void initSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(attributes)
                .build();

        soundMap = new SparseIntArray();
    }

    private void loadSounds() {
        // Load sound resources
        // Note: These are placeholder resource IDs since we're not including actual sound files
        soundMap.put(SOUND_DICE, soundPool.load(context, R.raw.sound_dice, 1));
        soundMap.put(SOUND_ROLL, soundPool.load(context, R.raw.sound_roll, 1));
        soundMap.put(SOUND_SELECT, soundPool.load(context, R.raw.sound_select, 1));
        soundMap.put(SOUND_MOVE, soundPool.load(context, R.raw.sound_move, 1));
        soundMap.put(SOUND_CAPTURE, soundPool.load(context, R.raw.sound_capture, 1));
        soundMap.put(SOUND_BLACK_HOLE, soundPool.load(context, R.raw.sound_black_hole, 1));
        soundMap.put(SOUND_METEOR, soundPool.load(context, R.raw.sound_meteor, 1));
        soundMap.put(SOUND_WORMHOLE, soundPool.load(context, R.raw.sound_wormhole, 1));
        soundMap.put(SOUND_BOOST, soundPool.load(context, R.raw.sound_boost, 1));
        soundMap.put(SOUND_ALIEN, soundPool.load(context, R.raw.sound_alien, 1));
        soundMap.put(SOUND_VICTORY, soundPool.load(context, R.raw.sound_victory, 1));
    }

    public void playSound(int soundId) {
        if (soundEnabled) {
            soundPool.play(soundMap.get(soundId), 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    public void pauseAll() {
        soundPool.autoPause();
    }

    public void resumeAll() {
        soundPool.autoResume();
    }

    public void release() {
        soundPool.release();
        soundPool = null;
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;

        if (!enabled) {
            pauseAll();
        }
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}