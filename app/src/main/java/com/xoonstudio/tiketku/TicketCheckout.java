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

import java.util.Random;

public class TicketCheckout extends AppCompatActivity {
    ImageView btn_back, btn_minus, btn_plus;
    LinearLayout notif_balance;
    TextView xtravel_name, xtravel_location, xuser_balance, xtotal_price, xticket_price, ticket_result, close_notif;
    Integer number_ticket, ticket_price, total_price, user_balance, special_number;
    String  transaction_number;
    Button btn_pay_now;

    Bundle bundle;

    DatabaseReference reference, reference_one, reference_two;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);
        // load the element
        btn_back = findViewById(R.id.btn_back);
        xtravel_name = findViewById(R.id.xtravel_name);
        xtravel_location = findViewById(R.id.xtravel_location);
        xuser_balance = findViewById(R.id.xuser_balance);
        xtotal_price = findViewById(R.id.xtotal_price);
        notif_balance = findViewById(R.id.notif_balance);
        xticket_price = findViewById(R.id.xticket_price);
        btn_minus = findViewById(R.id.btn_minus);
        btn_plus = findViewById(R.id.btn_plus);
        ticket_result = findViewById(R.id.ticket_result);
        close_notif = findViewById(R.id.close_notif);
        btn_pay_now = findViewById(R.id.btn_pay_now);

        // get data form intent
        bundle = getIntent().getExtras();
        final String travel_name = bundle.getString("travel_name");

        // load username local
        getUsernameLocal();

        // set hide first
        notif_balance.setVisibility(View.GONE);

        // set data
        number_ticket = 1;
        ticket_result.setText(number_ticket.toString());
        btn_minus.animate().alpha(0).start();
        user_balance = 25;
        ticket_price = 25;
        special_number = new Random().nextInt();
        transaction_number = travel_name + special_number.toString();

        // load data from database
        reference = FirebaseDatabase.getInstance().getReference().child("Travel").child(travel_name);
        reference_one = FirebaseDatabase.getInstance().getReference().child("Users").child(username_value);
        reference_two = FirebaseDatabase.getInstance().getReference().child("MyTicket").child(username_value).child(transaction_number);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xtravel_name.setText(dataSnapshot.child("travel_name").getValue().toString());
                xtravel_location.setText(dataSnapshot.child("travel_location").getValue().toString());
                ticket_price = Integer.parseInt(dataSnapshot.child("ticket_price").getValue().toString());
                // show ticket price
                xticket_price.setText("$ " + ticket_price.toString() + ".00");
                xtotal_price.setText("$ " + ticket_price.toString() + ".00");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        reference_one.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_balance = Integer.parseInt(dataSnapshot.child("user_balance").getValue().toString());
                xuser_balance.setText("$ " + user_balance.toString() + ".00");

                // define data again
                total_price = number_ticket * ticket_price;
                if(user_balance < total_price){
                    notif_balance.setVisibility(View.VISIBLE);
                    btn_pay_now.animate().alpha(0).translationY(400).setDuration(400).start();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_ticket -= 1;
                if(number_ticket == 1){
                    btn_minus.animate().alpha(0).setDuration(400).start();
                    btn_minus.setEnabled(false);
                }
                ticket_result.setText(number_ticket.toString());

                // for total price
                total_price = number_ticket * ticket_price;
                xtotal_price.setText("$ " + total_price.toString() + ".00");

                if(user_balance < total_price){
                    btn_pay_now.animate().alpha(0).translationY(400).setDuration(400).start();
                }else{
                    btn_pay_now.animate().alpha(1).setDuration(400).translationY(0).start();
                }
            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_ticket += 1;
                ticket_result.setText(number_ticket.toString());

                if(number_ticket > 1){
                    btn_minus.animate().alpha(1).setDuration(400).start();
                    btn_minus.setEnabled(true);
                }

                // all total price
                total_price = number_ticket * ticket_price;
                xtotal_price.setText("$ " + total_price.toString() + ".00");

                if(user_balance < total_price){
                    notif_balance.setVisibility(View.VISIBLE);
                    btn_pay_now.animate().alpha(0).translationY(400).setDuration(400).start();
                }
            }
        });
        close_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notif_balance.setVisibility(View.GONE);
            }
        });

        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // validate button
                btn_pay_now.setText("Loading ...");
                btn_pay_now.setEnabled(false);
                // save to database
                reference_two.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("travel_name").setValue(xtravel_name.getText().toString());
                        dataSnapshot.getRef().child("transaction_number").setValue(transaction_number);
                        dataSnapshot.getRef().child("travel_location").setValue(xtravel_location.getText().toString());
                        dataSnapshot.getRef().child("total_price").setValue(total_price.toString());
                        dataSnapshot.getRef().child("ticket_price").setValue(ticket_price.toString());
                        dataSnapshot.getRef().child("total_ticket").setValue(number_ticket.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                reference_one.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       Integer new_user_balance = user_balance - total_price;
                        // change balance
                        dataSnapshot.getRef().child("user_balance").setValue(new_user_balance.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // moviing activity
                Intent gotosuccessbuy = new Intent(TicketCheckout.this, SuccessBuyTicket.class);
                startActivity(gotosuccessbuy);
                finish();

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoticketdetail = new Intent(TicketCheckout.this, TicketDetail.class);
                backtoticketdetail.putExtra("travel_name", travel_name);
                startActivity(backtoticketdetail);
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
