package com.example.event_management.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.EventEntity;
import com.example.event_management.R;
import com.example.event_management.databinding.FragmentEventsBinding;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;
    private EventViewModel eventViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, eventEntities -> {
            // Update your UI with the updated events list
            // You may need to call notifyDataSetChanged() on the adapter
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.eventsRecyclerView);
        EventAdapter.OnItemClickListener onItemClickListener = new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EventEntity event) {
                // Handle item click here
                Intent intent = new Intent(getContext(), EventDescriptionActivity.class);
                intent.putExtra("EVENT", event);
                startActivity(intent);
            }
        };



        EventAdapter eventAdapter = new EventAdapter(getEventData(), onItemClickListener);

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    // Replace this method with your data retrieval logic
    public List<EventEntity> getEventData() {
        List<EventEntity> events = new ArrayList<>();
        events.add(new EventEntity("Event 1", "2023-11-01", "Description of Event 1", R.drawable.download));
        events.add(new EventEntity("Event 2", "2023-11-10", "Description of Event 2", R.drawable.download));
        events.add(new EventEntity("Event 3", "2023-11-20", "Description of Event 3", R.drawable.download));
        return events;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}