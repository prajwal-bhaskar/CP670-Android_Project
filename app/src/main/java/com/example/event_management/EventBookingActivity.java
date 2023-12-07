package com.example.event_management;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventBookingActivity extends AppCompatActivity {

    private EditText eventNameEditText, eventTimeEditText, eventDescriptionEditText, maxAttendeesEditText;
    private ImageView eventImageView;
    private Button submitButton, selectImageButton, eventDateButton;
    private DatabaseReference databaseReference;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageUrl, selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_booking);
        setTitle("New Event");

        eventNameEditText = findViewById(R.id.eventName);
        eventDateButton = findViewById(R.id.chooseDateButton);
        eventDateButton.setOnClickListener(this::showDatePickerDialog);
        eventTimeEditText = findViewById(R.id.eventTime);
        eventDescriptionEditText = findViewById(R.id.eventDescription);
        maxAttendeesEditText = findViewById(R.id.maxAttendees);
        eventImageView = findViewById(R.id.eventImage);
        submitButton = findViewById(R.id.submitEventButton);
        selectImageButton = findViewById(R.id.selectImageButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("events");

        selectImageButton.setOnClickListener(v -> selectImage());

        submitButton.setOnClickListener(v -> {
            if (imageUri != null) {
                uploadImageToFirebase(imageUri);
            } else {
                submitEvent(""); // Call with empty string if no image
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            eventImageView.setImageURI(imageUri);
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads")
                    .child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        imageUrl = uri.toString();
                        submitEvent(imageUrl); // Call submitEvent with imageUrl
                    }))
                    .addOnFailureListener(e -> Toast.makeText(EventBookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = String.format(Locale.getDefault(), "dd/MM/yyyy", selectedDay, selectedMonth + 1, selectedYear);
                    eventDateButton.setText(selectedDate); // Update the button text with the selected date
                },
                day, month, year);

        datePickerDialog.show();
    }
    private void submitEvent(String imagePath) {
        String name = eventNameEditText.getText().toString().trim();
        String date = selectedDate;
        String time = eventTimeEditText.getText().toString().trim();
        String description = eventDescriptionEditText.getText().toString().trim();
        int maxAttendees = Integer.parseInt(maxAttendeesEditText.getText().toString().trim());
        Map<String, Boolean> joinedUsers = new HashMap<>();

        // Get a new unique key from Firebase Database
        String eventId = databaseReference.push().getKey();
        Event event = new Event(eventId,name, date, description, time, imagePath, maxAttendees);
        event.setJoinedUsers(joinedUsers);
        event.setId(eventId); // Set the event ID

        if (eventId != null) {
            databaseReference.child(eventId).setValue(event)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EventBookingActivity.this, "Event booked successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Finish this activity and go back to the previous one (EventsFragment)
                    })
                    .addOnFailureListener(e -> Toast.makeText(EventBookingActivity.this, "Failed to book event.", Toast.LENGTH_SHORT).show());
        }
    }


}
