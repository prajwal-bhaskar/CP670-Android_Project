package com.example.event_management;

public class Event {
    private String title;
    private String date;
    private String description;


    private int imageResource;

    public Event(String title, String date, String description, int imageResource) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.imageResource = imageResource;
    }

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


}
