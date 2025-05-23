package com.example.super_ludo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.super_ludo.R;
import com.example.super_ludo.models.PlayerColor;

/**
 * Custom view for displaying a spaceship.
 */
public class SpaceshipView extends View {

    private Paint bodyPaint;
    private Paint windowPaint;
    private Paint enginePaint;
    private Paint glowPaint;

    private int shipColor;
    private PlayerColor playerColor;
    private int shipNumber;
    private boolean reachedHome;

    public SpaceshipView(Context context) {
        super(context);
        init(null);
    }

    public SpaceshipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SpaceshipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        // Initialize default values
        shipColor = ContextCompat.getColor(getContext(), R.color.playerBlue);
        playerColor = PlayerColor.BLUE;
        shipNumber = 1;
        reachedHome = false;

        // Parse custom attributes if available
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SpaceshipView);

            // Get ship color
            int colorIndex = a.getInt(R.styleable.SpaceshipView_shipColor, 0);
            setPlayerColor(PlayerColor.values()[colorIndex]);

            // Get ship number
            shipNumber = a.getInt(R.styleable.SpaceshipView_shipNumber, 1);

            // Get reached home state
            reachedHome = a.getBoolean(R.styleable.SpaceshipView_reachedHome, false);

            a.recycle();
        }

        // Initialize paints
        bodyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bodyPaint.setStyle(Paint.Style.FILL);

        windowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        windowPaint.setStyle(Paint.Style.FILL);

        enginePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        enginePaint.setStyle(Paint.Style.FILL);

        glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        glowPaint.setStyle(Paint.Style.FILL);

        updateColors();
    }

    private void updateColors() {
        switch (playerColor) {
            case BLUE:
                shipColor = ContextCompat.getColor(getContext(), R.color.playerBlue);
                break;
            case GREEN:
                shipColor = ContextCompat.getColor(getContext(), R.color.playerGreen);
                break;
            case RED:
                shipColor = ContextCompat.getColor(getContext(), R.color.playerRed);
                break;
            case YELLOW:
                shipColor = ContextCompat.getColor(getContext(), R.color.playerYellow);
                break;
        }

        bodyPaint.setColor(shipColor);

        // Calculate derived colors
        int windowColor = lightenColor(shipColor);
        windowPaint.setColor(windowColor);

        int engineColor = darkenColor(shipColor);
        enginePaint.setColor(engineColor);

        glowPaint.setColor(windowColor);

        invalidate();
    }

    private int lightenColor(int color) {
        float[] hsv = new float[3];
        android.graphics.Color.colorToHSV(color, hsv);
        hsv[1] *= 0.5f; // Reduce saturation
        hsv[2] = Math.min(1.0f, hsv[2] * 1.5f); // Increase value
        return android.graphics.Color.HSVToColor(hsv);
    }

    private int darkenColor(int color) {
        float[] hsv = new float[3];
        android.graphics.Color.colorToHSV(color, hsv);
        hsv[2] *= 0.5f; // Reduce value
        return android.graphics.Color.HSVToColor(hsv);
    }

    public void setPlayerColor(PlayerColor color) {
        this.playerColor = color;
        updateColors();
    }

    public PlayerColor getPlayerColor() {
        return playerColor;
    }

    public void setShipNumber(int number) {
        this.shipNumber = number;
        invalidate();
    }

    public int getShipNumber() {
        return shipNumber;
    }

    public void setReachedHome(boolean reachedHome) {
        this.reachedHome = reachedHome;
        invalidate();
    }

    public boolean hasReachedHome() {
        return reachedHome;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Draw a spaceship
        drawSpaceship(canvas, width, height);

        // Draw a home indicator if needed
        if (reachedHome) {
            drawHomeIndicator(canvas, width, height);
        }
    }

    private void drawSpaceship(Canvas canvas, int width, int height) {
        // Calculate dimensions
        float shipWidth = width * 0.8f;
        float shipHeight = height * 0.7f;
        float x = width / 2f;
        float y = height / 2f;

        // Ship body (triangle pointing up)
        Path bodyPath = new Path();
        bodyPath.moveTo(x, y - shipHeight/2); // Top point
        bodyPath.lineTo(x - shipWidth/2, y + shipHeight/2); // Bottom left
        bodyPath.lineTo(x + shipWidth/2, y + shipHeight/2); // Bottom right
        bodyPath.close();

        canvas.drawPath(bodyPath, bodyPaint);

        // Windows (circle in the middle)
        float windowRadius = shipWidth * 0.15f;
        canvas.drawCircle(x, y, windowRadius, windowPaint);

        // Engine (rectangle at the bottom)
        float engineWidth = shipWidth * 0.6f;
        float engineHeight = shipHeight * 0.2f;
        canvas.drawRect(
                x - engineWidth/2,
                y + shipHeight/2,
                x + engineWidth/2,
                y + shipHeight/2 + engineHeight,
                enginePaint);

        // Glow effect
        Path glowPath = new Path();
        glowPath.moveTo(x, y - shipHeight/2); // Top point
        glowPath.lineTo(x + shipWidth * 0.1f, y - shipHeight * 0.3f); // Right glow
        glowPath.lineTo(x + shipWidth * 0.3f, y); // Right mid
        glowPath.lineTo(x + shipWidth * 0.1f, y + shipHeight * 0.2f); // Right bottom
        glowPath.close();

        canvas.drawPath(glowPath, glowPaint);

        // Ship number
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        textPaint.setTextSize(shipWidth * 0.3f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        float textY = y + textPaint.getTextSize() * 0.3f; // Adjust for vertical centering
        canvas.drawText(String.valueOf(shipNumber), x, textY, textPaint);
    }

    private void drawHomeIndicator(Canvas canvas, int width, int height) {
        Paint homePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        homePaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        homePaint.setStyle(Paint.Style.STROKE);
        homePaint.setStrokeWidth(width * 0.05f);

        // Draw a circle around the ship
        float radius = Math.min(width, height) / 2f * 0.9f;
        canvas.drawCircle(width / 2f, height / 2f, radius, homePaint);

        // Draw an inner circle
        homePaint.setColor(lightenColor(shipColor));
        homePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2f, height / 2f, radius * 0.8f, homePaint);
    }
}
