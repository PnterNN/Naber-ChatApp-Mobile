package com.example.nabermobileproject.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.nabermobileproject.R;
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

    Button sendTweetButton;
    Button chatButton;
    ListView tweetList;
    EditText tweetBox;

    private void sendTweet(View v){
        Random random = new Random();
        int tweetUID = random.nextInt((999999999-100000000) + 1) + 100000000;
        ServerManager.sendTweet(tweetBox.getText().toString(), tweetUID + "");
    }
    private void openChat(View v){
        Intent chatIntent = new Intent(this, ChatActivity.class);
        startActivity(chatIntent);
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("NaberApp", "onCreate: TweetActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);

        sendTweetButton = findViewById(R.id.tweetButton);
        chatButton = findViewById(R.id.chatButton);
        tweetList = findViewById(R.id.tweetListView);
        tweetBox = findViewById(R.id.tweetBox);

        sendTweetButton.setOnClickListener(this::sendTweet);
        chatButton.setOnClickListener(this::openChat);

        DataService.server = ServerManager.getInstance();
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
        DataService.server.userConnectedEvent = this::userConnected;
        DataService.server.messageReceivedEvent = this::messageReceived;
        DataService.server.deleteMessageEvent = this::deleteMessageEvent;
        DataService.server.userDisconnectedEvent = this::userDisconnected;
        DataService.server.userRegisterConnectedEvent = this::registerUserConnected;
    }

    private void registerUserConnected(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        String UID = DataService.server.getPacketReader().readMessage();
        //UserModel user = new UserModel(username, UID);
        //user.getMessages().add(new TweetModel(username,"Welcome to the chat!", UUID.randomUUID().toString(), null));
        //DataService.users.add(user);
    }
    private void userConnected(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        String UID = DataService.server.getPacketReader().readMessage();
        int messageCount = Integer.parseInt(DataService.server.getPacketReader().readMessage());
        //UserModel user = new UserModel(username, UID);
        String dataUsername = "";
        String dataUID = "";
        String dataImageSource = "";
        String dataMessage = "";
        String dataVoice = "";
        LocalDateTime dataTime = null;//LocalDateTime.now();
        boolean dataFirstMessage = true;
        String messageUID = "";
        int index2 = 0;
        for (int i = 0; i < messageCount; i++) {
            dataUsername = DataService.server.getPacketReader().readMessage();
            dataUID = DataService.server.getPacketReader().readMessage();
            dataImageSource = DataService.server.getPacketReader().readMessage();
            dataMessage = DataService.server.getPacketReader().readMessage();
            dataVoice = DataService.server.getPacketReader().readMessage();
            dataTime = null;//LocalDateTime.parse(DataService.server.getPacketReader().readMessage());
            dataFirstMessage = Boolean.parseBoolean(DataService.server.getPacketReader().readMessage());
            messageUID = DataService.server.getPacketReader().readMessage();
            Random random = new Random();
            int messageUID2 = Integer.parseInt(messageUID) - random.nextInt(9999);
            //int index = user.getMessages().size();
            if(dataVoice == "0"){
                if(dataUsername!=""){
                    boolean ownMessage = false;
                    if(DataService.username == dataUsername){
                        ownMessage = true;
                    }
                    //user.getMessages().add(new TweetModel(dataUsername, dataMessage, messageUID2 + "", dataTime));
                }else{
                    //user.getMessages().add(new TweetModel("NaberApp", username+" çevrimiçi oldu",  UUID.randomUUID().toString(), dataTime));
                }
            }
        }
    }
    private void messageReceived(Void unused){
        String message = DataService.server.getPacketReader().readMessage();
        String username = DataService.server.getPacketReader().readMessage();
        String sendedUserUID = DataService.server.getPacketReader().readMessage();
        String messageUID = DataService.server.getPacketReader().readMessage();
        //UserModel user = DataService.users.stream().filter(x -> x.getUID().equals(sendedUserUID)).findFirst().get();
        //if(user != null){
        //    user.getMessages().add(new TweetModel(username, message, messageUID, null));
        //}
    }

    private void createGroup(Void unused){
        String groupName = DataService.server.getPacketReader().readMessage();
        String groupUID = DataService.server.getPacketReader().readMessage();
        String[] groupUsers = groupUID.split(" ");
        List<String> usernames = new ArrayList<>();
        for (String user : groupUsers){
            //UserModel user1 = DataService.users.stream().filter(x -> x.getUID().equals(user)).findFirst().get();
            //if(user1 != null){
            //    usernames.add(user1.getUsername());
            //}
        }
        //UserModel group = new UserModel(groupName, groupUID);
        //DataService.users.add(group);
        //group.getMessages().add(new TweetModel("NaberApp", "Grup oluşturuldu", UUID.randomUUID().toString(), null));
    }

    private void userDisconnected(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        //UserModel user = DataService.users.stream().filter(x -> x.getUsername().equals(username)).findFirst().get();
        //if(user != null){
        //    user.getMessages().add(new TweetModel("NaberApp", username+" çevrimdışı oldu", UUID.randomUUID().toString(), null));
         //   DataService.users.remove(user);
        //}
    }
    private void deleteMessageEvent(Void unused){
        String contactUID = DataService.server.getPacketReader().readMessage();
        String messageUID = DataService.server.getPacketReader().readMessage();
        for (UserModel user : DataService.users){
            if (user.getUID().equals(contactUID)){
                for (MessageModel message : user.getMessages()){
                    if (message.getUID().equals(messageUID)){
                        user.getMessages().remove(message);
                    }
                }
            }
        }
    }

    private void tweetReceivedEvent(Void unused){
        String username = DataService.server.getPacketReader().readMessage();
        String tweetMessage = DataService.server.getPacketReader().readMessage();
        String tweetUID = DataService.server.getPacketReader().readMessage();
        //TweetModel tweet = new TweetModel(username, tweetMessage, tweetUID, null);
        //DataService.tweets.add(tweet);
    }
    private void likeEvent(Void unused){
        String userUID = DataService.server.getPacketReader().readMessage();
        String tweetUID = DataService.server.getPacketReader().readMessage();
        for (TweetModel tweet : DataService.tweets){
            if (tweet.getTweetUID().equals(tweetUID)){
                tweet.addLike(new UserModel("", userUID));
            }
        }
    }
    private void getTweets(Void unused){
        int tweetCount = Integer.parseInt(DataService.server.getPacketReader().readMessage());
        for (int i = 0; i < tweetCount; i++){
            String username = DataService.server.getPacketReader().readMessage();
            String tweetUID = DataService.server.getPacketReader().readMessage();
            String tweetImage = DataService.server.getPacketReader().readMessage();
            String tweetMessage = DataService.server.getPacketReader().readMessage();
            String tweetLikes = DataService.server.getPacketReader().readMessage();
            String tweetTime = DataService.server.getPacketReader().readMessage();
            //List<UserModel> tweetLike = new ArrayList<>();
            String[] likes = tweetLikes.split(" ");
            for (String like : likes){
                if(like != "" || like != " " || like != null){
                    //tweetLike.add(new UserModel("", like));
                }
            }
            //TweetModel tweet = new TweetModel(username, tweetMessage, tweetUID, null);
            //tweet.setLikes(tweetLike);
            //DataService.tweets.add(tweet);
        }
    }
    private void deleteTweetEvent(Void unused){
        String tweetUID = DataService.server.getPacketReader().readMessage();
        for (TweetModel tweet : DataService.tweets){
            if (tweet.getTweetUID().equals(tweetUID)){
                DataService.tweets.remove(tweet);
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
    }

}