package com.example.event_management.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.event_management.Event;
import com.example.event_management.R;

public class EventDescriptionActivity extends AppCompatActivity implements EventAdapter.OnItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_description);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("EVENT")) {
            Event event = intent.getParcelableExtra("EVENT");

            // Null checks for safety
            if (event != null) {
                String eventTitle = event.getTitle();
                String eventDate = event.getDate();
                String eventDescription = event.getDescription();
                int imageResource = event.getImageResource();

                // Set the title to the event title
                getSupportActionBar().setTitle(eventTitle);

                // Find the TextViews and ImageView in your layout and set their texts or image
                TextView titleTextView = findViewById(R.id.titleTextView);
                TextView dateTextView = findViewById(R.id.dateTextView);
                TextView descriptionTextView = findViewById(R.id.descriptionTextView);
                ImageView eventImageView = findViewById(R.id.eventImageView);

                titleTextView.setText(eventTitle);
                dateTextView.setText(eventDate);
                descriptionTextView.setText(eventDescription);
                eventImageView.setImageResource(imageResource);
            }
        }
    }

    @Override
    public void onItemClick(Event event) {
        // Handle item click here
        Intent intent = new Intent(this, EventDescriptionActivity.class);
        intent.putExtra("EVENT", event);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button click
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
