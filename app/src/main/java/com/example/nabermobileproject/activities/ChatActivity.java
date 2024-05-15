package com.example.nabermobileproject.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nabermobileproject.NET.Server;
import com.example.nabermobileproject.R;
import com.example.nabermobileproject.adapters.ChatAdapter;
import com.example.nabermobileproject.model.MessageModel;
import com.example.nabermobileproject.services.DataService;
import com.example.nabermobileproject.services.ServerManager;

import java.util.HashMap;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {
    ListView chatList;

    EditText message;
    Button send;
    String UID;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_chat);

        DataService.server = ServerManager.getInstance();

        Intent intent = getIntent();
        UID = intent.getStringExtra("UID");

        if (DataService.chatAdapter == null) {
            DataService.chatAdapter = new HashMap<>();
        }

        chatList = findViewById(R.id.chatList);
        chatList.setAdapter(DataService.chatAdapter.get(UID));
        message = findViewById(R.id.messageBox);
        send = findViewById(R.id.sendButton);

        send.setOnClickListener(this::sendMessage);
    }

    private void sendMessage(View view) {
        Random random = new Random();
        int messageUID = random.nextInt((999999999-100000000) + 1) + 100000000;
        runOnUiThread(() -> {
            MessageModel messageModel = new MessageModel(DataService.username, message.getText().toString(), UID, null);
            ServerManager.sendMessage(message.getText().toString(), UID, "True", messageUID + "");
            DataService.chatAdapter.get(UID).addMessage(messageModel);
            message.setText("");
        });


    }
}