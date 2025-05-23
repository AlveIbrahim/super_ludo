package com.example.super_ludo.utils;

/**
 * Utility class for game constants.
 */
public class Constants {

    // Board dimensions
    public static final int BOARD_SIZE = 11;
    public static final int TOTAL_PATH_LENGTH = 52;

    // Game constants
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    public static final int SHIPS_PER_PLAYER = 3;

    // Event probabilities (percentages)
    public static final int PROB_NORMAL_ROLL = 65;
    public static final int PROB_BLACK_HOLE = 10;
    public static final int PROB_METEOR_STRIKE = 7;
    public static final int PROB_WORMHOLE = 8;
    public static final int PROB_SUPER_BOOST = 5;
    public static final int PROB_ALIEN_INVASION = 5;

    // Animation durations
    public static final int ANIM_DURATION_ROLL = 1000; // ms
    public static final int ANIM_DURATION_MOVE = 500; // ms
    public static final int ANIM_DURATION_POPUP = 300; // ms

    // Vibration durations
    public static final int VIBRATE_DURATION_ROLL = 200; // ms
    public static final int VIBRATE_DURATION_EVENT = 100; // ms

    // Preference keys
    public static final String PREF_PLAYER_COUNT = "player_count";
    public static final String PREF_PLAYER_NAME_PREFIX = "player_name_";

    // Intent extras
    public static final String EXTRA_PLAYER_COUNT = "player_count";
}
