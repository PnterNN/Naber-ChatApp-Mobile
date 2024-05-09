package com.example.nabermobileproject.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.nabermobileproject.R;
import com.example.nabermobileproject.model.MessageModel;
import com.example.nabermobileproject.model.TweetModel;
import com.example.nabermobileproject.model.UserModel;
import com.example.nabermobileproject.services.DataService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_chat);



    }

}