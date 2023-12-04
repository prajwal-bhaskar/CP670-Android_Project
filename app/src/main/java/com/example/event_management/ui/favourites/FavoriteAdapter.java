package com.example.event_management.ui.favourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.R;
import com.example.event_management.ui.events.EventAdapter;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Event> favoriteEvents;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    public FavoriteAdapter(List<Event> favoriteEvents,OnItemClickListener onItemClickListener) {
        this.favoriteEvents = favoriteEvents;
        this.onItemClickListener = onItemClickListener;

    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        if (favoriteEvents != null && position < favoriteEvents.size()) {
            Event event = favoriteEvents.get(position);
            holder.titleTextView.setText(event.getTitle());
            holder.dateTextView.setText(event.getDate());
            // Add other UI updates as needed

            // Set click listener
            holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(event));
        }
    }

    @Override
    public int getItemCount() {

        return favoriteEvents != null ? favoriteEvents.size() : 0;
    }
    public void setFavorites(List<Event> favoriteEvents) {
        this.favoriteEvents = favoriteEvents;
        notifyDataSetChanged();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView dateTextView;
        // Add other UI elements as needed

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            // Initialize other UI elements as needed
        }
    }


}
