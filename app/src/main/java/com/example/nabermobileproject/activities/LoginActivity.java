package com.example.nabermobileproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nabermobileproject.NET.Server;
import com.example.nabermobileproject.R;
import com.example.nabermobileproject.adapters.UserAdapter;
import com.example.nabermobileproject.services.DataService;
import com.example.nabermobileproject.services.ServerManager;

import java.util.ArrayList;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    Button registerButton;
    EditText emailBox;
    EditText passwordBox;
    Server server;

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Naber Channel";
            String description = "Channel for Naber app notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("naber_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createNotificationChannel();
        DataService.users = new ArrayList<>();
        DataService.tweets = new ArrayList<>();
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        DataService.server = ServerManager.getInstance();
        DataService.server.loginCorrectEvent = this::loginCorrect;
        DataService.server.loginFailedEvent = this::loginFailed;
        DataService.server.tooManyPacketsEvent = this::tooManyPackets;

        loginButton.setOnClickListener(this::onLogin);
        registerButton.setOnClickListener(this::onSignup);
    }

    private void tooManyPackets(Void unused){
        loginButton.setEnabled(false);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            runOnUiThread(() -> {
                loginButton.setEnabled(true);
            });
        }, 10000);
    }

    private void loginCorrect(Void unused){

        DataService.server = ServerManager.getInstance();
        server = DataService.server;
        DataService.users = new ArrayList<>();
        DataService.tweets = new ArrayList<>();
        DataService.friends = new ArrayList<>();
        DataService.friendRequests = new ArrayList<>();
        DataService.chatAdapter = new HashMap<>();
        DataService.userAdapter = new UserAdapter(this);

        Intent tweetIntent = new Intent(this, TweetActivity.class);
        startActivity(tweetIntent);
        finish();
    }
    private void loginFailed(Void unused){
        runOnUiThread(() -> {
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);
            emailBox.setEnabled(true);
            passwordBox.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
        });
    }

    protected void onLogin(View v){
        try {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
            emailBox.setEnabled(false);
            passwordBox.setEnabled(false);
            ServerManager.loginServer(emailBox.getText().toString(), passwordBox.getText().toString());
        } catch (Exception e) {
            Log.d("NaberApp", "Sunucu bulunamadı uygulama kapanıyor..." + e);
            System.exit(1);
        }
    }
    protected void onSignup(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}