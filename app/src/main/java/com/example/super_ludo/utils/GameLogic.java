package com.example.super_ludo.utils;

import com.example.super_ludo.models.BoardCell;
import com.example.super_ludo.models.GameBoard;
import com.example.super_ludo.models.PlayerColor;
import com.example.super_ludo.models.Spaceship;

/**
 * Utility class for game logic operations.
 */
public class GameLogic {

    // Total path length on the main track
    private static final int TOTAL_PATH_LENGTH = 52;

    /**
     * Check if moving a ship will take it towards or into home.
     */
    public boolean isPathToHome(Spaceship ship, int newPathPosition, GameBoard board) {
        // Check if ship is approaching home straight
        PlayerColor color = ship.getColor();
        int homeEntrancePosition = getHomeEntrancePosition(color);

        // If current position is before home entrance and new position is at or past it
        if (ship.getPathPosition() < homeEntrancePosition && newPathPosition >= homeEntrancePosition) {
            return true;
        }

        return false;
    }

    /**
     * Get the number of steps needed to reach home from current position.
     */
    public int getStepsToHome(Spaceship ship, GameBoard board) {
        // If ship is in base, calculate from start
        if (ship.isInBase()) {
            PlayerColor color = ship.getColor();
            int homeEntrancePosition = getHomeEntrancePosition(color);

            // Steps to home entrance + steps through home path
            return homeEntrancePosition + getHomePathLength(color);
        }

        // If ship is already on home path
        if (isOnHomePath(ship, board)) {
            // Calculate steps to home based on home path position
            int homePathLength = getHomePathLength(ship.getColor());
            int homePathPosition = getHomePathPosition(ship, board);

            return homePathLength - homePathPosition;
        }

        // Ship is on main path, calculate steps to home entrance, then add home path length
        PlayerColor color = ship.getColor();
        int homeEntrancePosition = getHomeEntrancePosition(color);
        int currentPosition = ship.getPathPosition();

        int stepsToHomeEntrance;
        if (currentPosition <= homeEntrancePosition) {
            stepsToHomeEntrance = homeEntrancePosition - currentPosition;
        } else {
            // Need to go around the board
            stepsToHomeEntrance = (TOTAL_PATH_LENGTH - currentPosition) + homeEntrancePosition;
        }

        // Add steps through home path
        return stepsToHomeEntrance + getHomePathLength(color);
    }

    /**
     * Check if a ship is on its home path.
     */
    public boolean isOnHomePath(Spaceship ship, GameBoard board) {
        BoardCell position = ship.getPosition();
        if (position == null) return false;

        // Check if cell is part of home path for this color
        return position.getType() == com.giiker.superludo.models.CellType.HOME_PATH &&
                position.getColor() == ship.getColor();
    }

    /**
     * Get a ship's position on its home path (0 = entrance, higher = closer to home).
     */
    public int getHomePathPosition(Spaceship ship, GameBoard board) {
        // This is a simplified implementation
        // In a real game, you would need to know the exact position in the home path

        // For our purposes, we'll return a fixed value for demonstration
        return 0;
    }

    /**
     * Get the board cell from a path index.
     */
    public BoardCell getPositionFromPathIndex(int pathIndex, PlayerColor color, GameBoard board) {
        // This is a simplified implementation
        // In a real game, you would need a map of path indices to board cells

        // For our purposes, we'll use a placeholder approach
        if (pathIndex < 0) {
            // In base
            return board.getBasePosition(color, 0);
        } else if (pathIndex == 0) {
            // At start position
            return board.getStartPosition(color);
        } else if (pathIndex >= TOTAL_PATH_LENGTH) {
            // At or past home entrance
            return board.getHomePosition(color);
        } else {
            // On main path
            // This is a simplified approach - in a real implementation,
            // you would have a mapping of path indices to actual board cells

            // For now, we'll just return a random cell as a placeholder
            int x = pathIndex % 11;
            int y = pathIndex / 11;
            return board.getCell(x, y);
        }
    }

    /**
     * Get the path position where a player's home entrance is located.
     */
    private int getHomeEntrancePosition(PlayerColor color) {
        // This would depend on the board layout
        // For our simplified implementation:
        switch (color) {
            case BLUE:
                return 0;
            case GREEN:
                return 13;
            case RED:
                return 26;
            case YELLOW:
                return 39;
            default:
                return 0;
        }
    }

    /**
     * Get the length of a player's home path.
     */
    private int getHomePathLength(PlayerColor color) {
        // In our implementation, all home paths are 5 steps long
        return 5;
    }
}