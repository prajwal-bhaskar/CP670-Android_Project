package com.example.event_management;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Event implements Parcelable {
    private String id;
    private String title;
    private String date;
    private String description;
    private String imageUrl;
    private String time;
    private int maxAttendees;
    private boolean isJoined;
    private boolean isFavorited;

    private Map<String, Boolean> joinedUsers;

    // Default image URL - replace with the actual URL of your default image in Firebase Storage
    private static final String DEFAULT_IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/cp670-eventmanagement.appspot.com/o/default.jpg?alt=media&token=33027533-9a73-48ef-b276-5536e8056308";

    // No-argument constructor for Firebase
    public Event() {
        this.imageUrl = DEFAULT_IMAGE_URL;
        this.joinedUsers = new HashMap<>();
    }

    // Constructor with image URL
    public Event(String id,String title, String date, String description, String time, String imageUrl, int maxAttendees) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
        this.time = time;
        this.maxAttendees = maxAttendees;
        this.imageUrl = imageUrl != null ? imageUrl : DEFAULT_IMAGE_URL;
        this.joinedUsers = new HashMap<>();
    }

    // Parcelable implementation
    protected Event(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = in.readString();
        description = in.readString();
        imageUrl = in.readString();
        time = in.readString();
        maxAttendees = in.readInt();
        isJoined = in.readByte() != 0;
        isFavorited = in.readByte() != 0;
        Object readObject = in.readSerializable();
        if (readObject instanceof HashMap<?, ?>) {
            //noinspection unchecked
            joinedUsers = (HashMap<String, Boolean>) readObject;
        } else {
            joinedUsers = new HashMap<>();
        }
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    public int getMaxAttendees(){return maxAttendees;}

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl != null ? imageUrl : DEFAULT_IMAGE_URL;
    }

    public boolean isJoined() {
        return isJoined;
    }

    public void setJoined(boolean joined) {
        isJoined = joined;
    }

    public boolean isFavorited() {
        return isFavorited;
    }

    public void setFavorited(boolean favorited) {
        isFavorited = favorited;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public Map<String, Boolean> getJoinedUsers() {
        return joinedUsers;
    }

    public void setJoinedUsers(Map<String, Boolean> joinedUsers) {
        this.joinedUsers = joinedUsers;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeString(imageUrl);
        dest.writeString(time);
        dest.writeInt(maxAttendees);
        dest.writeByte((byte) (isFavorited ? 1 : 0));
        dest.writeSerializable((Serializable) joinedUsers);
    }
}
