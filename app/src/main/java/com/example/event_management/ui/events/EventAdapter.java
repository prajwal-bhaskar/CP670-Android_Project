package com.example.event_management.ui.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.event_management.ui.events.EventViewModel;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.R;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    public  List<Event> events;
    private EventViewModel eventViewModel;
    public  OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public EventAdapter(List<Event> events, OnItemClickListener onItemClickListener) {
        this.events = events;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull

    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        holder.eventTitleTextView.setText(event.getTitle());
        holder.eventDateTextView.setText(event.getDate());
        holder.eventDescriptionTextView.setText(event.getDescription());
        holder.eventImageView.setImageResource(event.getImageResource());
        // Check if the event is joined or favorited and update UI accordingly
        if (event.isJoined()) {
            // Event is joined, update UI accordingly
            holder.joinButton.setText("Leave");
        } else {
            // Event is not joined, update UI accordingly
            holder.joinButton.setText("Join");
        }

        // Check if the event is favorited or not and update UI accordingly
        if (event.isFavorited()) {
            // Event is favorited, update UI accordingly
            holder.favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fav_light, 0, 0, 0);
        } else {
            // Event is not favorited, update UI accordingly
            holder.favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fav_dark, 0, 0, 0);
        }

        holder.joinButton.setOnClickListener(v -> {
            // Toggle the join state and update UI
            event.setJoined(!event.isJoined());
            notifyItemChanged(position);
            if (eventViewModel != null) {
                List<Event> updatedEvents = new ArrayList<>(events);
                updatedEvents.set(position, event);
                eventViewModel.setEvents(updatedEvents);
            }
        });

        holder.favoriteButton.setOnClickListener(v -> {
            // Toggle the favorite state and update UI
            event.setFavorited(!event.isFavorited());
            notifyItemChanged(position);
            if (eventViewModel != null) {
                List<Event> updatedEvents = new ArrayList<>(events);
                updatedEvents.set(position, event);
                eventViewModel.setEvents(updatedEvents);
            }

            // Optionally, add the event to the favorites tab
            if (event.isFavorited()) {
                // Add to favorites logic
            } else {
                // Remove from favorites logic
            }
        });

    }
    private void updateEventInViewModel(Event event) {
        List<Event> updatedEvents = new ArrayList<>(events);
        int index = updatedEvents.indexOf(event);
        if (index != -1) {
            updatedEvents.set(index, event);
            if (eventViewModel != null) {
                eventViewModel.setEvents(updatedEvents);
            }

        }
    }


    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitleTextView;
        public TextView eventDateTextView;
        public TextView eventDescriptionTextView;

        private final ImageView eventImageView;
        public Button joinButton;
        public Button favoriteButton;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventTitleTextView = itemView.findViewById(R.id.titleTextView);
            eventDateTextView = itemView.findViewById(R.id.dateTextView);
            eventDescriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            eventImageView = itemView.findViewById(R.id.eventImage);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(events.get(position));
                }
            });
            joinButton = itemView.findViewById(R.id.joinButton);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

        }




    }


}

