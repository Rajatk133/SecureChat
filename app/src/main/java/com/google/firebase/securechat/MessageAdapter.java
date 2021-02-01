package com.google.firebase.securechat;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class MessageAdapter extends ArrayAdapter<SecureMessage> {

    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<SecureMessage> objects) {
        super(context, resource, objects);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.message_layout, parent, false);
        }
        TextView message = convertView.findViewById(R.id.message);

        LinearLayout m1=convertView.findViewById(R.id.m1);



        SecureMessage message1 = getItem(position);
        if(message1.getOid()==1){
            message.setText(message1.getText());
            m1.setScaleX(-1);
            message.setScaleX(-1);
        }
        else{
            message.setText(message1.getText());
            message.setScaleX(1);
            m1.setScaleX(1);
        }
        return convertView;
    }
}

