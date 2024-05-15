package com.example.nabermobileproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.nabermobileproject.NET.Server;
import com.example.nabermobileproject.R;
import com.example.nabermobileproject.adapters.ChatAdapter;
import com.example.nabermobileproject.adapters.UserAdapter;
import com.example.nabermobileproject.model.MessageModel;
import com.example.nabermobileproject.model.TweetModel;
import com.example.nabermobileproject.model.UserModel;
import com.example.nabermobileproject.services.DataService;
import com.example.nabermobileproject.services.ServerManager;

import java.util.UUID;

public class UserlistActivity extends AppCompatActivity {
    ListView userListView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlist);
        DataService.server = ServerManager.getInstance();

        DataService.server.userConnectedEvent = this::userConnected;
        DataService.server.userRegisterConnectedEvent = this::registerUserConnected;
        DataService.server.messageReceivedEvent = this::messageReceived;
        DataService.server.deleteMessageEvent = this::deleteMessageEvent;
        DataService.server.userDisconnectedEvent = this::userDisconnected;


        userListView = findViewById(R.id.userList);
        DataService.userAdapter = new UserAdapter(this);
        userListView.setAdapter(DataService.userAdapter);
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("UID", DataService.userAdapter.getUsers().get(position).getUID());
            startActivity(intent);
        });
    }



    private void messageReceived(Void unused){
        String message = DataService.server.getPacketReader().readMessage();
        String username = DataService.server.getPacketReader().readMessage();
        String sendedUserUID = DataService.server.getPacketReader().readMessage();
        String messageUID = DataService.server.getPacketReader().readMessage();
        Log.d("NaberApp", "messageReceived: "+message+" "+username+" "+sendedUserUID+" "+messageUID);
        runOnUiThread(() -> {
            UserModel user = DataService.users.stream().filter(x -> x.getUID().equals(sendedUserUID)).findFirst().get();
            if(user != null){
                if(DataService.chatAdapter != null){
                    DataService.chatAdapter.get(sendedUserUID).addMessage(new MessageModel(username, message, messageUID, null));
                    DataService.chatAdapter.get(sendedUserUID).notifyDataSetChanged();
                }

            }
        });

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
}