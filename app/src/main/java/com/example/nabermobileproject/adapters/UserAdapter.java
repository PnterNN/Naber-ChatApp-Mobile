package com.example.nabermobileproject.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nabermobileproject.R;
import com.example.nabermobileproject.model.UserModel;
import com.example.nabermobileproject.services.DataService;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    public ArrayList<UserModel> getUsers() {
        return DataService.users;
    }
    private Context context;
    private LayoutInflater layoutInflater;

    public UserAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addUser(UserModel user) {
        if(DataService.users == null)
            DataService.users = new ArrayList<>();
        if(DataService.users.contains(user))
            return;
        DataService.users.add(user);
        notifyDataSetChanged();
    }
    public void removeUser(UserModel user) {
        DataService.users.remove(user);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(DataService.users == null)
            return 0;
        return DataService.users.size();
    }

    @Override
    public Object getItem(int position) {
        return DataService.users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = layoutInflater.inflate(R.layout.useritem,null);
        TextView name = (TextView) customView.findViewById(R.id.username);
        TextView lastActivity = (TextView) customView.findViewById(R.id.message);

        name.setText(DataService.users.get(position).getUsername());
        if(DataService.users.get(position).getMessages().size() > 0){
            lastActivity.setText(DataService.users.get(position).getMessages().get(DataService.users.get(position).getMessages().size()-1).getMessage());
        }else{
            lastActivity.setText("No activity");
        }

        return customView;
    }

}
