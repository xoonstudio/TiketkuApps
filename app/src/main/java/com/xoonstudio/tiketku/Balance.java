package com.xoonstudio.tiketku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Balance extends AppCompatActivity {
    ImageView btn_back;
    TextView xuser_balance;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        // load the element
        btn_back = findViewById(R.id.btn_back);
        xuser_balance = findViewById(R.id.xuser_balance);

        // load user local
        getUsernameLocal();

        // load content
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_value);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xuser_balance.setText("$ " + dataSnapshot.child("user_balance").getValue().toString() + ".00");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtohome =  new Intent(Balance.this, Home.class);
                startActivity(backtohome);
                finish();
            }
        });
    }

    // get username local
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_value = sharedPreferences.getString(username_key, "");
    }
}
