package com.example.nabermobileproject.services;

import android.util.Log;

import com.example.nabermobileproject.NET.Server;

import java.io.IOException;

public class ServerManager {
    private static Server instance;
    public static void loginServer(String username, String password) {
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.login(username, password);
                } catch (IOException e) {
                    Log.e("NaberApp", "loginServer: ", e);
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void sendVoiceMessage(byte[] voiceMessage, String UID, String ContactUID){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.sendVoiceMessage(voiceMessage, UID, ContactUID);
                } catch (IOException e) {
                    Log.e("NaberApp", "sendVoiceMessage: ", e);
                }
            });
            thread.start();
        }
    }

    public static void sendFriendRemove(String username){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.sendFriendRemove(username);
                } catch (IOException e) {
                    Log.e("NaberApp", "sendFriendRemoveRequest: ", e);
                }
            });
            thread.start();
        }
    }

    public static void sendFriendDecline(String username){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.sendFriendDecline(username);
                } catch (IOException e) {
                    Log.e("NaberApp", "sendFriendDeclineRequest: ", e);
                }
            });
            thread.start();
        }
    }

    public static void sendFriendAccept(String username){

        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.sendFriendAccept(username);
                } catch (IOException e) {
                    Log.e("NaberApp", "sendFriendAcceptRequest: ", e);
                }
            });
            thread.start();
        }
    }

    public static void sendFriendRequestCancel(String username){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.sendFriendRequestCancel(username);
                } catch (IOException e) {
                    Log.e("NaberApp", "sendFriendRequestCancelRequest: ", e);
                }
            });
            thread.start();
        }
    }

    public static void sendFriendRequest(String username){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.sendFriendRequest(username);
                } catch (IOException e) {
                    Log.e("NaberApp", "sendFriendRequest: ", e);
                }
            });
            thread.start();
        }
    }

    public static void createGroup(String groupName, String clientIDS){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.createGroup(groupName, clientIDS);
                } catch (IOException e) {
                    Log.e("NaberApp", "createGroup: ", e);
                }
            });
            thread.start();
        }
    }

    public static void sendMessage(String message, String contactUID, String firstMessage, String messageUID){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.sendMessage(message, contactUID, firstMessage, messageUID);
                } catch (IOException e) {
                    Log.e("NaberApp", "sendMessage: ", e);
                }
            });
            thread.start();
        }else{
            Log.e("NaberApp", "sendMessage: instance is null");
        }
    }
    public static void deleteTweet(String tweetUID){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.deleteTweet(tweetUID);
                } catch (IOException e) {
                    Log.e("NaberApp", "deleteTweet: ", e);
                }
            });
            thread.start();
        }
    }

    public static void likeTweet(String tweetUID){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.likeTweet(tweetUID);
                } catch (IOException e) {
                    Log.e("NaberApp", "likeTweet: ", e);
                }
            });
            thread.start();
        }
    }

    public static void sendTweet(String message, String tweetUID){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.sendTweet(message, tweetUID);
                } catch (IOException e) {
                    Log.e("NaberApp", "sendTweet: ", e);
                }
            });
            thread.start();
        }
    }

    public static void deleteMessage(String messageUID, String contactUID){
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.deleteMessage(messageUID, contactUID);
                } catch (IOException e) {
                    Log.e("NaberApp", "deleteMessage: ", e);
                }
            });
            thread.start();
        }
    }

    public static void registerServer(String username, String email, String password) {
        if (instance != null) {
            Thread thread = new Thread(() -> {
                try {
                    instance.register(username, email, password);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static Server getInstance() {
        if (instance == null) {
            synchronized (ServerManager.class) {
                if (instance == null) {
                    Thread thread = new Thread(() -> {
                        instance = new Server();
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return instance;
    }
}
