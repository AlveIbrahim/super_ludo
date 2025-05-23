package com.example.super_ludo.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import com.example.super_ludo.dialogs.GameEventDialog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.super_ludo.R;
import com.example.super_ludo.models.BoardCell;
import com.example.super_ludo.models.GameEvent;
import com.example.super_ludo.models.GameState;
import com.example.super_ludo.models.Player;
import com.example.super_ludo.models.Spaceship;
import com.example.super_ludo.utils.GameAnimationUtils;
import com.example.super_ludo.utils.SoundManager;
import com.example.super_ludo.viewmodels.GameViewModel;
import com.example.super_ludo.viewmodels.SettingsViewModel;
import com.example.super_ludo.views.BoardView;
import com.example.super_ludo.views.CrystalBallView;
import com.example.super_ludo.views.PlayerStatusView;

/**
 * Activity for the main game screen.
 */
public class GameActivity extends AppCompatActivity implements
        BoardView.BoardCellClickListener,
        CrystalBallView.CrystalBallRollListener {

    public static final String EXTRA_PLAYER_COUNT = "player_count";

    private GameViewModel gameViewModel;
    private SettingsViewModel settingsViewModel;

    // UI elements
    private BoardView boardView;
    private CrystalBallView crystalBallView;
    private TextView turnIndicatorTextView;
    private PlayerStatusView topPlayerPanel;
    private PlayerStatusView bottomPlayerPanel;
    private Button resetButton;
    private Button quitGameButton;

    // Utilities
    private SoundManager soundManager;
    private GameAnimationUtils gameAnimationUtils;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Initialize ViewModels
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);

        // Initialize utilities
        soundManager = new SoundManager(this);
        gameAnimationUtils = new GameAnimationUtils(this);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Initialize UI elements
        initializeUI();

        // Set up click listeners
        setupClickListeners();

        // Observe ViewModel changes
        observeViewModelChanges();

        // Start a new game
        int playerCount = getIntent().getIntExtra(EXTRA_PLAYER_COUNT, 2);
        gameViewModel.initializeGame(playerCount);
    }

    private void initializeUI() {
        boardView = findViewById(R.id.boardView);
        crystalBallView = findViewById(R.id.crystalBallView);
        turnIndicatorTextView = findViewById(R.id.turnIndicatorTextView);
        topPlayerPanel = findViewById(R.id.topPlayerPanel);
        bottomPlayerPanel = findViewById(R.id.bottomPlayerPanel);
        resetButton = findViewById(R.id.resetButton);
        quitGameButton = findViewById(R.id.quitGameButton);

        // Set listeners
        boardView.setBoardCellClickListener(this);
        crystalBallView.setRollListener(this);
    }

    private void setupClickListeners() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmReset();
            }
        });

        quitGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmQuit();
            }
        });
    }

    private void observeViewModelChanges() {
        // Observe game state changes
        gameViewModel.getGameState().observe(this, new Observer<GameState>() {
            @Override
            public void onChanged(GameState gameState) {
                updateGameUI(gameState);
            }
        });

        // Observe current event changes
        gameViewModel.getCurrentEvent().observe(this, new Observer<GameEvent>() {
            @Override
            public void onChanged(GameEvent event) {
                if (event != null) {
                    showGameEvent(event);
                }
            }
        });

        // Observe status message changes
        gameViewModel.getStatusMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                turnIndicatorTextView.setText(message);
            }
        });

        // Observe highlighted cell changes
        gameViewModel.getHighlightedCell().observe(this, new Observer<BoardCell>() {
            @Override
            public void onChanged(BoardCell cell) {
                boardView.setSelectedCell(cell);
            }
        });

        // Observe settings changes
        settingsViewModel.isSoundEnabled().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean enabled) {
                soundManager.setSoundEnabled(enabled);
            }
        });

        settingsViewModel.isVibrationEnabled().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean enabled) {
                // No action needed here, we'll check this when vibrating
            }
        });

        settingsViewModel.getAnimationSpeed().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer speed) {
                gameAnimationUtils.setAnimationSpeed(speed);
            }
        });
    }

    private void updateGameUI(GameState gameState) {
        if (gameState == null) return;

        // Update the board view
        boardView.setGameBoard(gameState.getBoard());

        // Update player panels
        List<Player> players = gameState.getPlayers();
        if (players.size() >= 2) {
            Player bottomPlayer = players.get(0); // First player at bottom
            bottomPlayerPanel.setPlayer(bottomPlayer);
            bottomPlayerPanel.setActive(bottomPlayer.isActive());

            Player topPlayer = players.get(1); // Second player at top
            topPlayerPanel.setPlayer(topPlayer);
            topPlayerPanel.setActive(topPlayer.isActive());
        }

        // Update turn indicator
        String playerName = gameState.getCurrentPlayer().getName();
        turnIndicatorTextView.setText(playerName + "'s Turn");

        // Check if game is over
        if (gameState.isGameOver()) {
            showGameOverDialog(gameState.getWinner());
        }
    }

    private void showGameEvent(GameEvent event) {
        // Show event dialog
        GameEventDialog dialog = new GameEventDialog(this, event);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Apply event effects after dialog is dismissed
                gameViewModel.processRoll(event);
            }
        });

        dialog.show();

        // Play sound effect for event
        playEventSound(event);

        // Vibrate for event
        if (settingsViewModel.isVibrationEnabled().getValue() == Boolean.TRUE) {
            vibrator.vibrate(100);
        }
    }

    private void playEventSound(GameEvent event) {
        if (settingsViewModel.isSoundEnabled().getValue() != Boolean.TRUE) {
            return;
        }

        switch (event.getType()) {
            case NORMAL_ROLL:
                soundManager.playSound(SoundManager.SOUND_DICE);
                break;

            case BLACK_HOLE:
                soundManager.playSound(SoundManager.SOUND_BLACK_HOLE);
                break;

            case METEOR_STRIKE:
                soundManager.playSound(SoundManager.SOUND_METEOR);
                break;

            case WORMHOLE:
                soundManager.playSound(SoundManager.SOUND_WORMHOLE);
                break;

            case SUPER_BOOST:
                soundManager.playSound(SoundManager.SOUND_BOOST);
                break;

            case ALIEN_INVASION:
                soundManager.playSound(SoundManager.SOUND_ALIEN);
                break;
        }
    }

    private void showGameOverDialog(Player winner) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over!");
        builder.setMessage(winner.getName() + " has won the game!");
        builder.setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameViewModel.resetGame();
            }
        });
        builder.setNegativeButton("Main Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();

        // Play victory sound
        if (settingsViewModel.isSoundEnabled().getValue() == Boolean.TRUE) {
            soundManager.playSound(SoundManager.SOUND_VICTORY);
        }
    }

    private void confirmReset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Game");
        builder.setMessage("Are you sure you want to reset the game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameViewModel.resetGame();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void confirmQuit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit Game");
        builder.setMessage("Are you sure you want to quit the game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    public void onCellClick(BoardCell cell) {
        // Check if waiting for ship selection
        if (gameViewModel.isWaitingForShipSelection().getValue() == Boolean.TRUE ||
                gameViewModel.isWaitingForTargetSelection().getValue() == Boolean.TRUE) {

            // Check if cell has any spaceships
            List<Spaceship> ships = cell.getSpaceships();
            if (!ships.isEmpty()) {
                // For simplicity, select the first ship in the cell
                gameViewModel.selectSpaceship(ships.get(0));

                // Play sound
                if (settingsViewModel.isSoundEnabled().getValue() == Boolean.TRUE) {
                    soundManager.playSound(SoundManager.SOUND_SELECT);
                }
            }
        }
    }

    @Override
    public void onRollComplete(GameEvent event) {
        // Play roll sound
        if (settingsViewModel.isSoundEnabled().getValue() == Boolean.TRUE) {
            soundManager.playSound(SoundManager.SOUND_ROLL);
        }

        // Vibrate
        if (settingsViewModel.isVibrationEnabled().getValue() == Boolean.TRUE) {
            vibrator.vibrate(200);
        }

        // Show event dialog after roll animation
        showGameEvent(event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause sound
        soundManager.pauseAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume sound
        soundManager.resumeAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release resources
        soundManager.release();
    }
}
