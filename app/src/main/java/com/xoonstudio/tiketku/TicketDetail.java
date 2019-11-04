package com.xoonstudio.tiketku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetail extends AppCompatActivity {
    ImageView btn_back, xtravel_banner;
    TextView xtravel_name, xtravel_location, xphoto_info, xwifi_info, xfestival_info, xdescription, xticket_price;
    Button btn_buy_ticket;

    Bundle bundle;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        // load the element
        btn_back = findViewById(R.id.btn_back);
        xtravel_banner = findViewById(R.id.xtravel_banner);
        xtravel_name = findViewById(R.id.xtravel_name);
        xtravel_location = findViewById(R.id.xtravel_location);
        xphoto_info = findViewById(R.id.xphoto_info);
        xwifi_info = findViewById(R.id.xwifi_info);
        xfestival_info = findViewById(R.id.xfestival_info);
        xdescription = findViewById(R.id.xdescription);
        xticket_price = findViewById(R.id.xticket_price);
        btn_buy_ticket = findViewById(R.id.btn_buy_ticket);

        // get data from intent
        bundle = getIntent().getExtras();
        final String travel_name = bundle.getString("travel_name");

        // load data from database
        reference = FirebaseDatabase.getInstance().getReference().child("Travel").child(travel_name);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(TicketDetail.this).load((String) dataSnapshot.child("travel_banner").getValue()).centerCrop().fit().into(xtravel_banner);
                xtravel_name.setText(dataSnapshot.child("travel_name").getValue().toString());
                xtravel_location.setText(dataSnapshot.child("travel_location").getValue().toString());
                xphoto_info.setText(dataSnapshot.child("photo_info").getValue().toString());
                xwifi_info.setText(dataSnapshot.child("wifi_info").getValue().toString());
                xfestival_info.setText(dataSnapshot.child("festival_info").getValue().toString());
                xdescription.setText(dataSnapshot.child("description").getValue().toString());
                xticket_price.setText("$" + dataSnapshot.child("ticket_price").getValue().toString()+ ".00");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_buy_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gototicketcheckout = new Intent(TicketDetail.this, TicketCheckout.class);
                gototicketcheckout.putExtra("travel_name", travel_name);
                startActivity(gototicketcheckout);
                finish();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtohome = new Intent(TicketDetail.this, Home.class);
                startActivity(backtohome);
                finish();
            }
        });
    }
}
