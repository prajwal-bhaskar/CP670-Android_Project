package com.example.event_management.ui.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.R;
import com.example.event_management.databinding.FragmentEventsBinding;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.eventsRecyclerView);
        EventAdapter eventAdapter = new EventAdapter(getEventData()); // Replace with your data source
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    // Replace this method with your data retrieval logic
    private List<Event> getEventData() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Event 1", "2023-11-01", "Description of Event 1",R.drawable.download));
        events.add(new Event("Event 2", "2023-11-10", "Description of Event 2",R.drawable.download));
        events.add(new Event("Event 3", "2023-11-20", "Description of Event 3",R.drawable.download));
        return events;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}