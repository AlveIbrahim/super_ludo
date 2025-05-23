package com.example.super_ludo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the current state of the game.
 */
public class GameState {
    private List<Player> players;
    private GameBoard board;
    private int currentPlayerIndex;
    private Player currentPlayer;
    private GameEvent lastEvent;
    private boolean gameOver;
    private Player winner;
    private boolean extraTurn;

    public GameState() {
        this.players = new ArrayList<>();
        this.board = new GameBoard();
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.extraTurn = false;
    }

    public void initialize(int playerCount) {
        // Clear any existing players
        players.clear();

        // Create players with default names
        PlayerColor[] colors = PlayerColor.values();
        for (int i = 0; i < playerCount && i < colors.length; i++) {
            Player player = new Player("Player " + (i + 1), colors[i]);
            players.add(player);
        }

        // Set first player as current
        currentPlayerIndex = 0;
        currentPlayer = players.get(0);
        currentPlayer.setActive(true);

        // Reset game state
        gameOver = false;
        winner = null;
        extraTurn = false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameBoard getBoard() {
        return board;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameEvent getLastEvent() {
        return lastEvent;
    }

    public void setLastEvent(GameEvent lastEvent) {
        this.lastEvent = lastEvent;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Player getWinner() {
        return winner;
    }

    /**
     * Move to the next player's turn.
     */
    public void nextTurn() {
        if (gameOver) return;

        // If player gets an extra turn, don't change players
        if (extraTurn) {
            extraTurn = false;
            return;
        }

        // Set current player as inactive
        currentPlayer.setActive(false);

        // Move to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setActive(true);
    }

    /**
     * Set that the current player gets an extra turn.
     */
    public void setExtraTurn() {
        this.extraTurn = true;
    }

    /**
     * Check if any player has won and update game state.
     */
    public boolean checkWinCondition() {
        for (Player player : players) {
            if (player.hasWon()) {
                gameOver = true;
                winner = player;
                return true;
            }
        }
        return false;
    }

    /**
     * Reset the game state for a new game.
     */
    public void resetGame() {
        // Keep the same players but reset their state
        for (Player player : players) {
            // Reset spaceships to base
            for (Spaceship ship : player.getSpaceships()) {
                ship.returnToBase();
                ship.setReachedHome(false);
            }
            player.setActive(false);
        }

        // Set first player as current again
        currentPlayerIndex = 0;
        currentPlayer = players.get(0);
        currentPlayer.setActive(true);

        // Reset game state
        gameOver = false;
        winner = null;
        extraTurn = false;
        lastEvent = null;
    }
}
