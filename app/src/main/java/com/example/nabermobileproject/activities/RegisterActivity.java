package com.example.nabermobileproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nabermobileproject.R;
import com.example.nabermobileproject.services.DataService;
import com.example.nabermobileproject.services.ServerManager;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Button loginButton;
    Button registerButton;
    EditText usernameBox;
    EditText emailBox;
    EditText passwordBox;


    @Override
    protected void onRestart() {
        super.onRestart();
        DataService.server.registerSuccessEvent = this::registerSuccessEvent;
        DataService.server.registerFailEvent = this::registerFailEvent;
        DataService.server.tooManyPacketsEvent = this::tooManyPackets;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        usernameBox = findViewById(R.id.usernameBox);
        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);

        registerButton.setOnClickListener(this::onSignup);
        loginButton.setOnClickListener(this::onLogin);

        DataService.server.registerSuccessEvent = this::registerSuccessEvent;
        DataService.server.registerFailEvent = this::registerFailEvent;
        DataService.server.tooManyPacketsEvent = this::tooManyPackets;
    }

    private void tooManyPackets(Void unused){
        loginButton.setEnabled(false);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            loginButton.setEnabled(true);
        }, 10000);
    }

    private void registerSuccessEvent(Void unused){

        DataService.server = ServerManager.getInstance();

        DataService.users = new ArrayList<>();
        DataService.tweets = new ArrayList<>();
        DataService.friends = new ArrayList<>();
        DataService.friendRequests = new ArrayList<>();
        DataService.chatAdapter = new HashMap<>();

        Intent tweetIntent = new Intent(this, TweetActivity.class);
        startActivity(tweetIntent);
        finish();
    }
    private void registerFailEvent(Void unused){
        runOnUiThread(() -> {
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);
            usernameBox.setEnabled(true);
            emailBox.setEnabled(true);
            passwordBox.setEnabled(true);
            Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_LONG).show();
        });
    }

    protected void onSignup(View v){
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);
        usernameBox.setEnabled(false);
        emailBox.setEnabled(false);
        passwordBox.setEnabled(false);
        ServerManager.registerServer(usernameBox.getText().toString(), emailBox.getText().toString(), passwordBox.getText().toString());
    }
    protected void onLogin(View v){
        getOnBackPressedDispatcher().onBackPressed();
    }
}