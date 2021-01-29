package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class preuserActivity extends AppCompatActivity {
    String username=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preuser);

        ProgressBar mprogressbar=findViewById(R.id.progressBar);
        ImageView sc=findViewById(R.id.sc);
        ImageView gc=findViewById(R.id.gc);

        Intent intent = getIntent();
        username = intent.getStringExtra("receivername");
        username = intent.getStringExtra("receivername2");
        Toast.makeText(getApplicationContext(),username,Toast.LENGTH_LONG).show();
        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                intent.putExtra("receivername2",username);
                startActivity(intent);
            }
        });
        gc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupChatActivity.class);
                intent.putExtra("receivername",username);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.usermenu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selected=item.getItemId();
        if(selected==R.id.action_logout){
            FirebaseAuth mAuth=FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                Toast.makeText(getApplicationContext(), "Bye Bye" + user.getEmail(), Toast.LENGTH_LONG).show();
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }
        return true;
    }
}