package com.xoonstudio.tiketku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessSignUp extends AppCompatActivity {
    Button btn_explore;
    TextView description;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_sign_up);
        // load the element
        btn_explore = findViewById(R.id.btn_explore);
        description = findViewById(R.id.description);

        // load username local
        getUsernameLocal();

        // event
        description.setText("Selamat "+ username_value+ " anda telah melakukan registrasi.");

        btn_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(SuccessSignUp.this, Home.class);
                startActivity(gotohome);
                finish();
            }
        });

    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_value = sharedPreferences.getString(username_key, "");
    }
}
