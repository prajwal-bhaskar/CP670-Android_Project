package com.example.event_management;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "events")
public class EventEntity implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String date;
    private String description;
    private int imageResource;
    private boolean isJoined;
    private boolean isFavorited;

    // Empty constructor required by Room
    public EventEntity() {
    }

    // Constructor for initializing the object
    public EventEntity(String title, String date, String description, int imageResource) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageResource = imageResource;
    }

    // Parcelable constructor
    protected EventEntity(Parcel in) {
        id = in.readInt();
        title = in.readString();
        date = in.readString();
        description = in.readString();
        imageResource = in.readInt();
    }

    public static final Creator<EventEntity> CREATOR = new Creator<EventEntity>() {
        @Override
        public EventEntity createFromParcel(Parcel in) {
            return new EventEntity(in);
        }

        @Override
        public EventEntity[] newArray(int size) {
            return new EventEntity[size];
        }
    };

    // Getters and setters for your fields
    public int getId() {
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getDescription(){
        return description;
    }
    public int getImageResource(){
        return imageResource;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setTitle(String title) {
        this.title = title;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(description);
        dest.writeInt(imageResource);
    }
}
