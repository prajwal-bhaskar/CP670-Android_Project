package com.example.event_management;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM events")
    LiveData<List<EventEntity>> getAllEvents();

    @Insert
    void insertEvent(EventEntity event);

    // Add other methods for additional database operations if needed
}
