package com.xoonstudio.tiketku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class GetStarted extends AppCompatActivity {
    Button btn_sign_in, btn_sign_up;
    TextView tagline;
    Animation splash_from_top, splash_from_left, splash_from_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // load a element
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_sign_up = findViewById(R.id.btn_sign_up);
        tagline = findViewById(R.id.tagline);

        // load animation
        splash_from_left = AnimationUtils.loadAnimation(this, R.anim.splash_from_left);
        splash_from_top = AnimationUtils.loadAnimation(this, R.anim.splash_from_top);
        splash_from_right = AnimationUtils.loadAnimation(this, R.anim.splash_from_right);

        // run animation
        tagline.startAnimation(splash_from_top);
        btn_sign_in.startAnimation(splash_from_left);
        btn_sign_up.startAnimation(splash_from_right);

        // event
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosignin = new Intent(GetStarted.this, SignIn.class);
                startActivity(gotosignin);
                finish();
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotosignup = new Intent(GetStarted.this, SignUp.class);
                startActivity(gotosignup);
                finish();
            }
        });
    }
}
