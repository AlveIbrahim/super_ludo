package com.example.super_ludo.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.super_ludo.models.BoardCell;
import com.example.super_ludo.models.EventType;
import com.example.super_ludo.models.GameEvent;
import com.example.super_ludo.models.GameState;
import com.example.super_ludo.models.Player;
import com.example.super_ludo.models.Spaceship;
import com.example.super_ludo.utils.GameLogic;

import java.util.List;
import java.util.Random;

/**
 * ViewModel for managing game state and logic.
 */
public class GameViewModel extends AndroidViewModel {

    private GameLogic gameLogic;
    private GameState gameState;

    // LiveData for observing changes
    private MutableLiveData<GameState> gameStateLiveData = new MutableLiveData<>();
    private MutableLiveData<GameEvent> currentEventLiveData = new MutableLiveData<>();
    private MutableLiveData<Spaceship> selectedSpaceshipLiveData = new MutableLiveData<>();
    private MutableLiveData<BoardCell> highlightedCellLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> waitingForShipSelectionLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> waitingForTargetSelectionLiveData = new MutableLiveData<>();
    private MutableLiveData<String> statusMessageLiveData = new MutableLiveData<>();

    // Random number generator
    private Random random = new Random();

    // Game state flags
    private boolean waitingForShipSelection = false;
    private boolean waitingForTargetSelection = false;
    private Spaceship movingShip = null;

    public GameViewModel(@NonNull Application application) {
        super(application);

        // Initialize game state
        gameState = new GameState();

        // Initialize game logic
        gameLogic = new GameLogic();

        // Set initial values
        gameStateLiveData.setValue(gameState);
        waitingForShipSelectionLiveData.setValue(false);
        waitingForTargetSelectionLiveData.setValue(false);
        statusMessageLiveData.setValue("Welcome to GiiKER Super Ludo!");
    }

    /**
     * Initialize a new game with the specified number of players.
     */
    public void initializeGame(int playerCount) {
        gameState.initialize(playerCount);
        gameStateLiveData.setValue(gameState);
        statusMessageLiveData.setValue("Game started! " + gameState.getCurrentPlayer().getName() + "'s turn.");
    }

    /**
     * Reset the current game.
     */
    public void resetGame() {
        gameState.resetGame();
        gameStateLiveData.setValue(gameState);
        currentEventLiveData.setValue(null);
        selectedSpaceshipLiveData.setValue(null);
        highlightedCellLiveData.setValue(null);
        waitingForShipSelection = false;
        waitingForShipSelectionLiveData.setValue(false);
        waitingForTargetSelection = false;
        waitingForTargetSelectionLiveData.setValue(false);
        movingShip = null;
        statusMessageLiveData.setValue("Game reset! " + gameState.getCurrentPlayer().getName() + "'s turn.");
    }

    /**
     * Process a crystal ball roll event.
     */
    public void processRoll(GameEvent event) {
        // Record the event
        gameState.setLastEvent(event);
        currentEventLiveData.setValue(event);

        // Handle the event based on its type
        switch (event.getType()) {
            case NORMAL_ROLL:
                handleNormalRoll(event.getValue());
                break;

            case BLACK_HOLE:
                handleBlackHole();
                break;

            case METEOR_STRIKE:
                handleMeteorStrike();
                break;

            case WORMHOLE:
                handleWormhole();
                break;

            case SUPER_BOOST:
                handleSuperBoost();
                break;

            case ALIEN_INVASION:
                handleAlienInvasion();
                break;
        }

        // Update LiveData
        gameStateLiveData.setValue(gameState);
    }

    /**
     * Handle a normal dice roll (1-6).
     */
    private void handleNormalRoll(int steps) {
        Player currentPlayer = gameState.getCurrentPlayer();

        // Check if player has any valid moves
        boolean hasValidMoves = false;

        for (Spaceship ship : currentPlayer.getSpaceships()) {
            // Can only move ships that are in base if rolled a 6
            if (ship.isInBase() && steps != 6) {
                continue;
            }

            // Can't move ships that have reached home
            if (ship.hasReachedHome()) {
                continue;
            }

            hasValidMoves = true;
            break;
        }

        if (hasValidMoves) {
            // Ask player to select a ship
            waitingForShipSelection = true;
            waitingForShipSelectionLiveData.setValue(true);
            statusMessageLiveData.setValue("Select a spaceship to move " + steps + " spaces.");
        } else {
            // No valid moves, go to next player
            statusMessageLiveData.setValue("No valid moves available. Turn passes to next player.");
            endTurn();
        }
    }

    /**
     * Handle the Black Hole event (skip turn).
     */
    private void handleBlackHole() {
        statusMessageLiveData.setValue("Black Hole! You lose your turn.");
        endTurn();
    }

    /**
     * Handle the Meteor Strike event (send opponent back to base).
     */
    private void handleMeteorStrike() {
        // Check if there are any opponent ships that can be sent back
        List<Player> players = gameState.getPlayers();
        Player currentPlayer = gameState.getCurrentPlayer();

        // Collect all opponent ships that are not in base or home
        boolean hasTargets = false;

        for (Player player : players) {
            if (player == currentPlayer) continue;

            for (Spaceship ship : player.getSpaceships()) {
                if (!ship.isInBase() && !ship.hasReachedHome() &&
                        !gameState.getBoard().isSafeZone(ship.getPosition())) {
                    hasTargets = true;
                    break;
                }
            }

            if (hasTargets) break;
        }

        if (hasTargets) {
            // Ask player to select an opponent's ship
            waitingForTargetSelection = true;
            waitingForTargetSelectionLiveData.setValue(true);
            statusMessageLiveData.setValue("Meteor Strike! Select an opponent's spaceship to send back to base.");
        } else {
            // No valid targets
            statusMessageLiveData.setValue("Meteor Strike! But there are no spaceships to hit. Turn passes to next player.");
            endTurn();
        }
    }

    /**
     * Handle the Wormhole event (jump to safe zone).
     */
    private void handleWormhole() {
        // Check if player has any ships that are not in base or home
        Player currentPlayer = gameState.getCurrentPlayer();
        boolean hasValidShips = false;

        for (Spaceship ship : currentPlayer.getSpaceships()) {
            if (!ship.isInBase() && !ship.hasReachedHome()) {
                hasValidShips = true;
                break;
            }
        }

        if (hasValidShips) {
            // Ask player to select a ship
            waitingForShipSelection = true;
            waitingForShipSelectionLiveData.setValue(true);
            statusMessageLiveData.setValue("Wormhole! Select a spaceship to jump to the next safe zone.");
        } else {
            // No valid ships
            statusMessageLiveData.setValue("Wormhole! But you have no spaceships to move. Turn passes to next player.");
            endTurn();
        }
    }

    /**
     * Handle the Super Boost event (extra turn).
     */
    private void handleSuperBoost() {
        // Give player an extra turn
        gameState.setExtraTurn();
        statusMessageLiveData.setValue("Super Boost! You get an extra turn.");

        // Since we're not changing players, just update and continue
        waitingForShipSelection = false;
        waitingForShipSelectionLiveData.setValue(false);
        waitingForTargetSelection = false;
        waitingForTargetSelectionLiveData.setValue(false);

        // End this turn (which will not change the player due to extra turn flag)
        endTurn();
    }

    /**
     * Handle the Alien Invasion event (send opponent to random location).
     */
    private void handleAlienInvasion() {
        // Similar to Meteor Strike, but we'll move the ship to a random location
        List<Player> players = gameState.getPlayers();
        Player currentPlayer = gameState.getCurrentPlayer();

        // Collect all opponent ships that are not in base or home
        boolean hasTargets = false;

        for (Player player : players) {
            if (player == currentPlayer) continue;

            for (Spaceship ship : player.getSpaceships()) {
                if (!ship.isInBase() && !ship.hasReachedHome()) {
                    hasTargets = true;
                    break;
                }
            }

            if (hasTargets) break;
        }

        if (hasTargets) {
            // Ask player to select an opponent's ship
            waitingForTargetSelection = true;
            waitingForTargetSelectionLiveData.setValue(true);
            statusMessageLiveData.setValue("Alien Invasion! Select an opponent's spaceship to send to a random location.");
        } else {
            // No valid targets
            statusMessageLiveData.setValue("Alien Invasion! But there are no spaceships to abduct. Turn passes to next player.");
            endTurn();
        }
    }

    /**
     * Handle a spaceship selection.
     */
    public void selectSpaceship(Spaceship ship) {
        if (!waitingForShipSelection && !waitingForTargetSelection) {
            return;
        }

        Player currentPlayer = gameState.getCurrentPlayer();
        GameEvent currentEvent = gameState.getLastEvent();

        if (waitingForShipSelection) {
            // Make sure ship belongs to current player
            if (ship.getOwner() != currentPlayer) {
                statusMessageLiveData.setValue("You can only select your own spaceships.");
                return;
            }

            // Make sure ship is valid for the current event
            if (currentEvent.getType() == EventType.NORMAL_ROLL) {
                // For normal roll, can only move ships that are in base if rolled a 6
                if (ship.isInBase() && currentEvent.getValue() != 6) {
                    statusMessageLiveData.setValue("You need to roll a 6 to move a spaceship out of base.");
                    return;
                }

                // Can't move ships that have reached home
                if (ship.hasReachedHome()) {
                    statusMessageLiveData.setValue("This spaceship has already reached home.");
                    return;
                }
            } else if (currentEvent.getType() == EventType.WORMHOLE) {
                // For wormhole, can only move ships that are not in base or home
                if (ship.isInBase() || ship.hasReachedHome()) {
                    statusMessageLiveData.setValue("You can only select a spaceship that is in flight.");
                    return;
                }
            }

            // Set selected ship
            selectedSpaceshipLiveData.setValue(ship);
            movingShip = ship;

            // Process movement based on event
            if (currentEvent.getType() == EventType.NORMAL_ROLL) {
                moveShip(ship, currentEvent.getValue());
            } else if (currentEvent.getType() == EventType.WORMHOLE) {
                moveToNextSafeZone(ship);
            }

            // Reset selection state
            waitingForShipSelection = false;
            waitingForShipSelectionLiveData.setValue(false);

            // Check win condition
            if (checkWinCondition()) {
                return;
            }

            // End turn
            endTurn();
        } else if (waitingForTargetSelection) {
            // For target selection, make sure ship belongs to an opponent
            if (ship.getOwner() == currentPlayer) {
                statusMessageLiveData.setValue("You can only select an opponent's spaceship.");
                return;
            }

            // Make sure ship is valid for the current event
            if (currentEvent.getType() == EventType.METEOR_STRIKE) {
                // For meteor strike, can't target ships in base, home, or safe zones
                if (ship.isInBase() || ship.hasReachedHome() ||
                        gameState.getBoard().isSafeZone(ship.getPosition())) {
                    statusMessageLiveData.setValue("You can't target this spaceship.");
                    return;
                }

                // Send ship back to base
                sendToBase(ship);
            } else if (currentEvent.getType() == EventType.ALIEN_INVASION) {
                // For alien invasion, can't target ships in base or home
                if (ship.isInBase() || ship.hasReachedHome()) {
                    statusMessageLiveData.setValue("You can't target this spaceship.");
                    return;
                }

                // Send ship to random location
                sendToRandomLocation(ship);
            }

            // Reset selection state
            waitingForTargetSelection = false;
            waitingForTargetSelectionLiveData.setValue(false);

            // End turn
            endTurn();
        }
    }

    /**
     * Move a ship by a number of steps.
     */
    private void moveShip(Spaceship ship, int steps) {
        if (ship.isInBase() && steps == 6) {
            // Move ship from base to start position
            BoardCell startPosition = gameState.getBoard().getStartPosition(ship.getColor());

            // First remove from current position if any
            if (ship.getPosition() != null) {
                ship.getPosition().removeSpaceship(ship);
            }

            // Set new position
            ship.setPosition(startPosition);
            startPosition.addSpaceship(ship);

            // Set path position to 0 (start)
            ship.setPathPosition(0);

            statusMessageLiveData.setValue("Spaceship moved from base to start position.");
        } else if (!ship.isInBase()) {
            // Calculate new path position
            int newPathPosition = ship.calculateNewPathPosition(steps);

            // Check if this would take the ship past home
            if (gameLogic.isPathToHome(ship, newPathPosition, gameState.getBoard())) {
                int stepsToHome = gameLogic.getStepsToHome(ship, gameState.getBoard());

                if (steps == stepsToHome) {
                    // Exact count to reach home
                    moveToHome(ship);
                    statusMessageLiveData.setValue("Spaceship reached home!");
                } else if (steps > stepsToHome) {
                    // Too many steps, can't move
                    statusMessageLiveData.setValue("You need an exact count to reach home.");
                } else {
                    // Move along the path
                    moveAlongPath(ship, newPathPosition);
                    statusMessageLiveData.setValue("Spaceship moved " + steps + " spaces.");
                }
            } else {
                // Regular movement along the path
                moveAlongPath(ship, newPathPosition);
                statusMessageLiveData.setValue("Spaceship moved " + steps + " spaces.");
            }
        }

        // Check for collisions
        checkForCollisions(ship);

        // Update game state
        gameStateLiveData.setValue(gameState);
    }

    /**
     * Move a ship to the next safe zone.
     */
    private void moveToNextSafeZone(Spaceship ship) {
        BoardCell nextSafeZone = gameState.getBoard().getNextSafeZone(ship.getPosition());

        if (nextSafeZone != null) {
            // First remove from current position
            if (ship.getPosition() != null) {
                ship.getPosition().removeSpaceship(ship);
            }

            // Set new position
            ship.setPosition(nextSafeZone);
            nextSafeZone.addSpaceship(ship);

            // Update path position (approximate)
            // This is a simplified approach - in a real implementation,
            // you would need to know the exact path position of the safe zone
            ship.setPathPosition(ship.getPathPosition() + 10);

            statusMessageLiveData.setValue("Spaceship jumped to the next safe zone!");
        } else {
            statusMessageLiveData.setValue("No safe zone found ahead. Spaceship stays in place.");
        }

        // Check for collisions
        checkForCollisions(ship);

        // Update game state
        gameStateLiveData.setValue(gameState);
    }

    /**
     * Move a ship to the home position.
     */
    private void moveToHome(Spaceship ship) {
        // First remove from current position
        if (ship.getPosition() != null) {
            ship.getPosition().removeSpaceship(ship);
        }

        // Set new position to home
        BoardCell homePosition = gameState.getBoard().getHomePosition(ship.getColor());
        ship.setPosition(homePosition);
        homePosition.addSpaceship(ship);

        // Mark as reached home
        ship.setReachedHome(true);

        // Update game state
        gameStateLiveData.setValue(gameState);
    }

    /**
     * Move a ship along the path to a new position.
     */
    private void moveAlongPath(Spaceship ship, int newPathPosition) {
        // Calculate the new board cell based on path position
        BoardCell newPosition = gameLogic.getPositionFromPathIndex(
                newPathPosition, ship.getColor(), gameState.getBoard());

        if (newPosition != null) {
            // First remove from current position
            if (ship.getPosition() != null) {
                ship.getPosition().removeSpaceship(ship);
            }

            // Set new position
            ship.setPosition(newPosition);
            newPosition.addSpaceship(ship);

            // Update path position
            ship.setPathPosition(newPathPosition);
        }
    }

    /**
     * Send a ship back to its base.
     */
    private void sendToBase(Spaceship ship) {
        // First remove from current position
        if (ship.getPosition() != null) {
            ship.getPosition().removeSpaceship(ship);
        }

        // Return to base
        ship.returnToBase();

        statusMessageLiveData.setValue("Opponent's spaceship sent back to base!");

        // Update game state
        gameStateLiveData.setValue(gameState);
    }

    /**
     * Send a ship to a random location on the board.
     */
    private void sendToRandomLocation(Spaceship ship) {
        // First remove from current position
        if (ship.getPosition() != null) {
            ship.getPosition().removeSpaceship(ship);
        }

        // Get a random path position
        int randomPathPosition = random.nextInt(52); // 52 is the total path length

        // Get the corresponding board cell
        BoardCell randomPosition = gameLogic.getPositionFromPathIndex(
                randomPathPosition, ship.getColor(), gameState.getBoard());

        if (randomPosition != null) {
            // Set new position
            ship.setPosition(randomPosition);
            randomPosition.addSpaceship(ship);

            // Update path position
            ship.setPathPosition(randomPathPosition);

            statusMessageLiveData.setValue("Opponent's spaceship teleported to a random location!");
        } else {
            // If random position is invalid, just send back to base
            ship.returnToBase();
            statusMessageLiveData.setValue("Opponent's spaceship sent back to base!");
        }

        // Update game state
        gameStateLiveData.setValue(gameState);
    }

    /**
     * Check for collisions with opponent ships.
     */
    private void checkForCollisions(Spaceship movedShip) {
        if (movedShip.isInBase() || movedShip.hasReachedHome() ||
                gameState.getBoard().isSafeZone(movedShip.getPosition())) {
            return; // No collisions possible
        }

        BoardCell currentPosition = movedShip.getPosition();
        if (currentPosition == null) return;

        for (Spaceship otherShip : currentPosition.getSpaceships()) {
            if (otherShip != movedShip && otherShip.getColor() != movedShip.getColor()) {
                // Collision with opponent ship
                sendToBase(otherShip);
                statusMessageLiveData.setValue(statusMessageLiveData.getValue() +
                        " Opponent's spaceship sent back to base!");
            }
        }
    }

    /**
     * Check if the current player has won.
     */
    private boolean checkWinCondition() {
        if (gameState.checkWinCondition()) {
            Player winner = gameState.getWinner();
            statusMessageLiveData.setValue("Game Over! " + winner.getName() + " wins!");
            return true;
        }
        return false;
    }

    /**
     * End the current turn and move to the next player.
     */
    private void endTurn() {
        // Move to next player
        gameState.nextTurn();

        // Reset selection state
        selectedSpaceshipLiveData.setValue(null);
        highlightedCellLiveData.setValue(null);

        // Update game state
        gameStateLiveData.setValue(gameState);

        // Update status message for next player
        if (!gameState.isGameOver()) {
            String baseMessage = statusMessageLiveData.getValue();
            if (!baseMessage.endsWith(".")) baseMessage += ".";

            statusMessageLiveData.setValue(baseMessage + " " +
                    gameState.getCurrentPlayer().getName() + "'s turn.");
        }
    }

    /**
     * Get the current game state as LiveData.
     */
    public LiveData<GameState> getGameState() {
        return gameStateLiveData;
    }

    /**
     * Get the current event as LiveData.
     */
    public LiveData<GameEvent> getCurrentEvent() {
        return currentEventLiveData;
    }

    /**
     * Get the selected spaceship as LiveData.
     */
    public LiveData<Spaceship> getSelectedSpaceship() {
        return selectedSpaceshipLiveData;
    }

    /**
     * Get the highlighted cell as LiveData.
     */
    public LiveData<BoardCell> getHighlightedCell() {
        return highlightedCellLiveData;
    }

    /**
     * Get whether we're waiting for ship selection as LiveData.
     */
    public LiveData<Boolean> isWaitingForShipSelection() {
        return waitingForShipSelectionLiveData;
    }

    /**
     * Get whether we're waiting for target selection as LiveData.
     */
    public LiveData<Boolean> isWaitingForTargetSelection() {
        return waitingForTargetSelectionLiveData;
    }

    /**
     * Get the current status message as LiveData.
     */
    public LiveData<String> getStatusMessage() {
        return statusMessageLiveData;
    }
}