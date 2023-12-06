package com.example.event_management.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.event_management.Event;
import com.example.event_management.R;
import java.util.List;

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
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event event = joinedEvents.get(position);
        holder.titleTextView.setText(event.getTitle());
        holder.dateTextView.setText(event.getDate());
        holder.timeTextView.setText(event.getTime());
    }

    @Override
    public int getItemCount() {
        return joinedEvents.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        TextView timeTextView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.joinedEventTitle);
            dateTextView = view.findViewById(R.id.joinedEventDate);
            timeTextView = view.findViewById(R.id.joinedEventTime);
        }
    }
}
