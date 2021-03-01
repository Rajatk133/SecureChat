package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
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

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

public class Groupchat2Activity extends AppCompatActivity {
    String username,groupname;
    String key;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat2);

        ListView messageListView=findViewById(R.id.messageListView);
        Intent intent = getIntent();

        groupname = intent.getStringExtra("groupname");
        getSupportActionBar().setTitle(groupname);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    final ImageView send=findViewById(R.id.sendButton);
                   final DatabaseReference nref=FirebaseDatabase.getInstance().getReference().child("Groups").child(key);
                        send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final EditText mess = findViewById(R.id.messageEditText);

                                myref=FirebaseDatabase.getInstance().getReference().child(username);
                                myref.child("Public Key").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(!mess.getText().toString().isEmpty()){
                                            String encryted=encryptRSAToString(mess.getText().toString(),dataSnapshot.getValue(String.class));
                                            nref.child("Messages").push().setValue(new groupmessage(username, encryted));
                                            mess.setText("");
                                            Toast.makeText(getApplicationContext(),"Sent......",Toast.LENGTH_LONG).show();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Please Type Message..",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });


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
                            final String mess1=dataSnapshot.child("message").getValue(String.class);
                            final String username1=dataSnapshot.child("username").getValue(String.class);
                            DatabaseReference myref2=FirebaseDatabase.getInstance().getReference().child(username1).child("Private Key");
                            myref2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String decryptmessage=decryptRSAToString(mess1,dataSnapshot.getValue(String.class));
                                    gMessageAdaptor.add(new groupmessage(username1,decryptmessage));
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


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
    public static String encryptRSAToString(String clearText, String publicKey) {
        String encryptedBase64 = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePublic(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(clearText.getBytes("UTF-8"));
            encryptedBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedBase64.replaceAll("(\\r|\\n)", "");//new line or carriage return replace kar and send kr
    }

    public static String decryptRSAToString(String encryptedBase64, String privateKey) {

        String decryptedString = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePrivate(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedString;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home) {
            finish();
        }else if(id==R.id.aboutg){

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groupinfo,menu);
        return true;
    }
}