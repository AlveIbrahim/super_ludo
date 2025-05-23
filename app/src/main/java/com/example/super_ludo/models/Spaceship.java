package com.example.super_ludo.models;

/**
 * Represents a player's spaceship that moves around the board.
 */
public class Spaceship {
    private Player owner;
    private int shipNumber;
    private BoardCell position;
    private boolean inBase;
    private boolean reachedHome;
    private int pathPosition; // Position in the path from 0 to totalPathLength

    public Spaceship(Player owner, int shipNumber) {
        this.owner = owner;
        this.shipNumber = shipNumber;
        this.inBase = true;
        this.reachedHome = false;
        this.pathPosition = -1; // -1 means in base
    }

    public Player getOwner() {
        return owner;
    }

    public int getShipNumber() {
        return shipNumber;
    }

    public BoardCell getPosition() {
        return position;
    }

    public void setPosition(BoardCell position) {
        this.position = position;
        this.inBase = false;
    }

    public boolean isInBase() {
        return inBase;
    }

    public void returnToBase() {
        this.inBase = true;
        this.position = null;
        this.pathPosition = -1;
    }

    public boolean hasReachedHome() {
        return reachedHome;
    }

    public void setReachedHome(boolean reachedHome) {
        this.reachedHome = reachedHome;
        if (reachedHome) {
            this.inBase = false; // Can't be in base if reached home
        }
    }

    public int getPathPosition() {
        return pathPosition;
    }

    public void setPathPosition(int pathPosition) {
        this.pathPosition = pathPosition;
    }

    public PlayerColor getColor() {
        return owner.getColor();
    }

    /**
     * Calculate the new path position after moving a certain number of steps.
     *
     * @param steps Number of steps to move
     * @return The new path position (or -1 if can't move)
     */
    public int calculateNewPathPosition(int steps) {
        // If in base and not rolling a 6, can't move
        if (isInBase() && steps != 6) {
            return -1;
        }

        // If in base and rolling a 6, move to start position (0)
        if (isInBase() && steps == 6) {
            return 0;
        }

        // Calculate new position
        return pathPosition + steps;
    }
}