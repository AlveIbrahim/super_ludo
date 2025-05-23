package com.example.super_ludo.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.super_ludo.R;
import com.example.super_ludo.models.EventType;
import com.example.super_ludo.models.GameEvent;

/**
 * Custom view for displaying game events.
 */
public class GameEventView extends View {

    private Paint backgroundPaint;
    private Paint textPaint;
    private Paint iconPaint;

    private GameEvent event;
    private String title;
    private String description;
    private Drawable iconDrawable;

    public GameEventView(Context context) {
        super(context);
        init();
    }

    public GameEventView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameEventView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Initialize paints
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        textPaint.setTextSize(30f);
        textPaint.setTextAlign(Paint.Align.CENTER);

        iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public void setEvent(GameEvent event) {
        this.event = event;

        if (event != null) {
            // Set title and description from resources
            title = getContext().getString(event.getTitleResourceId());
            description = getContext().getString(event.getDescriptionResourceId());

            // Set icon drawable
            iconDrawable = ContextCompat.getDrawable(getContext(), event.getDrawableResourceId());
        } else {
            title = "";
            description = "";
            iconDrawable = null;
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // Draw background
        canvas.drawRect(0, 0, width, height, backgroundPaint);

        if (event == null) return;

        // Draw title
        textPaint.setTextSize(height * 0.1f);
        canvas.drawText(title, width / 2f, height * 0.15f, textPaint);

        // Draw icon
        if (iconDrawable != null) {
            int iconSize = (int) (height * 0.4f);
            int left = (width - iconSize) / 2;
            int top = (int) (height * 0.2f);

            iconDrawable.setBounds(left, top, left + iconSize, top + iconSize);
            iconDrawable.draw(canvas);

            // For normal roll, draw the number
            if (event.getType() == EventType.NORMAL_ROLL) {
                textPaint.setTextSize(iconSize * 0.5f);
                canvas.drawText(String.valueOf(event.getValue()),
                        width / 2f,
                        top + iconSize / 2f + textPaint.getTextSize() / 3,
                        textPaint);
            }
        }

        // Draw description
        textPaint.setTextSize(height * 0.06f);

        // Split description into lines if needed
        String[] lines = splitTextIntoLines(description, width, textPaint);
        float lineY = height * 0.7f;
        float lineSpacing = textPaint.getTextSize() * 1.2f;

        for (String line : lines) {
            canvas.drawText(line, width / 2f, lineY, textPaint);
            lineY += lineSpacing;
        }
    }

    private String[] splitTextIntoLines(String text, int maxWidth, Paint paint) {
        // A simple text wrapping implementation
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (paint.measureText(line + " " + word) < maxWidth * 0.9f) {
                line.append(word).append(" ");
            } else {
                result.append(line).append("\n");
                line = new StringBuilder(word).append(" ");
            }
        }

        // Add the last line
        result.append(line);

        return result.toString().split("\n");
    }
}
