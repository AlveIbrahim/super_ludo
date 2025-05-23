package com.example.super_ludo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game with their spaceships and color.
 */
public class Player {
    private String name;
    private PlayerColor color;
    private List<Spaceship> spaceships;
    private boolean isActive;

    public Player(String name, PlayerColor color) {
        this.name = name;
        this.color = color;
        this.spaceships = new ArrayList<>(3); // Each player has 3 spaceships
        this.isActive = false;

        // Create the player's spaceships
        for (int i = 0; i < 3; i++) {
            Spaceship ship = new Spaceship(this, i + 1);
            spaceships.add(ship);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerColor getColor() {
        return color;
    }

    public List<Spaceship> getSpaceships() {
        return spaceships;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Check if all spaceships have reached home.
     */
    public boolean hasWon() {
        for (Spaceship ship : spaceships) {
            if (!ship.hasReachedHome()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the number of spaceships that have reached home.
     */
    public int getHomeCount() {
        int count = 0;
        for (Spaceship ship : spaceships) {
            if (ship.hasReachedHome()) {
                count++;
            }
        }
        return count;
    }
}
