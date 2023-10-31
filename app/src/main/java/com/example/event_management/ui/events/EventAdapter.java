package com.example.event_management.ui.events;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
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
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitleTextView;
        public TextView eventDateTextView;
        public TextView eventDescriptionTextView;

        private ImageView eventImageView;

        public EventViewHolder(View itemView) {
            super(itemView);
            eventTitleTextView = itemView.findViewById(R.id.titleTextView);
            eventDateTextView = itemView.findViewById(R.id.dateTextView);
            eventDescriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            eventImageView = itemView.findViewById(R.id.eventImage);
        }

        public void bind(Event event) {

            eventTitleTextView.setText(event.getTitle());
            eventImageView.setImageResource(event.getImageResource());
            eventDateTextView.setText(event.getDate());
            eventDescriptionTextView.setText(event.getDescription());
        }

    }
}

