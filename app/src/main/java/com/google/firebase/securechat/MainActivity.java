package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    String name=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_beta);

        // getSupportActionBar().hide();
        // androidx.appcompat.widget.Toolbar temp=findViewById(R.id.maintoolbar);
        //  setSupportActionBar(temp);

        final FirebaseAuth mauth;
        mauth=FirebaseAuth.getInstance();
        final ProgressBar mprogressbar=findViewById(R.id.progressBar);
        mprogressbar.setVisibility(ProgressBar.INVISIBLE);
        Button login=findViewById(R.id.Login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText email=findViewById(R.id.email2);
                EditText pass=findViewById(R.id.password2);
                String Email=email.getText().toString();
                String Pass=pass.getText().toString();

                if(!Email.isEmpty()&&!Pass.isEmpty()){
                    mprogressbar.setVisibility(ProgressBar.VISIBLE);
                    mauth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Welcome Again", Toast.LENGTH_LONG).show();
                                DatabaseReference ref= FirebaseDatabase.getInstance().getReference(mauth.getUid());
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        name=dataSnapshot.getValue(String.class);
                                        Intent intent=new Intent(getApplicationContext(),preuserActivity.class);
                                        intent.putExtra("receivername2",name);
                                        mprogressbar.setVisibility(ProgressBar.INVISIBLE);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Wrong Email or Password",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "Enter Email and Password Correctly.", Toast.LENGTH_LONG).show();
                }
            }
        });



        if(mauth.getCurrentUser()!=null){
            mprogressbar.setVisibility(ProgressBar.VISIBLE);
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference(mauth.getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    name=dataSnapshot.getValue(String.class);
                    Intent intent=new Intent(getApplicationContext(),preuserActivity.class);
                    intent.putExtra("receivername2",name);
                    mprogressbar.setVisibility(ProgressBar.INVISIBLE);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        TextView newuser=findViewById(R.id.newuser);
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NewUserActivity.class));
            }
        });

    }
}