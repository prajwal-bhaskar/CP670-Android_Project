package com.example.event_management.ui.favourites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.event_management.Event;

import java.util.List;

public class FavouritesViewModel extends ViewModel {
    private MutableLiveData<List<Event>> favoriteEventsLiveData = new MutableLiveData<>();

    public LiveData<List<Event>> getFavoriteEventsLiveData() {
        return favoriteEventsLiveData;
    }

    public void setFavoriteEvents(List<Event> favoriteEvents) {
        favoriteEventsLiveData.setValue(favoriteEvents);
    }
}
