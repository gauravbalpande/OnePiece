package com.example.journalapp;

import com.google.firebase.Timestamp;

public class Journals {
    private String title;
    private String imageUrl;
    private String thoughts;

    private Timestamp timeAdded;
    private String userId;
    private String userName;

    public Journals() {
    }

    public Journals(String title, String imageUrl, String thoughts, Timestamp timeAdded, String userId, String userName) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.thoughts = thoughts;
        this.timeAdded = timeAdded;
        this.userId = userId;
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
