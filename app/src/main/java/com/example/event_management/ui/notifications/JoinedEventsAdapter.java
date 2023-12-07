package com.example.event_management.ui.notifications;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.event_management.Event;
import com.example.event_management.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JoinedEventsAdapter extends RecyclerView.Adapter<JoinedEventsAdapter.ViewHolder> {
    private List<Event> joinedEvents;

    public JoinedEventsAdapter(List<Event> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.joined_event_item, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.bind(joinedEvents.get(position));
    }

    @Override
    public int getItemCount() {
        return joinedEvents.size();
    }
    private boolean isEventEnded(Event event) {
        String eventDateString = event.getDate();

        if (eventDateString == null || eventDateString.isEmpty()) {
            Log.e("EventAdapter", "Event date is null or empty");

            return false; // Handle the case where event date is not set
        }
        //String dateString = "5/12/2022";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            // Parse the event date string to a Date object
            Date currentDate = new Date();
            Date eventDate = dateFormat.parse(eventDateString);

            // Compare event date with current date
            if (eventDate != null) {
                boolean isEnded = currentDate.after(eventDate);
                Log.d("EventAdapter", "Event ended: " + isEnded);
                return isEnded;
            } else {
                Log.e("EventAdapter", "Failed to parse event date");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("EventAdapter", "Error parsing event date: " + e.getMessage());
            return false; // Handle the case where date parsing fails
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView timeTextView;
        Button btnEventOptions;
        float rating;
        private JoinedEventsAdapter adapter;

        public ViewHolder(View view, JoinedEventsAdapter adapter) {
            super(view);
            titleTextView = view.findViewById(R.id.joinedEventTitle);
            dateTextView = view.findViewById(R.id.joinedEventDate);
            timeTextView = view.findViewById(R.id.joinedEventTime);
            btnEventOptions = view.findViewById(R.id.btnEventOptions);
            this.adapter=adapter;// Initialize the button
            // Set up a click listener for the button
            btnEventOptions.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Event event = adapter.joinedEvents.get(position);
                    updateUIBasedOnEventState(event);
                }
            });
        }

        public void bind(Event event) {
            titleTextView.setText(event.getTitle());
            dateTextView.setText(event.getDate());
            timeTextView.setText(event.getTime());
            rating = event.getRating(); // Set the rating from the event

            float rating = event.getRating();
            // Set a click listener for the options button
            btnEventOptions.setOnClickListener(v -> {
                if (adapter.isEventEnded(event)) {
                    if (rating > 0) {
                        // Handle the case where a rating is already given (e.g., show the rating)
                        showReviewDialog(rating);
                    } else {
                        // Handle the case where the event has ended
                        showOptionsDialog();

                        // Display a message or perform other actions as needed
                    }
                } else {
                    // Handle the regular case (e.g., show the options dialog)
                    Toast.makeText(v.getContext(), "This event has ended", Toast.LENGTH_SHORT).show();

                }
            });
            // Display the rating or "Options" based on the event state
            updateUIBasedOnEventState(event);


        }
        private void updateUIBasedOnEventState(Event event) {
            if (adapter.isEventEnded(event)) {
                // Event has ended
                if (rating > 0) {
                    // Rating is given
                    btnEventOptions.setText("Rating: " + rating);
                } else {
                    // No rating given
                    btnEventOptions.setText("Options");
                }
            } else {
                // Event is ongoing
                btnEventOptions.setText("Coming soon");
            }
        }

        // Method to check if an event has ended


        // Add a method to show the options dialog
        private void showOptionsDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Event Options");

            if (rating > 0) {
                // If a rating is given, show it in the dialog message
                builder.setMessage("Your Rating: " + rating);
                builder.setPositiveButton("Change Rating", (dialog, which) -> showReviewDialog(rating));
            } else {
                builder.setMessage("Did you attend this event?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss(); // Dismiss the current dialog
                    showReviewDialog(rating); // Show the review dialog
                });
            }

            builder.setNegativeButton("No", (dialog, which) -> {
                // Handle No option (dismiss the dialog or any other action)
                dialog.dismiss();
            });
            builder.show();
        }

        // Add a method to show the review dialog
        private void showReviewDialog(float rating) {
            View customView = LayoutInflater.from(itemView.getContext()).inflate(R.layout.dialog_star_rating, null);
            RatingBar ratingBar = customView.findViewById(R.id.ratingBar);
            MaterialTextView textRating = customView.findViewById(R.id.textRating);

            new MaterialAlertDialogBuilder(itemView.getContext())
                    .setTitle("Rate the Event")
                    .setView(customView)
                    .setPositiveButton("Submit", (dialog, which) -> {
                        float newRating = ratingBar.getRating();
                        textRating.setText("Your Rating: " + newRating); // Set the text to show the rating

                        // Handle the submitted rating
                        Toast.makeText(itemView.getContext(), "Rating: " + newRating, Toast.LENGTH_SHORT).show();
                        // Update the event's rating
                        btnEventOptions.setText("Rating: " + newRating);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }
}
