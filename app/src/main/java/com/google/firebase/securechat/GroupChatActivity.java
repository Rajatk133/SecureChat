package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {
   HashSet<String> map=new HashSet<>();
   String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        Intent intent = getIntent();
        username = intent.getStringExtra("receivername");
        getSupportActionBar().setTitle("Groups");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

         ArrayList<groupNameInfo> list=new ArrayList<>();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = rootRef.child(username).child("Groups");


        ListView ginfoListView = findViewById(R.id.glist);
        final GroupAdaptor adaptor = new GroupAdaptor(GroupChatActivity.this, R.layout.groupusers, list);
        ginfoListView.setAdapter(adaptor);


       ChildEventListener mchildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("groupname").getValue(String.class);
                String Num=dataSnapshot.child("uesrsNo").getValue(String.class);
                String amin=dataSnapshot.child("admin").getValue(String.class);
                groupNameInfo ob = new groupNameInfo(name,Num,amin);
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


        ginfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView info = ((LinearLayout) view).findViewById(R.id.gname);
                Intent intent = new Intent(getApplicationContext(), Groupchat2Activity.class);
                intent.putExtra("groupname", info.getText());
                intent.putExtra("Username",username);
                startActivity(intent);
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groupmenun,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int selected=item.getItemId();
        if(selected==R.id.joingroup){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("Enter Group Name:");
            final View customLayout = getLayoutInflater().inflate(R.layout.adduser, null);
            builder.setView(customLayout);
            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final EditText editText = customLayout.findViewById(R.id.userid);
                    final DatabaseReference ref= FirebaseDatabase.getInstance().getReference(username);
                    final DatabaseReference nref=FirebaseDatabase.getInstance().getReference();
                    Query query=nref.child("Groups").orderByChild("groupname").equalTo(editText.getText().toString());


                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                if(!map.contains(editText.getText().toString())){
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        String ob = postSnapshot.child("admin").getValue(String.class);
                                        String num = postSnapshot.child("uesrsNo").getValue(String.class);
                                        ref.child("Groups").push().setValue(new groupNameInfo(editText.getText().toString(), String.valueOf(Integer.valueOf(num) + 1), ob));
                                        nref.child("Groups").child(postSnapshot.getKey()).child("uesrsNo").setValue(String.valueOf(Integer.valueOf(num) + 1));
                                        map.add(editText.getText().toString());
                                        Toast.makeText(getApplicationContext(), "Adding Please Wait....", Toast.LENGTH_LONG).show();
                                    }

                               }
                                Toast.makeText(getApplicationContext(),"Error...",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Group Doesn't Exist",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            });
            AlertDialog dialog = builder.create();dialog.show();
        }else if(selected==R.id.creategroup){
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("Enter Group Name:");
            final View customLayout = getLayoutInflater().inflate(R.layout.adduser, null);
            builder.setView(customLayout);
            builder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final EditText editText = customLayout.findViewById(R.id.userid);
                    final DatabaseReference ref= FirebaseDatabase.getInstance().getReference(username);
                    final DatabaseReference nref=FirebaseDatabase.getInstance().getReference();
                    Query query=nref.child("Groups").orderByChild("groupname").equalTo(editText.getText().toString());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                if(!map.contains(editText.getText().toString())){
                                    String gname=editText.getText().toString();
                                    ref.child("Groups").push().setValue(new groupNameInfo(editText.getText().toString(),"1",username));
                                    nref.child("Groups").push().setValue(new groupNameInfo(gname,"1",username));
                                    map.add(editText.getText().toString());
                                    Toast.makeText(getApplicationContext(),"Creating Please Wait....",Toast.LENGTH_LONG).show();}

                                Toast.makeText(getApplicationContext(),"Error...",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Group Already Exist",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });
                }
            });
            AlertDialog dialog = builder.create();dialog.show();
        }else if(selected==android.R.id.home){
                    finish();
                }
                return true;
    }

}