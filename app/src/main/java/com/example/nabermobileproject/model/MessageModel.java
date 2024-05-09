package com.example.nabermobileproject.model;

import java.time.LocalDateTime;

public class MessageModel {
    private String username;
    private String message;
    private String UID;
    private LocalDateTime date;
    public MessageModel(String username, String message, String UID, LocalDateTime date) {
        this.username = username;
        this.message = message;
        this.UID = UID;
        this.date = date;
    }

    public MessageModel() {
    }

    public String getUsername() {
        return username;
    }
    public String getMessage() {
        return message;
    }
    public String getUID() {
        return UID;
    }
    public LocalDateTime getDate() {
        return date;
    }

}
