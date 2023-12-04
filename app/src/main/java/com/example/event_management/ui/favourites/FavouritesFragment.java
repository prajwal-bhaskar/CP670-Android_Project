package com.example.event_management.ui.favourites;

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
import com.example.event_management.databinding.FragmentFavouritesBinding;
import com.example.event_management.ui.events.EventAdapter;
import com.example.event_management.ui.events.EventDescriptionActivity;
import com.example.event_management.ui.events.EventViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {
    private FragmentFavouritesBinding binding;
    private FavouritesViewModel favouritesViewModel;

    private List<Event> favoriteEvents = new ArrayList<>();
    private FavoriteAdapter favoriteAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        favouritesViewModel = new ViewModelProvider(requireActivity()).get(FavouritesViewModel.class);

        RecyclerView favoritesRecyclerView = root.findViewById(R.id.text_favourites);

        // Create an instance of OnItemClickListener
        FavoriteAdapter.OnItemClickListener onItemClickListener = event -> {
            Intent intent = new Intent(requireContext(), EventDescriptionActivity.class);
            intent.putExtra("EVENT", event);
            startActivity(intent);
        };
        // Initialize the FavoriteAdapter with an empty list
        favoriteAdapter = new FavoriteAdapter(new ArrayList<>() ,onItemClickListener);
        favoritesRecyclerView.setAdapter(favoriteAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Observe changes in favorite events and update the FavoriteAdapter
        observeFavorites();
        // Other initialization

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Reuse the data retrieval logic from EventsFragment
    private List<Event> getFavoriteEvents(List<Event> allEvents) {
        List<Event> favoriteEvents = new ArrayList<>();
        for (Event event : allEvents) {
            if (event.isFavorited()) {
                favoriteEvents.add(event);
            }
        }
        return favoriteEvents;
    }
    private void observeFavorites() {
        favouritesViewModel.getFavoriteEventsLiveData().observe(getViewLifecycleOwner(), favoriteEvents -> {
            if (favoriteAdapter != null) {
                favoriteAdapter.setFavorites(favoriteEvents);
            }
            // You may need to call notifyDataSetChanged() on the adapter
        });
    }
}
