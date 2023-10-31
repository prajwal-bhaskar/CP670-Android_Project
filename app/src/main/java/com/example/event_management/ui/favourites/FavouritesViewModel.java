package com.example.event_management.ui.favourites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavouritesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FavouritesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Favourite events will be saved here");
    }

    public LiveData<String> getText() {
        return mText;
    }
}