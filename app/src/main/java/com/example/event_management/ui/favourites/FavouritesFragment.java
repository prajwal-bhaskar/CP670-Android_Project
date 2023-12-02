package com.example.event_management.ui.favourites;

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

import com.example.event_management.Event;
import com.example.event_management.EventEntity;
import com.example.event_management.R;
import com.example.event_management.databinding.FragmentFavouritesBinding;
import com.example.event_management.ui.events.EventAdapter;
import com.example.event_management.ui.events.EventDescriptionActivity;
import com.example.event_management.ui.events.EventViewModel;

import java.util.ArrayList;
import java.util.List;


public class FavouritesFragment extends Fragment {
    private EventViewModel eventViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        eventViewModel.getEventsLiveData().observe(this, favoriteEvents -> {
            // Update your UI with the favorite events list
            // You may need to call notifyDataSetChanged() on the adapter
        });
    }
    private FragmentFavouritesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.text_favourites);
        EventAdapter.OnItemClickListener onItemClickListener = new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(EventEntity event) {
                // Handle item click here
                Intent intent = new Intent(getContext(), EventDescriptionActivity.class);
                intent.putExtra("EVENT", event);
                startActivity(intent);
            }
        };

        EventAdapter eventAdapter = new EventAdapter(getFavouritesData(), onItemClickListener);

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Reuse the data retrieval logic from EventsFragment
    private List<Event> getEventData() {
        // This method should provide the necessary data for your FavouritesFragment
        // It could be fetching data from a database, network, or any other source.
        List<Event> events = new ArrayList<>();
        // Populate the events list as needed
        return events;
    }

    // Filter the list to get only favorite events
    public List<EventEntity> getFavouritesData() {
        List<EventEntity> favourites = new ArrayList<>();
        // Add your favourites data here
        return favourites;
    }
}
