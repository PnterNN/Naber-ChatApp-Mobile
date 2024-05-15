package com.example.nabermobileproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nabermobileproject.R;
import com.example.nabermobileproject.model.TweetModel;
import com.example.nabermobileproject.model.UserModel;
import com.example.nabermobileproject.services.DataService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TweetAdapter extends BaseAdapter {

    public ArrayList<TweetModel> getTweets() {
        return DataService.tweets;
    }
    private Context context;
    private LayoutInflater layoutInflater;

    public TweetAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void addTweet(TweetModel tweet) {
        DataService.tweets.add(tweet);
    }
    public void removeTweet(TweetModel tweet) {
        DataService.tweets.remove(tweet);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(DataService.tweets == null)
            return 0;
        return DataService.tweets.size();
    }

    @Override
    public Object getItem(int position) {
        return DataService.tweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = layoutInflater.inflate(R.layout.tweetitem,null);
        TextView name = (TextView) customView.findViewById(R.id.tweetUsername);
        TextView genus = (TextView) customView.findViewById(R.id.tweetMessage);

        name.setText(DataService.tweets.get(position).getUsername());
        genus.setText(DataService.tweets.get(position).getTweetMessage());

        return customView;
    }
}
