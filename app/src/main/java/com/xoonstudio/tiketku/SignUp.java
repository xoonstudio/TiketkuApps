package com.xoonstudio.tiketku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SignUp extends AppCompatActivity {
    ImageView btn_back;
    Button btn_sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // load animation
        btn_back = findViewById(R.id.btn_back);
        btn_sign_up = findViewById(R.id.btn_sign_up);

        // event
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bactogetstrted = new Intent(SignUp.this, GetStarted.class);
                startActivity(bactogetstrted);
                finish();
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosignup = new Intent(SignUp.this, SignUpOne.class);
                startActivity(gotosignup);
                finish();
            }
        });
    }
}
