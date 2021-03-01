package com.google.firebase.securechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button Submit=findViewById(R.id.Submit);
        /******************************Key pair Generation**********************/
        KeyPair kp = getKeyPair();
        PublicKey publicKey = kp.getPublic();
        final byte[] publicKeyBytes = publicKey.getEncoded();
        final String publicKeyBytesBase64 = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));

        PrivateKey privateKey = kp.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();
        final String privateKeyBytesBase64 = new String(Base64.encode(privateKeyBytes, Base64.DEFAULT));


       //Toast.makeText(getApplicationContext(),sharedPreferences.getString("privatekey",""),Toast.LENGTH_LONG).show();
       // Toast.makeText(getApplicationContext(),sharedPreferences.getString("publickey",""),Toast.LENGTH_LONG).show();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText Fname=findViewById(R.id.FirstName);
                EditText Lname=findViewById(R.id.LastName);
                EditText Phone=findViewById(R.id.phone);
                EditText Email=findViewById(R.id.email2);

                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myref=database.getReference(Fname.getText().toString()+" "+Lname.getText().toString());

                DatabaseReference newref=database.getReference("Users");
                String username=Fname.getText().toString()+" "+Lname.getText().toString();
                newref.push().setValue(new userInfo(Fname.getText().toString()+" "+Lname.getText().toString()));

                myref.child("First Name").setValue(Fname.getText().toString());
                myref.child("Private Key").setValue(privateKeyBytesBase64);
                myref.child("Public Key").setValue(publicKeyBytesBase64);


                myref.child("Last Name").setValue(Lname.getText().toString());
                myref.child("Phone").setValue(Phone.getText().toString());
                myref.child("GroupUsers");


                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                myref=database.getReference(mAuth.getUid());
                myref.setValue(Fname.getText().toString()+" "+Lname.getText().toString());


              Intent intent = new Intent(RegisterActivity.this,preuserActivity.class);
                intent.putExtra("receivername2",username);
                startActivity(intent);
            }
        });
    }
    public static KeyPair getKeyPair() {
        KeyPair kp = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            kp = kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kp;
    }

}