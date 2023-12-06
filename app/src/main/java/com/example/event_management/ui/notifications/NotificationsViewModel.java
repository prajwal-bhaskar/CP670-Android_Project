package com.example.event_management.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.event_management.Event;

import java.util.List;

public class NotificationsViewModel extends ViewModel {
    private MutableLiveData<List<Event>> joinedEvents;

    public NotificationsViewModel() {
        joinedEvents = new MutableLiveData<>();
        // Load joined events from your data source
    }

    public LiveData<List<Event>> getJoinedEvents() {
        return joinedEvents;
    }

    // Method to update joined events (call this when the user joins/leaves an event)
    public void setJoinedEvents(List<Event> events) {
        joinedEvents.setValue(events);
    }
}
