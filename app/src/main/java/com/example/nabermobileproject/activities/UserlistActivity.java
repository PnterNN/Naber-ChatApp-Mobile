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
        userListView = findViewById(R.id.userList);
        userListView.setAdapter(DataService.userAdapter);
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("UID", DataService.userAdapter.getUsers().get(position).getUID());
            startActivity(intent);
        });
    }
}