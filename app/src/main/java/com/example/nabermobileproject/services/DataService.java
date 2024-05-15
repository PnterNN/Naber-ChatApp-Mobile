package com.example.nabermobileproject.services;

import com.example.nabermobileproject.NET.Server;
import com.example.nabermobileproject.adapters.ChatAdapter;
import com.example.nabermobileproject.adapters.TweetAdapter;
import com.example.nabermobileproject.adapters.UserAdapter;
import com.example.nabermobileproject.model.TweetModel;
import com.example.nabermobileproject.model.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataService {
    public static String username;
    public static String UID;
    public static ArrayList<UserModel> users;
    public static ArrayList<UserModel> friends;
    public static ArrayList<UserModel> friendRequests;
    public static ArrayList<TweetModel> tweets;
    public static Server server;
    public static HashMap<String, ChatAdapter> chatAdapter;
    public static TweetAdapter tweetAdapter;
    public static UserAdapter userAdapter;

    public static UserModel getUser(String UID){
        for(UserModel user : users){
            if(user.getUID().equals(UID)){
                return user;
            }
        }
        return null;
    }
}
