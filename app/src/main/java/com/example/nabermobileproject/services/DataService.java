package com.example.nabermobileproject.services;

import com.example.nabermobileproject.NET.Server;
import com.example.nabermobileproject.model.TweetModel;
import com.example.nabermobileproject.model.UserModel;

import java.io.Serializable;
import java.util.List;

public class DataService {
    public static String username;
    public static String UID;
    public static List<UserModel> users;
    public static List<UserModel> friends;
    public static List<UserModel> friendRequests;
    public static List<TweetModel> tweets;
    public static Server server;
}
