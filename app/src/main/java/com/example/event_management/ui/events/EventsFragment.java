package com.example.event_management.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.event_management.Event;
import com.example.event_management.EventBookingActivity;
import com.example.event_management.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        setHasOptionsMenu(true);
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
        eventAdapter = new EventAdapter(eventViewModel, onItemClickListener, favoriteAdapter);


        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Observe changes to the LiveData and update the UI when data changes
        eventViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> updatedEvents) {
                eventAdapter.setEvents(updatedEvents);
                eventAdapter.notifyDataSetChanged();  // Notify the adapter when data changes
            }
        });


        // Here, you should retrieve events from Firebase or another data source and set them in the ViewModel
        // Example: eventViewModel.setEvents(yourEventList);


        // You can also handle errors or empty data
        //if (eventViewModel.getEventsLiveData().getValue() == null || eventViewModel.getEventsLiveData().getValue().isEmpty()) {
        //Toast.makeText(requireContext(), "No events found.", Toast.LENGTH_SHORT).show();
        //}


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_events, menu);
        super.onCreateOptionsMenu(menu, inflater);


        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            if (item.getItemId() == R.id.action_post) {
                navigateToEventBooking();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
        private void navigateToEventBooking () {
            // Logic to navigate to Event Booking Activity
            Intent intent = new Intent(getActivity(), EventBookingActivity.class);
            startActivity(intent);
        }

        private void observeEvents () {
            eventViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), allEvents -> {
                eventAdapter.setEvents(allEvents);
                // You may need to call notifyDataSetChanged() on the adapter
            });
        }


    }
}