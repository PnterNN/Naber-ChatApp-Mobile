package com.example.nabermobileproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nabermobileproject.R;
import com.example.nabermobileproject.services.DataService;
import com.example.nabermobileproject.services.ServerManager;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    Button registerButton;
    EditText emailBox;
    EditText passwordBox;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        Log.d("NaberApp", "Login başarılı");
        Intent tweetIntent = new Intent(this, TweetActivity.class);
        tweetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tweetIntent);

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