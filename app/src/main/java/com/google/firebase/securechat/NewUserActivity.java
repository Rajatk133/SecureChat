package com.google.firebase.securechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Button Signup = findViewById(R.id.signup);


        Signup.setOnClickListener(new View.OnClickListener() {
            FirebaseAuth mAuth;

            @Override
            public void onClick(View view) {

                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);
                EditText repassword = findViewById(R.id.repassword);

                String Email = email.getText().toString();
                String Pass = password.getText().toString();
                String rePass = repassword.getText().toString();

                if (!Email.isEmpty() || !Pass.isEmpty() || !rePass.isEmpty()) {

                    if (Pass.compareTo(rePass) == 0) {
                        mAuth = FirebaseAuth.getInstance();


                        mAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(NewUserActivity.this, "Enter The Basic Details", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(NewUserActivity.this, RegisterActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(NewUserActivity.this, "User Already Existed!!!!!!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(NewUserActivity.this, "Password Doesn't Match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(NewUserActivity.this, "Please Enter Details Correctly", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}