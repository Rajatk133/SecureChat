package com.google.firebase.securechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        Button Submit=findViewById(R.id.Submit);

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
                myref.child("Last Name").setValue(Lname.getText().toString());
                myref.child("Phone").setValue(Phone.getText().toString());
                myref.child("Messsages");

                FirebaseAuth mAuth=FirebaseAuth.getInstance();

                myref=database.getReference(mAuth.getUid());
                myref.setValue(Fname.getText().toString()+" "+Lname.getText().toString());


                Intent intent = new Intent(getApplicationContext(),preuserActivity.class);
                intent.putExtra("receivername",username);
                startActivity(intent);



            }
        });




    }
}