package com.example.nabermobileproject.model;

import java.time.LocalDateTime;
import java.util.List;

public class TweetModel extends MessageModel {
    private String username;
    private String tweetMessage;
    private String tweetUID;
    private LocalDateTime date;
    private List<UserModel> likes;
    private Boolean ownMessage;

    public TweetModel(String username, String tweetMessage, String tweetUID, LocalDateTime date) {
        this.username = username;
        this.tweetMessage = tweetMessage;
        this.tweetUID = tweetUID;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getTweetMessage() {
        return tweetMessage;
    }

    public String getTweetUID() {
        return tweetUID;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public List<UserModel> getLikes() {
        return likes;
    }
    public void addLike(UserModel user) {
        likes.add(user);
    }
    public void setLikes(List<UserModel> likes) {
        this.likes = likes;
    }
    public void removeLike(UserModel user) {
        likes.remove(user);
    }
    public Boolean getOwnMessage() {
        return ownMessage;
    }
    public void setOwnMessage(Boolean ownMessage) {
        this.ownMessage = ownMessage;
    }

}
