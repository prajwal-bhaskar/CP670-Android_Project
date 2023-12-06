package com.example.event_management;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn.setValue(loggedIn);
    }

    public LiveData<Boolean> getLoggedIn() {
        return isLoggedIn;
    }
}
