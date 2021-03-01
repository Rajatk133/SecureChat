package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        Intent i=getIntent();
        String name=i.getStringExtra("user");
        TextView user=findViewById(R.id.Username);
        user.setText(name);

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference(name);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Email=dataSnapshot.child("Email").getValue(String.class);
                String Phone=dataSnapshot.child("Phone").getValue(String.class);
                String Status=dataSnapshot.child("Status").getValue(String.class);
                TextView email=findViewById(R.id.email3);
                email.setText(Email);
                TextView status=findViewById(R.id.status);
                status.setText(Status);
                TextView phone=findViewById(R.id.phone);
                phone.setText(Phone);

                ImageView editphone=findViewById(R.id.editphone);
                editphone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder= new AlertDialog.Builder(UpdateProfile.this);
                        builder.setTitle("New Status");
                        final View customLayout = getLayoutInflater().inflate(R.layout.adduser, null);
                        builder.setView(customLayout);
                        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final EditText editText = customLayout.findViewById(R.id.userid);
                                String data=editText.getText().toString();
                                reference.child("Phone").setValue(data);
                            }
                        });
                        AlertDialog dialog = builder.create();dialog.show();
                    }
                });
                ImageView editstatus=findViewById(R.id.editstatus);
                editstatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder= new AlertDialog.Builder(UpdateProfile.this);
                        builder.setTitle("New Phone");
                        final View customLayout = getLayoutInflater().inflate(R.layout.adduser, null);
                        builder.setView(customLayout);
                        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final EditText editText = customLayout.findViewById(R.id.userid);
                                String data=editText.getText().toString();
                                reference.child("Status").setValue(data);
                            }
                        });
                        AlertDialog dialog = builder.create();dialog.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
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