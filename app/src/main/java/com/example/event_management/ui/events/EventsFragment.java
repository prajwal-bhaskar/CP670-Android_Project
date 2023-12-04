package com.example.event_management.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.event_management.Event;
import com.example.event_management.R;
import com.example.event_management.databinding.FragmentEventsBinding;
import com.example.event_management.ui.favourites.FavoriteAdapter;
import com.example.event_management.ui.favourites.FavouritesViewModel;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private FragmentEventsBinding binding;
    private EventViewModel eventViewModel;
    private FavouritesViewModel favouritesViewModel;
    private EventAdapter eventAdapter;
    private FavoriteAdapter favoriteAdapter;
    private FavoriteAdapter.OnItemClickListener onItemClickListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // Initialize the EventAdapter with an empty list and an item click listener
            EventAdapter.OnItemClickListener onItemClickListener = event -> {
                Intent intent = new Intent(requireContext(), EventDescriptionActivity.class);
                intent.putExtra("EVENT", event);
                startActivity(intent);
            };
        eventAdapter = new EventAdapter(getEventData(),eventViewModel, onItemClickListener, favoriteAdapter);


        // Observe changes in all events and update the EventAdapter
        observeEvents();


        RecyclerView recyclerView = view.findViewById(R.id.eventsRecyclerView);

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private List<Event> getEventData() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("Event 1", "2023-11-01", "Description of Event 1", R.drawable.download));
        events.add(new Event("Event 2", "2023-11-10", "Description of Event 2", R.drawable.download));
        events.add(new Event("Event 3", "2023-11-20", "Description of Event 3", R.drawable.download));
        return events;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void observeEvents() {
        eventViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), allEvents -> {
            eventAdapter.setEvents(allEvents);
            // You may need to call notifyDataSetChanged() on the adapter
        });
    }


}
