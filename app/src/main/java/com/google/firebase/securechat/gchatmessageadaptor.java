package com.google.firebase.securechat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
public class gchatmessageadaptor extends ArrayAdapter<groupmessage> {
    String Username="";
    public gchatmessageadaptor(@NonNull Context context, int resource, @NonNull List<groupmessage> objects,String a) {
        super(context, resource, objects);
        Username=a;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.groupmessagelayout, parent, false);


        TextView message = convertView.findViewById(R.id.messageTextView);
        TextView username = convertView.findViewById(R.id.nameTextView);
        LinearLayout mainlayout=(LinearLayout) convertView.findViewById(R.id.mainlayout);
        LinearLayout inner=(LinearLayout) convertView.findViewById(R.id.inner);
        groupmessage details = getItem(position);
        username.setText(details.getUsername());
        message.setText(details.getMessage());
        if(details.getUsername().compareTo(Username)==0){
            mainlayout.setScaleX(-1);
            inner.setScaleX(-1);
        }else{
            mainlayout.setScaleX(1);
            inner.setScaleX(1);
        }
        return convertView;
    }
}
