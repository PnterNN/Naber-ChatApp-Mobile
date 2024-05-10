package com.example.nabermobileproject.services;

import com.example.nabermobileproject.NET.Server;
import com.example.nabermobileproject.model.TweetModel;
import com.example.nabermobileproject.model.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataService {
    public static String username;
    public static String UID;
    public static ArrayList<UserModel> users;
    public static ArrayList<UserModel> friends;
    public static ArrayList<UserModel> friendRequests;
    public static ArrayList<TweetModel> tweets = new ArrayList<>();
    public static Server server;
}
