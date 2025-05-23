package com.example.super_ludo.models;

/**
 * Types of cells on the game board.
 */
public enum CellType {
    EMPTY,      // Empty cell, not part of the path
    PATH,       // Regular path cell
    BASE,       // Player's base where spaceships start
    START,      // Starting position when leaving base
    SAFE_ZONE,  // Safe zone where spaceships can't be captured
    HOME,       // Final destination for spaceships
    HOME_PATH   // Path leading to home
}
