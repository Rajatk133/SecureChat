package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;

import android.widget.EditText;

import android.widget.ImageView;
import android.widget.ListView;

import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
     public String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        final String str = intent.getStringExtra("receivername");
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();

       getSupportActionBar().setTitle(str);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference(mAuth.getUid());
        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        database.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserName = dataSnapshot.getValue(String.class);

                ListView messagelist = findViewById(R.id.messagelist);

                List<SecureMessage> secureMessages = new ArrayList<>();
                final MessageAdapter mMessageAdapter = new MessageAdapter(ChatActivity.this, R.layout.message_layout, secureMessages);
                messagelist.setAdapter(mMessageAdapter);


                final EditText mainmessage = findViewById(R.id.editmessage);


                ImageView send = findViewById(R.id.send);

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String mess = mainmessage.getText().toString();
                        final String Receiver = str;
                        if (mess.length() != 0) {
                            DatabaseReference myref1 = FirebaseDatabase.getInstance().getReference(UserName);
                            SecureMessage Message1 = new SecureMessage(mess, UserName,Receiver,1);

                            myref1.child("Message").child(str).push().setValue(Message1);
                            DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference(Receiver);

                            SecureMessage Message2 = new SecureMessage(mess, UserName,Receiver,0);
                            myref2.child("Message").child(UserName).push().setValue(Message2);
                            mainmessage.setText("");
                            Toast.makeText(getApplicationContext(), "Sent......", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error....", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                ChildEventListener mchildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        SecureMessage ob = dataSnapshot.getValue(SecureMessage.class);
                        mMessageAdapter.add(ob);
                    }


                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                };
                FirebaseDatabase nref = FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = nref.getReference(UserName).child("Message").child(str);
                myRef2.addChildEventListener(mchildEventListener);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }
}