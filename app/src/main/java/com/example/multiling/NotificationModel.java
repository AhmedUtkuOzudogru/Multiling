package com.example.multiling;

public class NotificationModel {
    private String title;
    private String message;
    private String timestamp;

    public NotificationModel(String title, String message, String timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

