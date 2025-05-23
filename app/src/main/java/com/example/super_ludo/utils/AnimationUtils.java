package com.example.super_ludo.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.super_ludo.R;

/**
 * Utility class for managing animations.
 */
public class AnimationUtils {

    private Context context;
    private int animationSpeed = 100; // Default 100% speed

    public AnimationUtils(Context context) {
        this.context = context;
    }

    /**
     * Apply rotation animation to a view.
     */
    public void applyRotationAnimation(View view) {
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.rotate_crystal_ball);

        // Adjust speed
        adjustAnimationSpeed(animation);

        view.startAnimation(animation);
    }

    /**
     * Apply ship movement animation to a view.
     */
    public void applyShipMovementAnimation(View view, float fromX, float fromY, float toX, float toY) {
        // Create a custom animation for specific coordinates
        android.view.animation.TranslateAnimation animation = new android.view.animation.TranslateAnimation(
                fromX, toX, fromY, toY);

        animation.setDuration(500); // 500ms duration

        // Adjust speed
        adjustAnimationSpeed(animation);

        view.startAnimation(animation);
    }

    /**
     * Apply ship return to base animation.
     */
    public void applyReturnToBaseAnimation(View view) {
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.ship_return_to_base);

        // Adjust speed
        adjustAnimationSpeed(animation);

        view.startAnimation(animation);
    }

    /**
     * Apply popup animation for event dialogs.
     */
    public void applyPopupAnimation(View view) {
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.event_popup);

        // Adjust speed
        adjustAnimationSpeed(animation);

        view.startAnimation(animation);
    }

    /**
     * Adjust animation speed based on settings.
     */
    private void adjustAnimationSpeed(Animation animation) {
        if (animationSpeed != 100) {
            float speedFactor = animationSpeed / 100f;
            animation.setDuration((long) (animation.getDuration() / speedFactor));
        }
    }

    /**
     * Set the animation speed (percentage).
     */
    public void setAnimationSpeed(int speed) {
        this.animationSpeed = speed;
    }

    /**
     * Get the current animation speed.
     */
    public int getAnimationSpeed() {
        return animationSpeed;
    }
}