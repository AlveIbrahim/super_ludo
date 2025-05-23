package com.example.super_ludo.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.super_ludo.R;
import com.example.super_ludo.models.EventType;
import com.example.super_ludo.models.GameEvent;

import java.util.Random;

/**
 * Custom view for the Crystal Ball that controls game events.
 */
public class CrystalBallView extends View {

    private Paint ballPaint;
    private Paint glowPaint;
    private Paint highlightPaint;
    private Paint eventPaint;

    private int ballColor;
    private int glowColor;
    private int highlightColor;

    private float centerX;
    private float centerY;
    private float radius;

    private boolean isAnimating = false;
    private ValueAnimator rotationAnimator;
    private ValueAnimator pulseAnimator;

    private GameEvent currentEvent;
    private CrystalBallRollListener rollListener;

    private Random random = new Random();

    public CrystalBallView(Context context) {
        super(context);
        init();
    }

    public CrystalBallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CrystalBallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize colors
        ballColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        glowColor = ContextCompat.getColor(getContext(), R.color.colorAccent);
        highlightColor = ContextCompat.getColor(getContext(), android.R.color.white);

        // Initialize paints
        ballPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ballPaint.setStyle(Paint.Style.FILL);

        glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        glowPaint.setStyle(Paint.Style.FILL);

        highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint.setStyle(Paint.Style.FILL);

        eventPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        eventPaint.setStyle(Paint.Style.FILL);

        // Set up animation
        setupAnimations();
    }

    private void setupAnimations() {
        // Rotation animation
        rotationAnimator = ValueAnimator.ofFloat(0f, 360f);
        rotationAnimator.setDuration(1000);
        rotationAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotationAnimator.addUpdateListener(animation -> {
            invalidate();
        });

        // Pulse animation
        pulseAnimator = ValueAnimator.ofFloat(0.8f, 1.2f);
        pulseAnimator.setDuration(500);
        pulseAnimator.setRepeatCount(1);
        pulseAnimator.setRepeatMode(ValueAnimator.REVERSE);
        pulseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        pulseAnimator.addUpdateListener(animation -> {
            invalidate();
        });

        pulseAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                if (rollListener != null) {
                    rollListener.onRollComplete(currentEvent);
                }
            }
        });
    }

    public void setRollListener(CrystalBallRollListener listener) {
        this.rollListener = listener;
    }

    public void setCurrentEvent(GameEvent event) {
        this.currentEvent = event;
        invalidate();
    }

    public boolean isAnimating() {
        return isAnimating;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w, h) / 2f - 10; // Padding of 10px

        // Update gradients
        updateGradients();
    }

    private void updateGradients() {
        // Ball gradient
        RadialGradient ballGradient = new RadialGradient(
                centerX - radius * 0.3f,
                centerY - radius * 0.3f,
                radius,
                new int[] {
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark),
                        ballColor
                },
                null,
                Shader.TileMode.CLAMP
        );
        ballPaint.setShader(ballGradient);

        // Glow gradient
        RadialGradient glowGradient = new RadialGradient(
                centerX,
                centerY,
                radius * 1.2f,
                new int[] {
                        glowColor,
                        ContextCompat.getColor(getContext(), android.R.color.transparent)
                },
                new float[] { 0.7f, 1.0f },
                Shader.TileMode.CLAMP
        );
        glowPaint.setShader(glowGradient);

        // Highlight gradient
        RadialGradient highlightGradient = new RadialGradient(
                centerX - radius * 0.5f,
                centerY - radius * 0.5f,
                radius * 0.4f,
                new int[] {
                        highlightColor,
                        ContextCompat.getColor(getContext(), android.R.color.transparent)
                },
                new float[] { 0.0f, 1.0f },
                Shader.TileMode.CLAMP
        );
        highlightPaint.setShader(highlightGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Apply scale if animating
        if (isAnimating && pulseAnimator.isRunning()) {
            float scale = (Float) pulseAnimator.getAnimatedValue();
            canvas.save();
            canvas.scale(scale, scale, centerX, centerY);
        }

        // Draw outer glow
        canvas.drawCircle(centerX, centerY, radius * 1.1f, glowPaint);

        // Draw the ball
        canvas.drawCircle(centerX, centerY, radius, ballPaint);

        // Draw highlight
        canvas.drawCircle(centerX - radius * 0.3f, centerY - radius * 0.3f, radius * 0.4f, highlightPaint);

        // Draw event indicator if not animating
        if (!isAnimating && currentEvent != null) {
            drawEventIndicator(canvas);
        }

        // Restore canvas if scaled
        if (isAnimating && pulseAnimator.isRunning()) {
            canvas.restore();
        }
    }

    private void drawEventIndicator(Canvas canvas) {
        if (currentEvent == null) return;

        switch (currentEvent.getType()) {
            case NORMAL_ROLL:
                // Draw dice number
                int value = currentEvent.getValue();
                drawDiceNumber(canvas, value);
                break;

            case BLACK_HOLE:
                drawBlackHole(canvas);
                break;

            case METEOR_STRIKE:
                drawMeteor(canvas);
                break;

            case WORMHOLE:
                drawWormhole(canvas);
                break;

            case SUPER_BOOST:
                drawSuperBoost(canvas);
                break;

            case ALIEN_INVASION:
                drawAlienInvasion(canvas);
                break;
        }
    }

    private void drawDiceNumber(Canvas canvas, int number) {
        // Draw a small white circle in the center
        Paint numberBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numberBackgroundPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        canvas.drawCircle(centerX, centerY, radius * 0.3f, numberBackgroundPaint);

        // Draw the number
        Paint numberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        numberPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.black));
        numberPaint.setTextSize(radius * 0.5f);
        numberPaint.setTextAlign(Paint.Align.CENTER);

        // Draw text centered vertically
        float textHeight = numberPaint.descent() - numberPaint.ascent();
        float textOffset = (textHeight / 2) - numberPaint.descent();

        canvas.drawText(String.valueOf(number), centerX, centerY + textOffset, numberPaint);
    }

    private void drawBlackHole(Canvas canvas) {
        // Draw a black hole effect
        Paint blackHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        RadialGradient blackHoleGradient = new RadialGradient(
                centerX,
                centerY,
                radius * 0.4f,
                new int[] {
                        ContextCompat.getColor(getContext(), android.R.color.black),
                        ContextCompat.getColor(getContext(), R.color.colorPrimaryDark)
                },
                null,
                Shader.TileMode.CLAMP
        );
        blackHolePaint.setShader(blackHoleGradient);

        canvas.drawCircle(centerX, centerY, radius * 0.4f, blackHolePaint);
    }

    private void drawMeteor(Canvas canvas) {
        // Draw a meteor effect
        Paint meteorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        meteorPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerRed));

        canvas.save();
        canvas.rotate(45, centerX, centerY);
        canvas.drawOval(centerX - radius * 0.4f, centerY - radius * 0.2f,
                centerX + radius * 0.4f, centerY + radius * 0.2f, meteorPaint);
        canvas.restore();

        // Add a glow effect
        Paint meteorGlowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        meteorGlowPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        meteorGlowPaint.setStyle(Paint.Style.STROKE);
        meteorGlowPaint.setStrokeWidth(3f);

        canvas.save();
        canvas.rotate(45, centerX, centerY);
        canvas.drawOval(centerX - radius * 0.4f, centerY - radius * 0.2f,
                centerX + radius * 0.4f, centerY + radius * 0.2f, meteorGlowPaint);
        canvas.restore();
    }

    private void drawWormhole(Canvas canvas) {
        // Draw a wormhole effect (spiral)
        Paint wormholePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wormholePaint.setColor(ContextCompat.getColor(getContext(), R.color.playerYellow));
        wormholePaint.setStyle(Paint.Style.STROKE);
        wormholePaint.setStrokeWidth(4f);

        // Draw spiral
        float maxRadius = radius * 0.4f;
        float minRadius = radius * 0.1f;
        float radiusStep = (maxRadius - minRadius) / 720f;

        float currentRadius = minRadius;
        float centerX = this.centerX;
        float centerY = this.centerY;

        for (int angle = 0; angle < 720; angle += 5) {
            float x = (float) (centerX + currentRadius * Math.cos(Math.toRadians(angle)));
            float y = (float) (centerY + currentRadius * Math.sin(Math.toRadians(angle)));

            if (angle == 0) {
                canvas.drawCircle(x, y, 2f, wormholePaint);
            } else {
                canvas.drawCircle(x, y, 2f, wormholePaint);
            }

            currentRadius += radiusStep;
        }
    }

    private void drawSuperBoost(Canvas canvas) {
        // Draw a boost effect (starburst)
        Paint boostPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        boostPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));

        int numRays = 8;
        float innerRadius = radius * 0.1f;
        float outerRadius = radius * 0.4f;

        for (int i = 0; i < numRays; i++) {
            float angle = (float) (i * 2 * Math.PI / numRays);
            float startX = (float) (centerX + innerRadius * Math.cos(angle));
            float startY = (float) (centerY + innerRadius * Math.sin(angle));
            float endX = (float) (centerX + outerRadius * Math.cos(angle));
            float endY = (float) (centerY + outerRadius * Math.sin(angle));

            canvas.drawLine(startX, startY, endX, endY, boostPaint);
        }

        // Draw a center circle
        boostPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerYellow));
        canvas.drawCircle(centerX, centerY, innerRadius, boostPaint);
    }

    private void drawAlienInvasion(Canvas canvas) {
        // Draw an alien head
        Paint alienPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        alienPaint.setColor(ContextCompat.getColor(getContext(), R.color.playerGreen));

        // Alien head
        canvas.drawOval(centerX - radius * 0.3f, centerY - radius * 0.4f,
                centerX + radius * 0.3f, centerY + radius * 0.2f, alienPaint);

        // Alien eyes
        Paint eyePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        eyePaint.setColor(ContextCompat.getColor(getContext(), android.R.color.black));

        canvas.drawOval(centerX - radius * 0.2f, centerY - radius * 0.25f,
                centerX - radius * 0.05f, centerY - radius * 0.1f, eyePaint);
        canvas.drawOval(centerX + radius * 0.05f, centerY - radius * 0.25f,
                centerX + radius * 0.2f, centerY - radius * 0.1f, eyePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();

            // Check if touch was within the ball
            float distance = (float) Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
            if (distance <= radius && !isAnimating) {
                rollCrystalBall();
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    public void rollCrystalBall() {
        if (isAnimating) return;

        isAnimating = true;

        // Generate a random event
        currentEvent = generateRandomEvent();

        // Start animations
        rotationAnimator.start();
        pulseAnimator.start();
    }

    private GameEvent generateRandomEvent() {
        double chance = random.nextDouble();

        if (chance < 0.65) {
            // Normal roll (1-6)
            int value = random.nextInt(6) + 1;
            return new GameEvent(EventType.NORMAL_ROLL, value);
        } else if (chance < 0.75) {
            // Black Hole
            return new GameEvent(EventType.BLACK_HOLE);
        } else if (chance < 0.825) {
            // Meteor Strike
            return new GameEvent(EventType.METEOR_STRIKE);
        } else if (chance < 0.9) {
            // Wormhole
            return new GameEvent(EventType.WORMHOLE);
        } else if (chance < 0.95) {
            // Super Boost
            return new GameEvent(EventType.SUPER_BOOST);
        } else {
            // Alien Invasion
            return new GameEvent(EventType.ALIEN_INVASION);
        }
    }

    /**
     * Interface for crystal ball roll callbacks.
     */
    public interface CrystalBallRollListener {
        void onRollComplete(GameEvent event);
    }
}
