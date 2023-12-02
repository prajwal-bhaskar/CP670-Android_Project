package com.example.event_management.ui.events;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.event_management.AppDatabase;
import com.example.event_management.EventDao;
import com.example.event_management.EventEntity;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private EventDao eventDao;
    private MediatorLiveData<List<EventEntity>> eventsLiveData = new MediatorLiveData<>();

    public EventViewModel(Application application) {
        super(application);
        AppDatabase db = AppDatabase.getInstance(application);
        eventDao = db.eventDao();
        loadEvents();
        Log.d("ViewModelFetch", "Fetching data from the ViewModel...");
    }

    private void loadEvents() {
        LiveData<List<EventEntity>> daoLiveData = eventDao.getAllEvents();
        eventsLiveData.addSource(daoLiveData, events -> {
            eventsLiveData.setValue(events);
            eventsLiveData.removeSource(daoLiveData);
        });
    }

    public LiveData<List<EventEntity>> getEventsLiveData() {
        return eventsLiveData;
    }

    public void insertEvent(EventEntity event) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> {
            eventDao.insertEvent(event);
        });
    }

    public void setEvents(List<EventEntity> events) {
        eventsLiveData.setValue(events);
    }

    public void updateEvents(List<EventEntity> events) {
        eventsLiveData.postValue(events);
    }
}
