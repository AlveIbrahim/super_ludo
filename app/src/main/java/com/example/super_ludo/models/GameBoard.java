package com.example.super_ludo.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Represents the game board with all cells and paths.
 */
public class GameBoard {
    private static final int BOARD_SIZE = 11; // 11x11 grid
    private static final int PATH_LENGTH = 52; // Total path length around the board
    private static final int SAFE_ZONE_INTERVAL = 8; // Safe zones every 8 steps

    private BoardCell[][] cells;
    private Map<PlayerColor, List<BoardCell>> playerPaths;
    private Map<PlayerColor, List<BoardCell>> homePaths;
    private Map<PlayerColor, List<BoardCell>> baseCells;
    private Map<PlayerColor, BoardCell> startPositions;
    private Map<PlayerColor, BoardCell> homePositions;
    private List<BoardCell> safeZones;

    public GameBoard() {
        initializeBoard();
        createPaths();
    }

    /**
     * Initialize the board grid.
     */
    private void initializeBoard() {
        cells = new BoardCell[BOARD_SIZE][BOARD_SIZE];

        // Create all cells as empty initially
        for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
                cells[y][x] = new BoardCell(x, y, CellType.EMPTY);
            }
        }

        playerPaths = new HashMap<>();
        homePaths = new HashMap<>();
        baseCells = new HashMap<>();
        startPositions = new HashMap<>();
        homePositions = new HashMap<>();
        safeZones = new ArrayList<>();

        // Initialize collections for each player color
        for (PlayerColor color : PlayerColor.values()) {
            playerPaths.put(color, new ArrayList<>());
            homePaths.put(color, new ArrayList<>());
            baseCells.put(color, new ArrayList<>());
        }
    }

    /**
     * Create the paths, bases, and home areas for each player.
     */
    private void createPaths() {
        // Create the main path that goes around the board
        createMainPath();

        // Create colored paths, bases, and homes for each player
        createBluePlayerArea();
        createGreenPlayerArea();
        createRedPlayerArea();
        createYellowPlayerArea();

        // Mark safe zones
        markSafeZones();
    }

    /**
     * Create the main path that goes around the board.
     */
    private void createMainPath() {
        // Define the main path coordinates (simplified example)
        // In a real implementation, this would be more detailed
        // to create a complete path around the board

        // Bottom row (left to right)
        for (int x = 0; x < BOARD_SIZE; x++) {
            cells[BOARD_SIZE-1][x].setType(CellType.PATH);
        }

        // Right column (bottom to top)
        for (int y = BOARD_SIZE-2; y >= 0; y--) {
            cells[y][BOARD_SIZE-1].setType(CellType.PATH);
        }

        // Top row (right to left)
        for (int x = BOARD_SIZE-2; x >= 0; x--) {
            cells[0][x].setType(CellType.PATH);
        }

        // Left column (top to bottom)
        for (int y = 1; y < BOARD_SIZE-1; y++) {
            cells[y][0].setType(CellType.PATH);
        }
    }

    /**
     * Create the blue player's area (bottom-right).
     */
    private void createBluePlayerArea() {
        PlayerColor color = PlayerColor.BLUE;

        // Base cells (bottom-right corner)
        for (int y = BOARD_SIZE-3; y < BOARD_SIZE; y++) {
            for (int x = BOARD_SIZE-3; x < BOARD_SIZE; x++) {
                if (x == BOARD_SIZE-1 && y == BOARD_SIZE-1) continue; // Skip the corner cell (part of main path)

                cells[y][x].setType(CellType.BASE);
                cells[y][x].setColor(color);
                baseCells.get(color).add(cells[y][x]);
            }
        }

        // Start position
        BoardCell startCell = cells[BOARD_SIZE-1][BOARD_SIZE-2];
        startCell.setType(CellType.START);
        startCell.setColor(color);
        startPositions.put(color, startCell);

        // Home path (vertical path towards center)
        for (int y = BOARD_SIZE-2; y > BOARD_SIZE/2; y--) {
            cells[y][BOARD_SIZE/2].setType(CellType.HOME_PATH);
            cells[y][BOARD_SIZE/2].setColor(color);
            homePaths.get(color).add(cells[y][BOARD_SIZE/2]);
        }

        // Home position (center of board)
        BoardCell homeCell = cells[BOARD_SIZE/2][BOARD_SIZE/2];
        homeCell.setType(CellType.HOME);
        homeCell.setColor(color);
        homePositions.put(color, homeCell);
    }

    /**
     * Create the green player's area (top-right).
     */
    private void createGreenPlayerArea() {
        PlayerColor color = PlayerColor.GREEN;

        // Base cells (top-right corner)
        for (int y = 0; y < 3; y++) {
            for (int x = BOARD_SIZE-3; x < BOARD_SIZE; x++) {
                if (x == BOARD_SIZE-1 && y == 0) continue; // Skip the corner cell (part of main path)

                cells[y][x].setType(CellType.BASE);
                cells[y][x].setColor(color);
                baseCells.get(color).add(cells[y][x]);
            }
        }

        // Start position
        BoardCell startCell = cells[0][BOARD_SIZE-2];
        startCell.setType(CellType.START);    // Use startCell, not cells[y][x]
        startCell.setColor(color);             // Use startCell, not cells[y][x]
        startPositions.put(color, startCell);

        // Home path (horizontal path towards center)
        for (int x = BOARD_SIZE-2; x > BOARD_SIZE/2; x--) {
            cells[BOARD_SIZE/2][x].setType(CellType.HOME_PATH);
            cells[BOARD_SIZE/2][x].setColor(color);
            homePaths.get(color).add(cells[BOARD_SIZE/2][x]);
        }

        // Home position (center of board)
        homePositions.put(color, cells[BOARD_SIZE/2][BOARD_SIZE/2]);
    }

    /**
     * Create the red player's area (top-left).
     */
    private void createRedPlayerArea() {
        PlayerColor color = PlayerColor.RED;

        // Base cells (top-left corner)
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (x == 0 && y == 0) continue; // Skip the corner cell (part of main path)

                cells[y][x].setType(CellType.BASE);
                cells[y][x].setColor(color);
                baseCells.get(color).add(cells[y][x]);
            }
        }

        // Start position
        BoardCell startCell = cells[0][1];
        startCell.setType(CellType.START);
        startCell.setColor(color);
        startPositions.put(color, startCell);

        // Home path (vertical path towards center)
        for (int y = 1; y < BOARD_SIZE/2; y++) {
            cells[y][BOARD_SIZE/2].setType(CellType.HOME_PATH);
            cells[y][BOARD_SIZE/2].setColor(color);
            homePaths.get(color).add(cells[y][BOARD_SIZE/2]);
        }

        // Home position (center of board)
        homePositions.put(color, cells[BOARD_SIZE/2][BOARD_SIZE/2]);
    }

    /**
     * Create the yellow player's area (bottom-left).
     */
    private void createYellowPlayerArea() {
        PlayerColor color = PlayerColor.YELLOW;

        // Base cells (bottom-left corner)
        for (int y = BOARD_SIZE-3; y < BOARD_SIZE; y++) {
            for (int x = 0; x < 3; x++) {
                if (x == 0 && y == BOARD_SIZE-1) continue; // Skip the corner cell (part of main path)

                cells[y][x].setType(CellType.BASE);
                cells[y][x].setColor(color);
                baseCells.get(color).add(cells[y][x]);
            }
        }

        // Start position
        BoardCell startCell = cells[BOARD_SIZE-1][1];
        startCell.setType(CellType.START);
        startCell.setColor(color);
        startPositions.put(color, startCell);

        // Home path (horizontal path towards center)
        for (int x = 1; x < BOARD_SIZE/2; x++) {
            cells[BOARD_SIZE/2][x].setType(CellType.HOME_PATH);
            cells[BOARD_SIZE/2][x].setColor(color);
            homePaths.get(color).add(cells[BOARD_SIZE/2][x]);
        }

        // Home position (center of board)
        homePositions.put(color, cells[BOARD_SIZE/2][BOARD_SIZE/2]);
    }

    /**
     * Mark safe zones on the board.
     */
    private void markSafeZones() {
        // Mark safe zones on the main path (every 8 steps)
        // This is a simplified example

        // Bottom row
        cells[BOARD_SIZE-1][BOARD_SIZE/2].setType(CellType.SAFE_ZONE);
        safeZones.add(cells[BOARD_SIZE-1][BOARD_SIZE/2]);

        // Right column
        cells[BOARD_SIZE/2][BOARD_SIZE-1].setType (CellType.SAFE_ZONE);
        safeZones.add(cells[BOARD_SIZE/2][BOARD_SIZE-1]);

        // Top row
        cells[0][BOARD_SIZE/2].setType(CellType.SAFE_ZONE);
        safeZones.add(cells[0][BOARD_SIZE/2]);

        // Left column
        cells[BOARD_SIZE/2][0].setType(CellType.SAFE_ZONE);
        safeZones.add(cells[BOARD_SIZE/2][0]);
    }

    /**
     * Get a cell at specified coordinates.
     */
    public BoardCell getCell(int x, int y) {
        if (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
            return cells[y][x];
        }
        return null;
    }

    /**
     * Get the start position for a player.
     */
    public BoardCell getStartPosition(PlayerColor color) {
        return startPositions.get(color);
    }

    /**
     * Get the home position for a player.
     */
    public BoardCell getHomePosition(PlayerColor color) {
        return homePositions.get(color);
    }

    /**
     * Get a base position for a player's spaceship.
     */
    public BoardCell getBasePosition(PlayerColor color, int shipIndex) {
        List<BoardCell> bases = baseCells.get(color);
        if (shipIndex >= 0 && shipIndex < bases.size()) {
            return bases.get(shipIndex);
        }
        return bases.get(0); // Default to first base if index out of bounds
    }

    /**
     * Calculate the new position after moving a certain number of steps.
     */
    public BoardCell calculateNewPosition(BoardCell currentPosition, PlayerColor color, int steps) {
        // If current position is null (in base), and steps is 6, return start position
        if (currentPosition == null && steps == 6) {
            return getStartPosition(color);
        }

        // If current position is null and steps is not 6, can't move
        if (currentPosition == null && steps != 6) {
            return null;
        }

        // Calculate new position based on the main path
        // This is a simplified example

        // TODO: Implement the actual path calculation logic
        // For a real implementation, you would need to track the
        // current position in the path and calculate the new position
        // based on the steps and the player's color.

        return null;
    }

    /**
     * Get the next safe zone from a position.
     */
    public BoardCell getNextSafeZone(BoardCell currentPosition) {
        // TODO: Implement finding the next safe zone
        // This would require knowing the current position in the path
        // and finding the next safe zone in the direction of movement.

        // For now, return a random safe zone
        if (!safeZones.isEmpty()) {
            Random random = new Random();
            return safeZones.get(random.nextInt(safeZones.size()));
        }
        return null;
    }

    /**
     * Get a random cell on the board.
     */
    public BoardCell getRandomCell() {
        Random random = new Random();
        int x = random.nextInt(BOARD_SIZE);
        int y = random.nextInt(BOARD_SIZE);
        return cells[y][x];
    }

    /**
     * Check if moving from one position to another requires entering home.
     */
    public boolean isPathToHome(BoardCell fromPosition, BoardCell toPosition, PlayerColor color) {
        if (fromPosition == null || toPosition == null) {
            return false;
        }

        List<BoardCell> homePath = homePaths.get(color);
        return homePath.contains(toPosition) || toPosition.equals(homePositions.get(color));
    }

    /**
     * Get the number of steps required to reach home from the current position.
     */
    public int getStepsToHome(BoardCell currentPosition, PlayerColor color) {
        if (currentPosition == null) {
            return -1; // Can't calculate from base
        }

        // Check if already on home path
        List<BoardCell> homePath = homePaths.get(color);
        if (homePath.contains(currentPosition)) {
            int index = homePath.indexOf(currentPosition);
            return homePath.size() - index; // Steps to end of home path
        }

        // TODO: Calculate steps from main path to home
        // This would require knowing where on the main path the current
        // position is and how many steps to the entrance of the home path.

        return -1;
    }

    /**
     * Check if a cell is a safe zone.
     */
    public boolean isSafeZone(BoardCell cell) {
        return cell != null && cell.isSafeZone();
    }
}

