package com.xoonstudio.tiketku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SuccessBuyTicket extends AppCompatActivity {
    Button btn_view_ticket, btn_dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);
        // load element
        btn_dashboard = findViewById(R.id.btn_dashboard);
        btn_view_ticket = findViewById(R.id.btn_view_ticket);

        // event
        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(SuccessBuyTicket.this, Home.class);
                startActivity(gotohome);
                finish();
            }
        });
        btn_view_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotomyticket = new Intent(SuccessBuyTicket.this, MyTicket.class);
                startActivity(gotomyticket);
                finish();
            }
        });
    }
}
