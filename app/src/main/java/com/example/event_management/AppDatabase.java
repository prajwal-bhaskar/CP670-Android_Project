package com.example.event_management;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.event_management.EventDao;
import com.example.event_management.EventEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {EventEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Define abstract method to get DAO instance
    public abstract EventDao eventDao();

    // Singleton pattern to ensure only one instance of the database is created
    private static volatile AppDatabase INSTANCE;

    // Add an executor to handle database write operations on a separate thread
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4); // You can adjust the pool size as needed

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create the database instance
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "events_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Expose the executor to other parts of the application
    public static ExecutorService getDatabaseWriteExecutor() {
        return databaseWriteExecutor;
    }
}
