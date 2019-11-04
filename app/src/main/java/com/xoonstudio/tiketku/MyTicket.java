package com.xoonstudio.tiketku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyTicket extends AppCompatActivity {
    RecyclerView all_ticket_item;
    ImageView btn_back;
    DatabaseReference reference;
    ArrayList<TicketItem> list;
    TicketAdapter ticketAdapter;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket);
        // load the element
        all_ticket_item = findViewById(R.id.all_ticket_item);
        btn_back = findViewById(R.id.btn_back);

        //se data
        list = new ArrayList<TicketItem>();
        all_ticket_item.setLayoutManager(new LinearLayoutManager(this));

        // load usetnamelocal
        getUsernameLocal();

        // load data from database
        reference = FirebaseDatabase.getInstance().getReference().child("MyTicket").child(username_value);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    TicketItem a = dataSnapshot1.getValue(TicketItem.class);
                    list.add(a);
                }
                ticketAdapter = new TicketAdapter(MyTicket.this, list);
                all_ticket_item.setAdapter(ticketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtohome = new Intent(MyTicket.this, Home.class);
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
