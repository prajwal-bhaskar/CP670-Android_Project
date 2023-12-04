package com.example.event_management.ui.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.event_management.Event;
import com.example.event_management.R;
import com.example.event_management.ui.favourites.FavoriteAdapter;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private static List<Event> events;
    private List<Event> favoriteEvents;

    private final EventViewModel eventViewModel;
    private static OnItemClickListener onItemClickListener;
    private final FavoriteAdapter favoriteAdapter;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public EventAdapter(List<Event> events, EventViewModel eventViewModel, OnItemClickListener onItemClickListener, FavoriteAdapter favoriteAdapter) {
            this.events = new ArrayList<>();
        this.favoriteEvents = favoriteEvents!= null ? favoriteEvents : new ArrayList<>(); // Add this line
        this.eventViewModel = eventViewModel;
        this.onItemClickListener = onItemClickListener;
        this.favoriteAdapter = favoriteAdapter;
    }
    @NonNull
    @Override
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
        holder.eventTimeTextView.setText(event.getTime());
        holder.eventMaxAttendeesTextView.setText(String.valueOf(event.getMaxAttendees()));
        if (event.getImageUrl() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(event.getImageUrl())
                    .into(holder.eventImageView);
        }

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
           // if (onItemClickListener != null) {
             //   onItemClickListener.onItemClick(event);
            //updateEventInViewModel(event);
            //}

            // Optionally, add the event to the favorites tab
            if (event.isFavorited()) {
                addToFavorites(position,event);
            } else {
                removeFromFavorites(position,event);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(event);
            }
        });

        }

    private void removeFromFavorites(int position, Event event) {
    }

    private void addToFavorites(int position, Event event) {
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    public void setEvents(List<Event> events) {
        EventAdapter.events.clear();
        EventAdapter.events.addAll(events);
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitleTextView;
        public TextView eventDateTextView;
        public TextView eventDescriptionTextView;
        public TextView eventTimeTextView;
        public TextView eventMaxAttendeesTextView;
        private final ImageView eventImageView;
        public Button joinButton;
        public Button favoriteButton;


        public EventViewHolder(View itemView) {
            super(itemView);
            eventTitleTextView = itemView.findViewById(R.id.titleTextView);
            eventDateTextView = itemView.findViewById(R.id.dateTextView);
            eventDescriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            eventImageView = itemView.findViewById(R.id.eventImage);
            eventTimeTextView = itemView.findViewById(R.id.timeTextView);
            eventMaxAttendeesTextView = itemView.findViewById(R.id.maxAttendeesTextView);
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
    private void updateEventInViewModel(Event event){
        if(eventViewModel!= null){
            List<Event> updatedEvents= eventViewModel.getEventsLiveData().getValue();
            if(updatedEvents!=null) {
                int index = updatedEvents.indexOf(event);
                if (index != -1) {
                    updatedEvents.set(index, event);
                    eventViewModel.setEvents(updatedEvents);
                }
            }
        }
    }

    private void addToFavorites(Event event) {

            favoriteEvents.add(event);
            refreshFavoritesUI();

    }

    private void removeFromFavorites(Event event) {
        favoriteEvents.remove(event);
        refreshFavoritesUI();
    }

    private void refreshFavoritesUI() {
        // Refresh the favorites list UI
        // Update your RecyclerView adapter or UI elements displaying the favorites list
        // For example, if you have a FavoriteAdapter, call a method like 'setFavorites' to update the list
        if (favoriteAdapter != null) {
            favoriteAdapter.setFavorites(favoriteEvents);
        }
    }

  }

