package com.example.event_management.ui.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.event_management.Event;
import java.util.List;

public class EventViewModel extends ViewModel {
    private MutableLiveData<List<Event>> eventsLiveData = new MutableLiveData<>();

    public LiveData<List<Event>> getEventsLiveData() {
        return eventsLiveData;
    }

    public void setEvents(List<Event> events) {
        eventsLiveData.setValue(events);
    }
}