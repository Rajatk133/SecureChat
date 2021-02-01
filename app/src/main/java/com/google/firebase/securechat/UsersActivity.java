package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class UsersActivity extends AppCompatActivity {
    ArrayList<userInfo> users=new ArrayList<>();
   String username=null;
   HashSet<String> map=new HashSet<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        Intent intent = getIntent();
        username = intent.getStringExtra("receivername");
        username = intent.getStringExtra("receivername2");
        map.add(username);
        Toast.makeText(getApplicationContext(), username, Toast.LENGTH_LONG).show();


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = rootRef.child(username).child("Chat Users");


        ListView userinfoListView = findViewById(R.id.list);
        final userInfoAdapter adaptor = new userInfoAdapter(UsersActivity.this, R.layout.username, users);
        userinfoListView.setAdapter(adaptor);


        ChildEventListener mchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("name").getValue(String.class);
                userInfo ob = new userInfo(name);
                adaptor.add(ob);
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
        ref.addChildEventListener(mchildEventListener);


        userinfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView info = ((LinearLayout) view).findViewById(R.id.username);
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("receivername", info.getText());
                startActivity(intent);
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
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
        }else if(selected==R.id.action_add){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("User Name");
            final View customLayout = getLayoutInflater().inflate(R.layout.adduser, null);
            builder.setView(customLayout);
            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final EditText editText = customLayout.findViewById(R.id.userid);
                                    final DatabaseReference ref=FirebaseDatabase.getInstance().getReference(username);
                                    DatabaseReference nref=FirebaseDatabase.getInstance().getReference();
                                    Query query=nref.child("Users").orderByChild("name").equalTo(editText.getText().toString());

                                    query.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                if(!map.contains(editText.getText().toString())){
                                                ref.child("Chat Users").push().setValue(new userInfo(editText.getText().toString()));
                                                map.add(editText.getText().toString());
                                                Toast.makeText(getApplicationContext(),"Adding Please Wait....",Toast.LENGTH_LONG).show();}
                                                Toast.makeText(getApplicationContext(),"Error...",Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(),"User Doesn't Exist",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    });
                                }
                            });
            AlertDialog dialog = builder.create();dialog.show();
        }
        return true;
    }
}