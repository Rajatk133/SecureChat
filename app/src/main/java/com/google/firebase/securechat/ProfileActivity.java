package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent=getIntent();
        String username=intent.getStringExtra("receivername");

        getSupportActionBar().setTitle(username);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference(username);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Fname=dataSnapshot.child("First Name").getValue(String.class);
                    String Lname=dataSnapshot.child("Last Name").getValue(String.class);
                    String Phone=dataSnapshot.child("Phone").getValue(String.class);
                    String Email=dataSnapshot.child("Email").getValue(String.class);
                    String Status=dataSnapshot.child("Status").getValue(String.class);
                TextView name=findViewById(R.id.name);
                name.setText(Fname+" "+Lname);
                TextView phone=findViewById(R.id.phone);
                phone.setText(Phone);
                TextView email=findViewById(R.id.email);
                email.setText(Email);
                TextView status=findViewById(R.id.status);
                if(!Status.equals("NA")){
                    status.setText(Status);
                }else{
                    status.setText("HEY I AM NEW TO SECURE CHAT");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return true;
    }
}