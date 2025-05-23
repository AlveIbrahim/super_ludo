package com.example.super_ludo.models;

/**
 * Represents an event triggered by the Crystal Ball.
 */
public class GameEvent {
    private EventType type;
    private int value; // For normal roll (1-6)

    public GameEvent(EventType type) {
        this.type = type;
        this.value = 0;
    }

    public GameEvent(EventType type, int value) {
        this.type = type;
        this.value = value;
    }

    public EventType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    /**
     * Get the drawable resource ID for this event.
     */
    public int getDrawableResourceId() {
        switch (type) {
            case NORMAL_ROLL:
                return com.example.super_ludo.R.drawable.crystal_ball_normal;
            case BLACK_HOLE:
                return com.example.super_ludo.R.drawable.crystal_ball_black_hole;
            case METEOR_STRIKE:
                return com.example.super_ludo.R.drawable.crystal_ball_meteor;
            case WORMHOLE:
                return com.example.super_ludo.R.drawable.crystal_ball_wormhole;
            case SUPER_BOOST:
                return com.example.super_ludo.R.drawable.crystal_ball_boost;
            case ALIEN_INVASION:
                return com.example.super_ludo.R.drawable.crystal_ball_alien;
            default:
                return com.example.super_ludo.R.drawable.crystal_ball;
        }
    }

    /**
     * Get the title string resource ID for this event.
     */
    public int getTitleResourceId() {
        switch (type) {
            case NORMAL_ROLL:
                return com.example.super_ludo.R.string.normal_roll;
            case BLACK_HOLE:
                return com.example.super_ludo.R.string.black_hole;
            case METEOR_STRIKE:
                return com.example.super_ludo.R.string.meteor_strike;
            case WORMHOLE:
                return com.example.super_ludo.R.string.wormhole;
            case SUPER_BOOST:
                return com.example.super_ludo.R.string.super_boost;
            case ALIEN_INVASION:
                return com.example.super_ludo.R.string.alien_invasion;
            default:
                return com.example.super_ludo.R.string.event_title;
        }
    }

    /**
     * Get the description string resource ID for this event.
     */
    public int getDescriptionResourceId() {
        switch (type) {
            case NORMAL_ROLL:
                return com.example.super_ludo.R.string.normal_roll_desc;
            case BLACK_HOLE:
                return com.example.super_ludo.R.string.black_hole_desc;
            case METEOR_STRIKE:
                return com.example.super_ludo.R.string.meteor_strike_desc;
            case WORMHOLE:
                return com.example.super_ludo.R.string.wormhole_desc;
            case SUPER_BOOST:
                return com.example.super_ludo.R.string.super_boost_desc;
            case ALIEN_INVASION:
                return com.example.super_ludo.R.string.alien_invasion_desc;
            default:
                return com.example.super_ludo.R.string.event_description;
        }
    }
}
