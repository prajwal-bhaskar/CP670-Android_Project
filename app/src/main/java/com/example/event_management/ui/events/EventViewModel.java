package com.example.event_management.ui.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.event_management.Event;

import java.util.List;

public class EventViewModel extends ViewModel {
    private LiveData<List<Event>> eventsLiveData;
    private EventRepository eventRepository;

    public EventViewModel() {
        eventRepository = new EventRepository();
        eventsLiveData = eventRepository.getEventsLiveData();
    }

    public LiveData<List<Event>> getEventsLiveData() {
        return eventsLiveData;
    }
}
