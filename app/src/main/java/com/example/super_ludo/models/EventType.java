package com.example.super_ludo.models;

/**
 * Types of events that can be triggered by the Crystal Ball.
 */
public enum EventType {
    NORMAL_ROLL,    // Regular dice roll (1-6)
    BLACK_HOLE,     // Skip a turn
    METEOR_STRIKE,  // Send opponent back to base
    WORMHOLE,       // Jump forward to safe zone
    SUPER_BOOST,    // Extra turn
    ALIEN_INVASION  // Send opponent's ship to a random location
}
