package com.example.super_ludo.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.super_ludo.R;
import com.example.super_ludo.models.EventType;
import com.example.super_ludo.models.GameEvent;

/**
 * Dialog for displaying game events.
 */
public class GameEventDialog extends Dialog {

    private GameEvent event;

    private TextView eventTitleTextView;
    private ImageView eventIconImageView;
    private TextView eventDescriptionTextView;
    private Button okButton;

    public GameEventDialog(@NonNull Context context, GameEvent event) {
        super(context);
        this.event = event;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_event);

        // Initialize UI elements
        eventTitleTextView = findViewById(R.id.eventTitleTextView);
        eventIconImageView = findViewById(R.id.eventIconImageView);
        eventDescriptionTextView = findViewById(R.id.eventDescriptionTextView);
        okButton = findViewById(R.id.okButton);

        // Set event data
        if (event != null) {
            // Set title
            eventTitleTextView.setText(getContext().getString(event.getTitleResourceId()));

            // Set icon
            eventIconImageView.setImageResource(event.getDrawableResourceId());

            // Set description
            String description = getContext().getString(event.getDescriptionResourceId());

            // For normal roll, include the value
            if (event.getType() == EventType.NORMAL_ROLL) {
                description = "You rolled a " + event.getValue() + ". " + description;
            }

            eventDescriptionTextView.setText(description);
        }

        // Set click listener for OK button
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}