package com.example.event_management.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.event_management.Event;
import com.example.event_management.databinding.FragmentNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private RecyclerView joinedEventsRecyclerView;
    private JoinedEventsAdapter adapter;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        joinedEventsRecyclerView = binding.joinedEventsRecyclerView;
        joinedEventsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            retrieveJoinedEvents(currentUser.getUid());
        } else {
            binding.textNotifications.setText("Please sign in to view the upcoming events");
        }

        return root;
    }

    private void retrieveJoinedEvents(String userId) {
        FirebaseDatabase.getInstance().getReference("events")
                .orderByChild("joinedUsers/" + userId).equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Event> joinedEvents = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Event event = snapshot.getValue(Event.class);
                            if (event != null) {
                                joinedEvents.add(event);
                            }
                        }
                        updateUIWithJoinedEvents(joinedEvents);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Log error
                    }
                });
    }

    private void updateUIWithJoinedEvents(List<Event> events) {
        if (events.isEmpty()) {
            binding.textNotifications.setText("No Events Joined");
        } else {
            binding.textNotifications.setVisibility(View.GONE);
            if (adapter == null) {
                adapter = new JoinedEventsAdapter(events);
                joinedEventsRecyclerView.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
