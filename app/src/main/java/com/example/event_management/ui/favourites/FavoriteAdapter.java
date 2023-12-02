package com.example.event_management.ui.favourites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Event> favoriteEvents;

    public FavoriteAdapter(List<Event> favoriteEvents) {
        this.favoriteEvents = favoriteEvents;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Event event = favoriteEvents.get(position);

        holder.titleTextView.setText(event.getTitle());
        holder.dateTextView.setText(event.getDate());
        // Add other UI updates as needed
    }

    @Override
    public int getItemCount() {
        return favoriteEvents.size();
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

