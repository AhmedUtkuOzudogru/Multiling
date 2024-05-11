package com.example.multiling;

public class NotificationModel {
    String notificationTitle;
    String notificationText;
    int notificationImage;


    public NotificationModel(String notificationTitle, String notificationText, int notificationImage) {
        this.notificationTitle = notificationTitle;
        this.notificationText = notificationText;
        this.notificationImage = notificationImage;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public int getNotificationImage() {
        return notificationImage;
    }
}
