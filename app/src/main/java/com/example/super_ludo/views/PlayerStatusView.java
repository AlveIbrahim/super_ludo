package com.example.super_ludo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.super_ludo.R;
import com.example.super_ludo.models.Player;
import com.example.super_ludo.models.PlayerColor;
import com.example.super_ludo.models.Spaceship;

import java.util.List;

/**
 * Custom view for displaying player status.
 */
public class PlayerStatusView extends View {

    private Paint backgroundPaint;
    private Paint namePaint;
    private Paint statusPaint;
    private Paint activeIndicatorPaint;

    private Player player;
    private boolean isActive;
    private String statusText;

    public PlayerStatusView(Context context) {
        super(context);
        init();
    }

    public PlayerStatusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerStatusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize paints
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        namePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        namePaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        namePaint.setTextSize(30f);

        statusPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        statusPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        statusPaint.setTextSize(24f);

        activeIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        activeIndicatorPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        // Default values
        isActive = false;
        statusText = getContext().getString(R.string.waiting);
    }

    public void setPlayer(Player player) {
        this.player = player;
        invalidate();
    }

    public void setActive(boolean active) {
        this.isActive = active;
        invalidate();
    }

    public void setStatusText(String status) {
        this.statusText = status;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        float padding = height * 0.1f;

        // Draw background with rounded corners
        RectF backgroundRect = new RectF(0, 0, width, height);
        canvas.drawRoundRect(backgroundRect, height * 0.2f, height * 0.2f, backgroundPaint);

        // Draw active indicator
        if (isActive) {
            RectF indicatorRect = new RectF(
                    padding / 2,
                    padding / 2,
                    width - padding / 2,
                    height - padding / 2);
            activeIndicatorPaint.setStyle(Paint.Style.STROKE);
            activeIndicatorPaint.setStrokeWidth(padding / 2);
            canvas.drawRoundRect(indicatorRect, height * 0.15f, height * 0.15f, activeIndicatorPaint);
        }

        if (player == null) return;

        // Draw player name
        namePaint.setTextSize(height * 0.25f);
        float nameX = padding * 2;
        float nameY = height * 0.4f;
        canvas.drawText(player.getName(), nameX, nameY, namePaint);

        // Draw player ships
        List<Spaceship> ships = player.getSpaceships();
        float shipSize = height * 0.3f;
        float shipY = height * 0.7f;
        float shipStartX = nameX;

        for (int i = 0; i < ships.size(); i++) {
            Spaceship ship = ships.get(i);
            float shipX = shipStartX + i * (shipSize * 1.2f);

            // Get proper ship drawable based on color
            PlayerColor color = player.getColor();
            int drawableId;

            switch (color) {
                case BLUE:
                    drawableId = R.drawable.spaceship_blue;
                    break;
                case GREEN:
                    drawableId = R.drawable.spaceship_green;
                    break;
                case RED:
                    drawableId = R.drawable.spaceship_red;
                    break;
                case YELLOW:
                    drawableId = R.drawable.spaceship_yellow;
                    break;
                default:
                    drawableId = R.drawable.spaceship_blue;
                    break;
            }

            // Draw the ship
            Drawable shipDrawable = ContextCompat.getDrawable(getContext(), drawableId);
            if (shipDrawable != null) {
                int left = (int) (shipX - shipSize / 2);
                int top = (int) (shipY - shipSize / 2);
                int right = (int) (shipX + shipSize / 2);
                int bottom = (int) (shipY + shipSize / 2);

                shipDrawable.setBounds(left, top, right, bottom);
                shipDrawable.draw(canvas);

                // If ship has reached home, draw indicator
                if (ship.hasReachedHome()) {
                    Paint homePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    homePaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    homePaint.setStyle(Paint.Style.STROKE);
                    homePaint.setStrokeWidth(3f);

                    canvas.drawCircle(shipX, shipY, shipSize * 0.6f, homePaint);
                }
            }
        }

        // Draw status
        statusPaint.setTextSize(height * 0.2f);
        statusPaint.setTextAlign(Paint.Align.RIGHT);
        float statusX = width - padding * 2;
        float statusY = height * 0.6f;
        canvas.drawText(statusText, statusX, statusY, statusPaint);
    }

    // Helper method to load drawable
    private Drawable getShipDrawable(Context context, PlayerColor color) {
        int drawableId;

        switch (color) {
            case BLUE:
                drawableId = R.drawable.spaceship_blue;
                break;
            case GREEN:
                drawableId = R.drawable.spaceship_green;
                break;
            case RED:
                drawableId = R.drawable.spaceship_red;
                break;
            case YELLOW:
                drawableId = R.drawable.spaceship_yellow;
                break;
            default:
                drawableId = R.drawable.spaceship_blue;
                break;
        }

        return ContextCompat.getDrawable(context, drawableId);
    }
}