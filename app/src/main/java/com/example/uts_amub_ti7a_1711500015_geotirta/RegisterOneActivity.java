package com.example.uts_amub_ti7a_1711500015_geotirta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity {

    Button next;
    EditText username, password, email_Address;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        username = findViewById(R.id.daftaruaser);
        password = findViewById(R.id.daftarpassword);
        email_Address = findViewById(R.id.daftaremail);
        next = findViewById(R.id.bnext);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, username.getText().toString());
                editor.apply();

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username.getText().toString());

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange (@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("usernamme").setValue(username.getText().toString());
                        dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                        dataSnapshot.getRef().child("email_address").setValue(email_Address.getText().toString());
                        dataSnapshot.getRef().child("user_balance").setValue(800);
                    }
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                Intent gotonexregister = new Intent(com.example.uts_amub_ti7a_1711500015_geotirta.RegisterOneActivity.this, com.example.uts_amub_ti7a_1711500015_geotirta.RegisterTwoActivity.class);
                startActivity(gotonexregister);
            }


        });
    }
}