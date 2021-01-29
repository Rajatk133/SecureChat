package com.google.firebase.securechat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class userInfoAdapter extends ArrayAdapter<userInfo> {
    public userInfoAdapter(@NonNull Context context, int resource, @NonNull List<userInfo> objects) {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.username, parent, false);

        TextView username = convertView.findViewById(R.id.username);

        userInfo details = getItem(position);

        username.setText(details.getName());
        return convertView;
    }


}
