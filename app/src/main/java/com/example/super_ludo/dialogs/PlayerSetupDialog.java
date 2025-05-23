package com.example.super_ludo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.super_ludo.R;

/**
 * Dialog for setting up players before starting a game.
 */
public class PlayerSetupDialog extends Dialog {

    private NumberPicker playerCountPicker;
    private EditText player1NameEditText;
    private EditText player2NameEditText;
    private EditText player3NameEditText;
    private EditText player4NameEditText;
    private Button startButton;
    private Button cancelButton;

    private PlayerSetupListener listener;

    public PlayerSetupDialog(@NonNull Context context, PlayerSetupListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_player_setup);

        // Initialize UI elements
        playerCountPicker = findViewById(R.id.playerCountPicker);
        player1NameEditText = findViewById(R.id.player1NameEditText);
        player2NameEditText = findViewById(R.id.player2NameEditText);
        player3NameEditText = findViewById(R.id.player3NameEditText);
        player4NameEditText = findViewById(R.id.player4NameEditText);
        startButton = findViewById(R.id.startButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Setup player count picker
        playerCountPicker.setMinValue(2);
        playerCountPicker.setMaxValue(4);
        playerCountPicker.setValue(2);

        // Update player name visibility based on player count
        playerCountPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updatePlayerNameVisibility(newVal);
            }
        });

        // Set initial visibility
        updatePlayerNameVisibility(2);

        // Set click listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void updatePlayerNameVisibility(int playerCount) {
        player1NameEditText.setVisibility(View.VISIBLE);
        player2NameEditText.setVisibility(View.VISIBLE);

        // Show/hide player 3 and 4 based on count
        player3NameEditText.setVisibility(playerCount >= 3 ? View.VISIBLE : View.GONE);
        player4NameEditText.setVisibility(playerCount >= 4 ? View.VISIBLE : View.GONE);

        // Also update the labels
        TextView player3Label = findViewById(R.id.player3Label);
        TextView player4Label = findViewById(R.id.player4Label);

        player3Label.setVisibility(playerCount >= 3 ? View.VISIBLE : View.GONE);
        player4Label.setVisibility(playerCount >= 4 ? View.VISIBLE : View.GONE);
    }

    private void startGame() {
        // Get player count
        int playerCount = playerCountPicker.getValue();

        // Get player names
        String player1Name = player1NameEditText.getText().toString();
        String player2Name = player2NameEditText.getText().toString();
        String player3Name = player3NameEditText.getText().toString();
        String player4Name = player4NameEditText.getText().toString();

        // Use default names if empty
        if (TextUtils.isEmpty(player1Name)) player1Name = "Player 1";
        if (TextUtils.isEmpty(player2Name)) player2Name = "Player 2";
        if (TextUtils.isEmpty(player3Name)) player3Name = "Player 3";
        if (TextUtils.isEmpty(player4Name)) player4Name = "Player 4";

        // TODO: Store player names for use in the game
        // Currently, we're just passing the count back to the activity

        // Notify listener
        if (listener != null) {
            listener.onPlayersReady(playerCount);
        }

        dismiss();
    }

    /**
     * Interface for player setup callbacks.
     */
    public interface PlayerSetupListener {
        void onPlayersReady(int playerCount);
    }
}