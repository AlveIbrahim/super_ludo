package com.example.super_ludo.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.super_ludo.models.Player;
import com.example.super_ludo.models.PlayerColor;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for managing player data.
 */
public class PlayerViewModel extends AndroidViewModel {

    private MutableLiveData<List<Player>> playersLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> playerCountLiveData = new MutableLiveData<>();

    public PlayerViewModel(@NonNull Application application) {
        super(application);

        // Set default values
        playerCountLiveData.setValue(2); // Default to 2 players

        // Initialize with default players
        initializePlayers(2);
    }

    /**
     * Initialize player list with the specified count.
     */
    public void initializePlayers(int count) {
        if (count < 2) count = 2;
        if (count > 4) count = 4;

        List<Player> players = new ArrayList<>();

        // Create players with default names and colors
        for (int i = 0; i < count; i++) {
            Player player = new Player("Player " + (i + 1), PlayerColor.values()[i]);
            players.add(player);
        }

        playersLiveData.setValue(players);
        playerCountLiveData.setValue(count);
    }

    /**
     * Update a player's name.
     */
    public void updatePlayerName(int playerIndex, String newName) {
        List<Player> players = playersLiveData.getValue();
        if (players != null && playerIndex >= 0 && playerIndex < players.size()) {
            players.get(playerIndex).setName(newName);
            playersLiveData.setValue(players);
        }
    }

    /**
     * Get the player count as LiveData.
     */
    public LiveData<Integer> getPlayerCount() {
        return playerCountLiveData;
    }

    /**
     * Set the player count.
     */
    public void setPlayerCount(int count) {
        if (count != playerCountLiveData.getValue()) {
            initializePlayers(count);
        }
    }

    /**
     * Get the players as LiveData.
     */
    public LiveData<List<Player>> getPlayers() {
        return playersLiveData;
    }

    /**
     * Get a specific player.
     */
    public Player getPlayer(int index) {
        List<Player> players = playersLiveData.getValue();
        if (players != null && index >= 0 && index < players.size()) {
            return players.get(index);
        }
        return null;
    }
}
