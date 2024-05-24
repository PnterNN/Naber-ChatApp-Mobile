package com.example.nabermobileproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nabermobileproject.R;
import com.example.nabermobileproject.model.MessageModel;
import com.example.nabermobileproject.model.UserModel;
import com.example.nabermobileproject.services.DataService;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private String UID;
    private UserModel currentUser;

    public ChatAdapter(Context context, String UID) {
        this.context = context;
        this.UID = UID;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentUser = DataService.getUser(UID);
    }

    public void addMessage(MessageModel message) {
        for (MessageModel messageModel : currentUser.getMessages()) {
            if (messageModel.getUID().equals(message.getUID())) {
                return;
            }
        }
        if (currentUser != null) {
            currentUser.getMessages().add(message);
            notifyDataSetChanged();
        }
    }
    public void clearMessages(){
        if(currentUser != null){
            currentUser.getMessages().clear();
            notifyDataSetChanged();
        }
    }
    public ArrayList<MessageModel> getMessages(){
        return currentUser.getMessages();
    }

    public void removeMessage(MessageModel message) {
        if (currentUser != null) {
            currentUser.getMessages().remove(message);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (currentUser == null || currentUser.getMessages() == null) {
            return 0;
        }
        return currentUser.getMessages().size();
    }

    @Override
    public Object getItem(int position) {
        if (currentUser != null && currentUser.getMessages() != null && position < currentUser.getMessages().size()) {
            return currentUser.getMessages().get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = layoutInflater.inflate(R.layout.chatitem, null);
        TextView name = customView.findViewById(R.id.username);
        TextView message = customView.findViewById(R.id.message);

        if (currentUser != null && currentUser.getMessages() != null) {
            if (position < currentUser.getMessages().size()) {
                name.setText(currentUser.getUsername());
                message.setText(currentUser.getMessages().get(position).getMessage());
            } else {
                message.setText("");
            }
        }

        return customView;
    }
}