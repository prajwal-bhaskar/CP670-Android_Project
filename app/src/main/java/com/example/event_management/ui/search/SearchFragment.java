package com.example.event_management.ui.search;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.R;
import com.example.event_management.ui.events.EventAdapter;
import com.example.event_management.ui.events.EventRepository;
import com.example.event_management.ui.events.EventViewModel;
import com.example.event_management.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private EventViewModel eventViewModel;
    private EditText searchEditText;
    private EventAdapter eventAdapter;
    private EventRepository eventRepository;
    private List<Event> allEvents;
    private List<Event> filteredEvents;

    private String selectedDate = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // Initialize allEvents and filteredEvents lists
        allEvents = new ArrayList<>();
        filteredEvents = new ArrayList<>();

        // Observe eventsLiveData and update allEvents when it changes
        eventViewModel.getEventsLiveData().observe(this, events -> {
            allEvents = events;
            filterEvents(""); // Filter with empty string to show all events initially
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        eventRepository = new EventRepository();
        // Setup RecyclerView and Adapter
        RecyclerView recyclerView = binding.eventsRecyclerView;
        eventAdapter = new EventAdapter(event -> {
            // Handle event item click
        },eventRepository);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Setup EditText for search
        searchEditText = root.findViewById(R.id.search_bar);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterEvents(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        ImageButton calendarIcon = root.findViewById(R.id.calendar_icon);
        calendarIcon.setOnClickListener(v -> showDatePickerDialog());
        return root;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year1);
                    filterEvents(searchEditText.getText().toString());
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void filterEvents(String query) {
        filteredEvents.clear();
        for (Event event : allEvents) {
            boolean matchesQuery = event.getTitle().toLowerCase().contains(query.toLowerCase());
            boolean matchesDate = selectedDate.isEmpty() || event.getDate().equals(selectedDate);

            if (matchesQuery && matchesDate) {
                filteredEvents.add(event);
            }
        }

        eventAdapter.setEvents(filteredEvents);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
