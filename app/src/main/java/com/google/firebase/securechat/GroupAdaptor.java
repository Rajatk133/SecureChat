package com.google.firebase.securechat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.google.firebase.securechat.R;
import java.util.List;

public class GroupAdaptor extends ArrayAdapter<groupNameInfo> {

    public GroupAdaptor(@NonNull Context context, int resource, @NonNull List<groupNameInfo> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.groupusers, parent, false);
        }

        TextView Groupname = (TextView) convertView.findViewById(R.id.gname);
        TextView userno = (TextView) convertView.findViewById(R.id.userno);

         TextView admin=convertView.findViewById(R.id.adminname);

         groupNameInfo  info = getItem(position);
         Groupname.setText(info.getGroupname());
         userno.setText(info.UesrsNo);
         admin.setText(info.getAdmin());


        return convertView;
    }
}
