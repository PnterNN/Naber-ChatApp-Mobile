package com.example.nabermobileproject.model;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    private String username;
    private String UID;
    private List<UserModel> friends;
    private List<MessageModel> messages;
    private List<TweetModel> tweets;
    private MessageModel lastMessage = messages.get(messages.size() - 1);
    private Boolean ownRequest;

    public UserModel(String username, String userID) {
        this.username = username;
        this.UID = userID;
        messages = new ArrayList<>();
        friends = new ArrayList<>();
        tweets = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }
    public String getUID() {
        return UID;
    }
    public List<UserModel> getFriends() {
        return friends;
    }
    public List<MessageModel> getMessages() {
        return messages;
    }
    public List<TweetModel> getTweets() {
        return tweets;
    }
    public MessageModel getLastMessage() {
        return lastMessage;
    }
    public void addFriend(UserModel friend) {
        friends.add(friend);
    }
    public void addMessage(MessageModel message) {
        messages.add(message);
    }
    public void addTweet(TweetModel tweet) {
        tweets.add(tweet);
    }
    public void removeFriend(UserModel friend) {
        friends.remove(friend);
    }
    public void removeMessage(MessageModel message) {
        messages.remove(message);
    }
    public void removeTweet(TweetModel tweet) {
        tweets.remove(tweet);
    }
    public Boolean getOwnRequest() {
        return ownRequest;
    }
    public void setOwnRequest(Boolean ownRequest) {
        this.ownRequest = ownRequest;
    }

}
