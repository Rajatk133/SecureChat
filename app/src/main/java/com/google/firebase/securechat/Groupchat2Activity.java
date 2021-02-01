package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Groupchat2Activity extends AppCompatActivity {
    String username,groupname;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat2);

        ListView messageListView=findViewById(R.id.messageListView);
        Intent intent = getIntent();

        groupname = intent.getStringExtra("groupname");
        getSupportActionBar().setTitle(groupname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        username=intent.getStringExtra("Username");

        ArrayList<groupmessage> gmlist = new ArrayList<>();
        final gchatmessageadaptor gMessageAdaptor = new gchatmessageadaptor(Groupchat2Activity.this, R.layout.groupmessagelayout, gmlist,username);
        messageListView.setAdapter(gMessageAdaptor);


        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference();
        Query query=ref.child("Groups").orderByChild("groupname").equalTo(groupname);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String key=null;
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        key=postSnapshot.getKey();}
                        ImageView send=findViewById(R.id.sendButton);
                   final DatabaseReference nref=FirebaseDatabase.getInstance().getReference().child("Groups").child(key);
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EditText mess = findViewById(R.id.messageEditText);
                                if(!mess.getText().toString().isEmpty()){
                                nref.child("Messages").push().setValue(new groupmessage(username, mess.getText().toString()));}
                                else{
                                    Toast.makeText(getApplicationContext(),"Please Type Message..",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String key="";
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {key=postSnapshot.getKey();}

                    ref.child("Groups").child(key).child("Messages").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String mess1=dataSnapshot.child("message").getValue(String.class);
                            String username1=dataSnapshot.child("username").getValue(String.class);
                            gMessageAdaptor.add(new groupmessage(username1,mess1));
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
}