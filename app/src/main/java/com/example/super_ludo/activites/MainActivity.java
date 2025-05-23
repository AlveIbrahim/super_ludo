package com.example.super_ludo.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.super_ludo.R;
import com.example.super_ludo.dialogs.PlayerSetupDialog;
import com.example.super_ludo.viewmodels.SettingsViewModel;

/**
 * Main entry point for the game.
 */
public class MainActivity extends AppCompatActivity {

    private Button startGameButton;
    private Button howToPlayButton;
    private Button quitButton;

    private SettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ViewModel
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // Initialize UI elements
        initializeUI();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeUI() {
        startGameButton = findViewById(R.id.startGameButton);
        howToPlayButton = findViewById(R.id.howToPlayButton);
        quitButton = findViewById(R.id.quitButton);
    }

    private void setupClickListeners() {
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerSetupDialog();
            }
        });

        howToPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHowToPlayScreen();
            }
        });

        quitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showPlayerSetupDialog() {
        PlayerSetupDialog dialog = new PlayerSetupDialog(this, new PlayerSetupDialog.PlayerSetupListener() {
            @Override
            public void onPlayersReady(int playerCount) {
                startGame(playerCount);
            }
        });

        dialog.show();
    }

    private void startGame(int playerCount) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_PLAYER_COUNT, playerCount);
        startActivity(intent);
    }

    private void openHowToPlayScreen() {
        Intent intent = new Intent(this, HowToPlayActivity.class);
        startActivity(intent);
    }
}