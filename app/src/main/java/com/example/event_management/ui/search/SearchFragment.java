package com.example.event_management.ui.search;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.R;
import com.example.event_management.ui.events.EventAdapter;
import com.example.event_management.ui.events.EventViewModel;
import com.example.event_management.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private EventViewModel eventViewModel;
    private List<Event> allEvents;
    private List<Event> filteredEvents;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        // Initialize allEvents with the initial value from LiveData
        allEvents = eventViewModel.getEventsLiveData().getValue();
        filteredEvents = new ArrayList<>(); // Initialize the filteredEvents list
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Find the RecyclerView from fragment_events.xml
        RecyclerView recyclerView = requireActivity().findViewById(R.id.eventsRecyclerView);

        // Setup EditText for search
        EditText searchEditText = root.findViewById(R.id.search_bar);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the events based on the search query
                filterEvents(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this example
            }
        });

        // Setup Calendar icon click
        ImageButton calendarIcon = root.findViewById(R.id.calendar_icon);
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle calendar icon click here
                // You can implement date-based search functionality
                // For example, show a DatePickerDialog and filter events by selected date
                showDatePickerDialog();
            }
        });

        return root;
    }

    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        // Handle the selected date
                        // For example, you can filter events based on the selected date
                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        filterEvents(selectedDate);
                    }
                },
                year, month, day);

        // Show the date picker dialog
        datePickerDialog.show();
    }
    private void filterEvents(String query) {
        if (allEvents == null) {
            return; // Handle the case where allEvents is null
        }

        filteredEvents.clear();
        for (Event event : allEvents) {
            // Check if the title or date contains the query
            if (event.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    event.getDate().toLowerCase().contains(query.toLowerCase())) {
                filteredEvents.add(event);
            }
        }

        // Notify the adapter that the data set has changed
        //eventViewModel.setEvents(filteredEvents);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
