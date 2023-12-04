package com.example.event_management.ui.events;

import android.util.Log;

import com.example.event_management.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private DatabaseReference eventsRef;

    public EventRepository() {
        this.eventsRef = FirebaseDatabase.getInstance().getReference("events");
    }

    public void addEvent(Event event) {
        // Add event to the database
        String eventId = eventsRef.push().getKey();
        eventsRef.child(eventId).setValue(event);
    }

    public LiveData<List<Event>> getEventsLiveData() {
        // Retrieve events from the database and convert to LiveData
        MutableLiveData<List<Event>> liveData = new MutableLiveData<>();

        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Event> events = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Event event = dataSnapshot.getValue(Event.class);
                        if (event != null) {
                            events.add(event);
                        }
                    }
                }
                liveData.setValue(events);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
                Log.e("EventRepository", "Error getting events", error.toException());
            }
        });

        return liveData;
    }

    public DatabaseReference getEventsReference() {
        return eventsRef;
    }
}
