package com.example.event_management.ui.events;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.lifecycle.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.EventBookingActivity;
import com.example.event_management.R;
import com.example.event_management.SharedViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class EventsFragment extends Fragment {

    private EventViewModel eventViewModel;
    private EventAdapter eventAdapter;
    private SharedViewModel sharedViewModel;
    private EventRepository eventRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        eventRepository = new EventRepository();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.eventsRecyclerView);

        EventAdapter.OnItemClickListener onItemClickListener = event -> {
            // Handle item click here
            Intent intent = new Intent(getContext(), EventDescriptionActivity.class);
            intent.putExtra("EVENT", event);
            startActivity(intent);
        };

        eventAdapter = new EventAdapter(onItemClickListener,eventRepository);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventViewModel.getEventsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> updatedEvents) {
                eventAdapter.setEvents(updatedEvents);
            }
        });

        sharedViewModel.getLoggedIn().observe(getViewLifecycleOwner(), isLoggedIn -> {
            requireActivity().invalidateOptionsMenu();
        });

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MenuItem postItem = menu.findItem(R.id.action_post);
        postItem.setVisible(user != null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_events, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_post) {
            navigateToEventBooking();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateToEventBooking() {
        Intent intent = new Intent(getActivity(), EventBookingActivity.class);
        startActivity(intent);
    }
}
