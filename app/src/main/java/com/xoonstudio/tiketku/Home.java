package com.xoonstudio.tiketku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.RoundRectView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {
    TextView greeting;
    ImageView btn_profile;
    LinearLayout menu_balance, menu_ticket;
    RoundRectView item_pisa, item_borobudur, item_monas, item_sphinx, item_pagoda, item_torii;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // load the element
        greeting = findViewById(R.id.greeting);
        btn_profile = findViewById(R.id.btn_profile);
        menu_balance = findViewById(R.id.menu_balance);
        menu_ticket = findViewById(R.id.menu_ticket);
        item_pisa = findViewById(R.id.item_pisa);
        item_borobudur = findViewById(R.id.item_borobudur);
        item_monas = findViewById(R.id.item_monas);
        item_sphinx = findViewById(R.id.item_sphinx);
        item_pagoda = findViewById(R.id.item_pagoda);
        item_torii = findViewById(R.id.item_torii);

        // load usernamelocal
        getUsernameLocal();

        // set value text
        greeting.setText("Hai " + username_value);

        // load content
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_value);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(Home.this).load((String) dataSnapshot.child("url_photo_profile").getValue()).centerCrop().fit().into(btn_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // event
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyprofile = new Intent(Home.this, MyProfile.class);
                startActivity(gotomyprofile);
                finish();
            }
        });
        menu_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotobalance = new Intent(Home.this, Balance.class);
                startActivity(gotobalance);
                finish();
            }
        });
        menu_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicket = new Intent(Home.this, MyTicket.class);
                startActivity(gototicket);
                finish();
            }
        });
        item_pisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicketdetail = new Intent(Home.this,TicketDetail.class);
                gototicketdetail.putExtra("travel_name", "Pisa");
                startActivity(gototicketdetail);
                finish();
            }
        });
        item_borobudur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicketdetail = new Intent(Home.this,TicketDetail.class);
                gototicketdetail.putExtra("travel_name", "Borobudur");
                startActivity(gototicketdetail);
                finish();
            }
        });
        item_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicketdetail = new Intent(Home.this,TicketDetail.class);
                gototicketdetail.putExtra("travel_name", "Monas");
                startActivity(gototicketdetail);
                finish();
            }
        });
        item_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicketdetail = new Intent(Home.this,TicketDetail.class);
                gototicketdetail.putExtra("travel_name", "Pagoda");
                startActivity(gototicketdetail);
                finish();
            }
        });
        item_sphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicketdetail = new Intent(Home.this,TicketDetail.class);
                gototicketdetail.putExtra("travel_name", "Sphinx");
                startActivity(gototicketdetail);
                finish();
            }
        });
        item_torii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicketdetail = new Intent(Home.this,TicketDetail.class);
                gototicketdetail.putExtra("travel_name", "Torii");
                startActivity(gototicketdetail);
                finish();
            }
        });
    }

    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_value = sharedPreferences.getString(username_key, "");
    }
}
