package com.example.event_management.ui.events;
import com.bumptech.glide.Glide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.R;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public EventAdapter(OnItemClickListener onItemClickListener) {
        this.events = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void setEvents(List<Event> events) {
        this.events.clear();
        this.events.addAll(events);
        notifyDataSetChanged();
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
        Glide.with(holder.itemView.getContext())
                .load(event.getImageUrl())
                .into(holder.eventImageView);

        // Check if the event is joined or favorited and update UI accordingly
        if (event.isJoined()) {
            holder.joinButton.setText("Leave");
        } else {
            holder.joinButton.setText("Join");
        }

        if (event.isFavorited()) {
            holder.favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fav_light, 0, 0, 0);
        } else {
            holder.favoriteButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_fav_dark, 0, 0, 0);
        }

        holder.joinButton.setOnClickListener(v -> {
            event.setJoined(!event.isJoined());
            notifyItemChanged(position);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(event);
            }
        });

        holder.favoriteButton.setOnClickListener(v -> {
            event.setFavorited(!event.isFavorited());
            notifyItemChanged(position);
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

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
}
