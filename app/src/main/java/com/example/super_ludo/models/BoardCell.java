package com.example.super_ludo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a cell on the game board.
 */
public class BoardCell {
    private int x;
    private int y;
    private CellType type;
    private PlayerColor color; // For colored paths
    private List<Spaceship> spaceships; // Spaceships currently on this cell

    public BoardCell(int x, int y, CellType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.spaceships = new ArrayList<>();
        this.color = null; // Default to no color
    }

    public BoardCell(int x, int y, CellType type, PlayerColor color) {
        this(x, y, type);
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public List<Spaceship> getSpaceships() {
        return spaceships;
    }

    public void addSpaceship(Spaceship spaceship) {
        if (!spaceships.contains(spaceship)) {
            spaceships.add(spaceship);
        }
    }

    public void removeSpaceship(Spaceship spaceship) {
        spaceships.remove(spaceship);
    }

    public boolean isEmpty() {
        return spaceships.isEmpty();
    }

    public boolean isSafeZone() {
        return type == CellType.SAFE_ZONE;
    }

    public boolean isBase() {
        return type == CellType.BASE;
    }

    public boolean isHome() {
        return type == CellType.HOME;
    }

    public boolean isStart() {
        return type == CellType.START;
    }

    public boolean hasOpponentSpaceship(PlayerColor playerColor) {
        for (Spaceship ship : spaceships) {
            if (ship.getColor() != playerColor) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get all opponent spaceships on this cell.
     */
    public List<Spaceship> getOpponentSpaceships(PlayerColor playerColor) {
        List<Spaceship> opponentShips = new ArrayList<>();
        for (Spaceship ship : spaceships) {
            if (ship.getColor() != playerColor) {
                opponentShips.add(ship);
            }
        }
        return opponentShips;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BoardCell)) return false;
        BoardCell other = (BoardCell) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}