package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Base64;
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
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

public class ChatActivity extends AppCompatActivity {
     public String UserName;
     String my_privatekey="";
    String rec_publickey="";
    String my_publickey="";
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



        //Toast.makeText(getApplicationContext(),sharedPreferences.getString("privatekey",""),Toast.LENGTH_LONG).show();

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
                        DatabaseReference rec_reference=FirebaseDatabase.getInstance().getReference(str).child("Public Key");

                        rec_reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                rec_publickey=dataSnapshot.getValue(String.class);
                                final String Receiver = str;
                                DatabaseReference my_reference=FirebaseDatabase.getInstance().getReference(UserName).child("Public Key");

                                my_reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                       my_publickey=dataSnapshot.getValue(String.class);
                                        if (mess.length() != 0) {

                                            String encrypted=encryptRSAToString(mess,my_publickey);
                                            DatabaseReference myref1 = FirebaseDatabase.getInstance().getReference(UserName);
                                            SecureMessage Message1 = new SecureMessage(encrypted, UserName, Receiver, 1);
                                            myref1.child("Message").child(str).push().setValue(Message1);
                                            DatabaseReference myref2 = FirebaseDatabase.getInstance().getReference(Receiver);
                                            encrypted=encryptRSAToString(mess,rec_publickey);
                                            SecureMessage Message2 = new SecureMessage(encrypted, UserName, Receiver, 0);
                                            myref2.child("Message").child(UserName).push().setValue(Message2);
                                            mainmessage.setText("");
                                            Toast.makeText(getApplicationContext(), "Sent......", Toast.LENGTH_SHORT).show();


                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error....", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });

                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                ChildEventListener mchildEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                       final SecureMessage ob = dataSnapshot.getValue(SecureMessage.class);
                        DatabaseReference myref1 = FirebaseDatabase.getInstance().getReference(UserName).child("Private Key");
                        myref1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                my_privatekey=dataSnapshot.getValue(String.class);
                                String decrypted = decryptRSAToString(ob.getText(), my_privatekey);
                                Toast.makeText(getApplicationContext(),my_privatekey,Toast.LENGTH_LONG).show();
                                ob.setText(decrypted);
                                mMessageAdapter.add(ob);
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
                };
                FirebaseDatabase nref = FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = nref.getReference(UserName).child("Message").child(str);
                myRef2.addChildEventListener(mchildEventListener);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
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

}