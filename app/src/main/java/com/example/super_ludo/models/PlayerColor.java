package com.example.super_ludo.models;

import android.graphics.Color;

/**
 * Represents the available player colors.
 */
public enum PlayerColor {
    BLUE(Color.parseColor("#0066CC")),
    GREEN(Color.parseColor("#00CC66")),
    RED(Color.parseColor("#CC0000")),
    YELLOW(Color.parseColor("#FFCC00"));

    private final int colorValue;

    PlayerColor(int colorValue) {
        this.colorValue = colorValue;
    }

    public int getColorValue() {
        return colorValue;
    }

    public int getDrawableResourceId() {
        switch (this) {
            case BLUE:
                return com.giiker.superludo.R.drawable.spaceship_blue;
            case GREEN:
                return com.giiker.superludo.R.drawable.spaceship_green;
            case RED:
                return com.giiker.superludo.R.drawable.spaceship_red;
            case YELLOW:
                return com.giiker.superludo.R.drawable.spaceship_yellow;
            default:
                return com.giiker.superludo.R.drawable.spaceship_blue;
        }
    }

    public int getCellDrawableResourceId() {
        switch (this) {
            case BLUE:
                return com.giiker.superludo.R.drawable.board_cell_blue;
            case GREEN:
                return com.giiker.superludo.R.drawable.board_cell_green;
            case RED:
                return com.giiker.superludo.R.drawable.board_cell_red;
            case YELLOW:
                return com.giiker.superludo.R.drawable.board_cell_yellow;
            default:
                return com.giiker.superludo.R.drawable.board_cell_blue;
        }
    }
}
