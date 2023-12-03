package com.example.event_management.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.R;

import java.util.List;

public class EventsFragment extends Fragment {

    private EventViewModel eventViewModel;
    private EventAdapter eventAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, events -> {
            // Update your UI with the updated events list
            // You may need to call notifyDataSetChanged() on the adapter

            // Assuming eventAdapter is the reference to your EventAdapter
            eventAdapter.setEvents(events);
    });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.eventsRecyclerView);
        EventAdapter.OnItemClickListener onItemClickListener = new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                // Handle item click here
                Intent intent = new Intent(getContext(), EventDescriptionActivity.class);
                intent.putExtra("EVENT", event);
                startActivity(intent);
            }
        };

        // Use the actual data retrieval logic to get the events

        // Instantiate EventAdapter with the actual data
        eventAdapter = new EventAdapter( onItemClickListener);

        // Observe changes to the LiveData
        eventViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), updatedEvents -> {
            // Update the UI when the LiveData changes

            eventAdapter.setEvents(updatedEvents);
            eventAdapter.notifyDataSetChanged();  // Notify the adapter when data changes
        });

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
