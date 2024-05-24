package com.example.nabermobileproject.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.nabermobileproject.NET.Server;
import com.example.nabermobileproject.R;
import com.example.nabermobileproject.adapters.ChatAdapter;
import com.example.nabermobileproject.adapters.TweetAdapter;
import com.example.nabermobileproject.model.MessageModel;
import com.example.nabermobileproject.model.TweetModel;
import com.example.nabermobileproject.model.UserModel;
import com.example.nabermobileproject.services.DataService;
import com.example.nabermobileproject.services.ServerManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class TweetActivity extends AppCompatActivity {

    MediaPlayer notificationSound;
    private static int badgeCount;
    private static final int NOTIFICATION_ID = 99040499;
    private NotificationManagerCompat notificationManager;
    private NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    private NotificationCompat.InboxStyle inboxStyle2 = new NotificationCompat.InboxStyle();
    Button sendTweetButton;
    Button chatButton;
    ListView tweetList;
    EditText tweetBox;

    private void sendTweet(View v) {
        if (tweetBox.getText().toString().equals("")) {
            return;
        }
        Random random = new Random();
        int tweetUID = random.nextInt((999999999 - 100000000) + 1) + 100000000;
        ServerManager.sendTweet(tweetBox.getText().toString(), tweetUID + "");
        tweetBox.setText("");
    }

    private void openChat(View v) {
        try {
            Intent chatIntent = new Intent(this, UserlistActivity.class);
            startActivity(chatIntent);
        } catch (Exception e) {
            Log.e("NaberApp", "openChat: ", e);
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        badgeCount = 0;
        notificationManager = NotificationManagerCompat.from(this);
        notificationSound = MediaPlayer.create(this, R.raw.notification);
        sendTweetButton = findViewById(R.id.tweetButton);
        chatButton = findViewById(R.id.chatButton);
        tweetList = findViewById(R.id.tweetListView);
        tweetBox = findViewById(R.id.tweetBox);

        DataService.server = ServerManager.getInstance();

        DataService.tweetAdapter = new TweetAdapter(this);
        tweetList.setAdapter(DataService.tweetAdapter);

        sendTweetButton.setOnClickListener(this::sendTweet);
        chatButton.setOnClickListener(this::openChat);


        DataService.server.userConnectedEvent = this::userConnected;
        DataService.server.userRegisterConnectedEvent = this::registerUserConnected;
        DataService.server.messageReceivedEvent = this::messageReceived;
        DataService.server.deleteMessageEvent = this::deleteMessageEvent;
        DataService.server.userDisconnectedEvent = this::userDisconnected;
        DataService.server.likeEvent = this::likeEvent;
        DataService.server.getTweetsEvent = this::getTweets;
        DataService.server.getFriendEvent = this::getFriendEvent;
        DataService.server.deleteTweetEvent = this::deleteTweetEvent;
        DataService.server.friendRemoveEvent = this::friendRemoveEvent;
        DataService.server.friendRequestEvent = this::friendRequestEvent;
        DataService.server.tweetReceivedEvent = this::tweetReceivedEvent;
        DataService.server.friendRequestCancelEvent = this::friendRequestCancelEvent;
        DataService.server.friendRequestAcceptEvent = this::friendRequestAcceptEvent;
        DataService.server.friendRequestDeclineEvent = this::friendRequestDeclineEvent;
        DataService.server.groupCreatedEvent = this::createGroup;


    }


    private void createGroup(Void unused) {
        String groupName = DataService.server.getPacketReader().readMessage();
        String groupUID = DataService.server.getPacketReader().readMessage();
        String[] groupUsers = groupUID.split(" ");
        List<String> usernames = new ArrayList<>();
        for (String user : groupUsers) {
            //UserModel user1 = DataService.users.stream().filter(x -> x.getUID().equals(user)).findFirst().get();
            //if(user1 != null){
            //    usernames.add(user1.getUsername());
            //}
        }
        //UserModel group = new UserModel(groupName, groupUID);
        //DataService.users.add(group);
        //group.getMessages().add(new TweetModel("NaberApp", "Grup oluşturuldu", UUID.randomUUID().toString(), null));
    }


    private void tweetReceivedEvent(Void unused) {
        String username = DataService.server.getPacketReader().readMessage();
        String tweetMessage = DataService.server.getPacketReader().readMessage();
        String tweetUID = DataService.server.getPacketReader().readMessage();
        runOnUiThread(() -> {
            notificationSound.start();
            TweetModel tweet = new TweetModel(username, tweetMessage, tweetUID, null);
            DataService.tweetAdapter.addTweet(tweet);
            DataService.tweetAdapter.notifyDataSetChanged();
            tweetList.requestLayout();
        });
        inboxStyle2.addLine(username + ": " + tweetMessage);
        Intent intent = new Intent(this, TweetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        badgeCount++;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "naber_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tweetleriniz var")
                .setStyle(inboxStyle2)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setNumber(badgeCount)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        notificationManager.notify(NOTIFICATION_ID+1, builder.build());


    }

    private void likeEvent(Void unused) {
        String userUID = DataService.server.getPacketReader().readMessage();
        String tweetUID = DataService.server.getPacketReader().readMessage();
        for (TweetModel tweet : DataService.tweets) {
            if (tweet.getTweetUID().equals(tweetUID)) {
                tweet.addLike(new UserModel("", userUID));
            }
        }
    }

    private void messageReceived(Void unused) {
        String message = DataService.server.getPacketReader().readMessage();
        String username = DataService.server.getPacketReader().readMessage();
        String sendedUserUID = DataService.server.getPacketReader().readMessage();
        String messageUID = DataService.server.getPacketReader().readMessage();
        Log.d("NaberApp", "messageReceived: " + message + " " + username + " " + sendedUserUID + " " + messageUID);

        Intent intent = new Intent(this, UserlistActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        runOnUiThread(() -> {
            UserModel user = DataService.users.stream().filter(x -> x.getUID().equals(sendedUserUID)).findFirst().get();
            if (user != null) {
                if (DataService.chatAdapter != null) {
                    DataService.chatAdapter.get(sendedUserUID).addMessage(new MessageModel(username, message, messageUID, null));
                    DataService.chatAdapter.get(sendedUserUID).notifyDataSetChanged();
                    notificationSound.start();
                }
            }
        });
        badgeCount++;
        inboxStyle.addLine(username + ": " + message);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "naber_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Mesajlarınız var")
                .setStyle(inboxStyle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setNumber(badgeCount)
                .setAutoCancel(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        badgeCount = 0;
    }
    private void userDisconnected(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        runOnUiThread(() -> {
            try{
                UserModel user = DataService.users.stream().filter(x -> x.getUsername().equals(username)).findFirst().get();
                if(user != null){
                    DataService.userAdapter.removeUser(user);
                    DataService.chatAdapter.remove(user.getUID());
                    DataService.userAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                Log.e("NaberApp", "userDisconnected: ", e);
            }
        });
    }

    private void registerUserConnected(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        String UID = DataService.server.getPacketReader().readMessage();
        runOnUiThread(() -> {
            try{
                UserModel user = new UserModel(username, UID);

                if(DataService.chatAdapter.get(UID) == null){
                    DataService.userAdapter.addUser(user);
                    DataService.chatAdapter.put(UID, new ChatAdapter(this, UID));
                }
                if(DataService.userAdapter != null){
                    DataService.userAdapter.notifyDataSetChanged();
                }
            }catch (Exception e){
                Log.e("NaberApp", "registerUserConnected: ", e);
            }

        });
        //UserModel user = new UserModel(username, UID);
        //user.getMessages().add(new TweetModel(username,"Welcome to the chat!", UUID.randomUUID().toString(), null));
    }
    private void userConnected(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        String UID = DataService.server.getPacketReader().readMessage();
        int messageCount = Integer.parseInt(DataService.server.getPacketReader().readMessage());
        Log.d("NaberApp", "userConnected: "+username+" "+UID+" "+messageCount);
        runOnUiThread(() -> {
            UserModel user = new UserModel(username, UID);
            if(DataService.chatAdapter.get(UID) == null){
                DataService.userAdapter.addUser(user);
                DataService.chatAdapter.put(UID, new ChatAdapter(this, UID));
            }
        });
        String dataUsername = "";
        String dataUID = "";
        String dataImageSource = "";
        String dataMessage = "";
        String dataVoice = "";
        String dataTime = null;//LocalDateTime.now();
        boolean dataFirstMessage = true;
        String messageUID = "";
        int index2 = 0;
        for (int i = 0; i < messageCount; i++) {
            dataUsername = DataService.server.getPacketReader().readMessage();
            dataUID = DataService.server.getPacketReader().readMessage();
            dataImageSource = DataService.server.getPacketReader().readMessage();
            dataMessage = DataService.server.getPacketReader().readMessage();
            dataVoice = DataService.server.getPacketReader().readMessage();
            dataTime = DataService.server.getPacketReader().readMessage();
            dataFirstMessage = Boolean.parseBoolean(DataService.server.getPacketReader().readMessage());
            messageUID = DataService.server.getPacketReader().readMessage();
            String finalDataVoice1 = dataVoice;
            String finalDataUsername = dataUsername;
            String finalDataMessage = dataMessage;
            String finalMessageUID = messageUID;
            runOnUiThread(() -> {
                try{
                    if(finalDataVoice1.equals("0")){
                        if(finalDataUsername !=""){
                            boolean ownMessage = false;
                            if(DataService.username == finalDataUsername){
                                ownMessage = true;
                            }
                            DataService.chatAdapter.get(UID).addMessage(new MessageModel(finalDataUsername, finalDataMessage, finalMessageUID + "", null));

                        }else{
                            DataService.chatAdapter.get(UID).addMessage(new MessageModel("NaberApp", username+" çevrimiçi oldu", UUID.randomUUID().toString(), null));
                        }
                        DataService.userAdapter.notifyDataSetChanged();
                    }
                    if(DataService.userAdapter != null){
                        DataService.userAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    Log.e("NaberApp", "userConnected: ", e);
                }
            });
        }
    }

    private void deleteMessageEvent(Void unused){
        String contactUID = DataService.server.getPacketReader().readMessage();
        String messageUID = DataService.server.getPacketReader().readMessage();
        for (UserModel user : DataService.users){
            if (user.getUID().equals(contactUID)){
                for (MessageModel message : user.getMessages()){
                    if (message.getUID().equals(messageUID)){
                        DataService.chatAdapter.get(contactUID).removeMessage(message);
                        DataService.chatAdapter.get(contactUID).notifyDataSetChanged();
                    }
                }
            }
        }
    }
    private void getTweets(Void unused) {
        int tweetCount = Integer.parseInt(DataService.server.getPacketReader().readMessage());
        for (int i = 0; i < tweetCount; i++) {
            String username = DataService.server.getPacketReader().readMessage();
            String tweetUID = DataService.server.getPacketReader().readMessage();
            String tweetImage = DataService.server.getPacketReader().readMessage();
            String tweetMessage = DataService.server.getPacketReader().readMessage();
            String tweetLikes = DataService.server.getPacketReader().readMessage();
            String tweetTime = DataService.server.getPacketReader().readMessage();
            List<UserModel> tweetLike = new ArrayList<>();
            String[] likes = tweetLikes.split(" ");
            try{
                runOnUiThread(() ->{
                    for (String like : likes) {
                        if (!like.isEmpty() && !like.equals(" ")) {
                            tweetLike.add(new UserModel("", like));
                        }
                    }
                    TweetModel tweet = new TweetModel(username, tweetMessage, tweetUID, null);
                    tweet.setLikes(tweetLike);
                    DataService.tweetAdapter.addTweet(tweet);
                    DataService.tweetAdapter.notifyDataSetChanged();
                    tweetList.requestLayout();
                });
            }catch (Exception e){
                Log.e("NaberApp", "getTweets: ", e);
            }
        }
    }
    private void deleteTweetEvent(Void unused){
        String tweetUID = DataService.server.getPacketReader().readMessage();
        for (TweetModel tweet : DataService.tweetAdapter.getTweets()){
            if (tweet.getTweetUID().equals(tweetUID)){
                runOnUiThread(() -> {
                    DataService.tweetAdapter.removeTweet(tweet);
                    DataService.tweetAdapter.notifyDataSetChanged();
                    tweetList.requestLayout();
                });
            }
        }
    }
    private void friendRequestEvent(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        UserModel user = new UserModel(username, UUID.randomUUID().toString());
        user.setOwnRequest(false);
        for(UserModel friend : DataService.friendRequests){
            if (friend.getUsername().equals(username)){
                DataService.friendRequests.add(new UserModel(friend.getUsername(), friend.getUID()));
            }
        }

    }
    private void friendRequestCancelEvent(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        for (UserModel friend : DataService.friendRequests){
            if (friend.getUsername().equals(username)){
                DataService.friendRequests.remove(friend);
            }
        }
    }
    private void friendRequestAcceptEvent(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        for (UserModel friend : DataService.friendRequests){
            if (friend.getUsername().equals(username)){
                DataService.friendRequests.remove(friend);
                DataService.friends.add(friend);
            }
        }
    }
    private void friendRequestDeclineEvent(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        for (UserModel friend : DataService.friendRequests){
            if (friend.getUsername().equals(username)){
                DataService.friendRequests.remove(friend);
            }
        }
    }
    private void friendRemoveEvent(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        for (UserModel friend : DataService.friends){
            if (friend.getUsername().equals(username)){
                DataService.friends.remove(friend);
            }
        }
    }
    private void getFriendEvent(Void unused){
        int friendCount = Integer.parseInt(DataService.server.getPacketReader().readMessage());
        try{
            for (int i = 0; i < friendCount; i++){
                String username = DataService.server.getPacketReader().readMessage();
                Boolean ownRequest = Boolean.parseBoolean(DataService.server.getPacketReader().readMessage());
                Boolean state = Boolean.parseBoolean(DataService.server.getPacketReader().readMessage());
                //UserModel user = new UserModel(username, UUID.randomUUID().toString());
                //user.setOwnRequest(ownRequest);
                if (state){
                    //DataService.friends.add(user);
                } else {
                    //DataService.friendRequests.add(user);
                }
            }
        }catch (Exception e){
            Log.e("NaberApp", "getFriendEvent: ", e);
        }

    }

}