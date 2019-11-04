package com.xoonstudio.tiketku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetail extends AppCompatActivity {
    ImageView btn_back;
    TextView xtravel_name, xtravel_location, xtravel_date, xtravel_time, xtravel_rule, do_refund;
    Button btn_refund_ticket;
    LinearLayout notif_refund;
    Bundle bundle;

    DatabaseReference reference, reference_one, reference_two;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_value = "";

    Integer value_balance = 20, value_total_price = 20, new_user_balance = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);
        // load the element
        btn_back = findViewById(R.id.btn_back);
        xtravel_name = findViewById(R.id.xtravel_name);
        xtravel_location = findViewById(R.id.xtravel_location);
        xtravel_date = findViewById(R.id.xtravel_date);
        xtravel_time = findViewById(R.id.xtravel_time);
        xtravel_rule = findViewById(R.id.xtravel_rule);
        btn_refund_ticket = findViewById(R.id.btn_refund_ticket);
        do_refund = findViewById(R.id.do_refund);
        notif_refund = findViewById(R.id.notif_refund);

        // get data from intent
        bundle = getIntent().getExtras();
        String travel_name = bundle.getString("travel_name");
        String transaction_number = bundle.getString("transaction_number");
        String total_price = bundle.getString("total_price");

        // set visible first
        notif_refund.setVisibility(View.GONE);

        // loda user local
        getUsernameLocal();

        // define data
        value_total_price = Integer.parseInt(total_price);

        // load data from database
        reference = FirebaseDatabase.getInstance().getReference().child("Travel").child(travel_name);
        reference_one = FirebaseDatabase.getInstance().getReference().child("MyTicket").child(username_value).child(transaction_number);
        reference_two = FirebaseDatabase.getInstance().getReference().child("Users").child(username_value);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xtravel_name.setText(dataSnapshot.child("travel_name").getValue().toString());
                xtravel_location.setText(dataSnapshot.child("travel_location").getValue().toString());
                xtravel_date.setText(dataSnapshot.child("travel_date").getValue().toString());
                xtravel_time.setText(dataSnapshot.child("travel_time").getValue().toString());
                xtravel_rule.setText(dataSnapshot.child("travel_rule").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference_two.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                value_balance = Integer.parseInt(dataSnapshot.child("user_balance").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_refund_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notif_refund.setVisibility(View.VISIBLE);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtomyticket = new Intent(MyTicketDetail.this, MyTicket.class);
                startActivity(backtomyticket);
                finish();
            }
        });

        do_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new_user_balance = value_balance + value_total_price;
                // set back balance
                reference_two.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("user_balance").setValue(new_user_balance.toString());
                        notif_refund.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //delete ticket
                reference_one.removeValue();

                // moving activity
                Intent backtomyticket = new Intent(MyTicketDetail.this, MyTicket.class);
                startActivity(backtomyticket);
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
