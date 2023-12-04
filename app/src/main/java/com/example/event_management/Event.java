package com.example.event_management;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event implements Parcelable {
    private String title;
    private String date;
    private String description;
    private boolean isJoined;
    private boolean isFavorited;
    private int imageResource;

    public Event(String title, String date, String description, int imageResource) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageResource = imageResource;
    }

    protected Event(Parcel in) {
        title = in.readString();
        date = in.readString();
        description = in.readString();
        imageResource = in.readInt();
        isJoined = in.readByte() != 0;
        isFavorited = in.readByte() != 0;
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

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResource() {
        return imageResource;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeInt(imageResource);
        dest.writeByte((byte) (isJoined ? 1 : 0));
        dest.writeByte((byte) (isFavorited ? 1 : 0));
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Event event = (Event) obj;
        return title.equals(event.title) && date.equals(event.date);  // Assuming title and date uniquely identify an event
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date);
    }


}
