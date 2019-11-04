package com.xoonstudio.tiketku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    LinearLayout logo;
    Animation splash_from_left;

    String USERNAME_KEY ="usernamekey";
    String username_key = "";
    String username_value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load the element
        logo = findViewById(R.id.logo);

        // load animation
        splash_from_left = AnimationUtils.loadAnimation(this, R.anim.splash_from_left);

        // run adnimation
        logo.startAnimation(splash_from_left);

        // load usernam local
        getUsernameLocal();

        if(username_value != null && !username_value.equals("")){
            // move activity
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gotohome = new Intent(MainActivity.this, Home.class);
                    startActivity(gotohome);
                    finish();
                }
            }, 2000);
        }else{

            // move activity
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gotogetstarted = new Intent(MainActivity.this, GetStarted.class);
                    startActivity(gotogetstarted);
                    finish();
                }
            }, 2000);
        }
    }

    // get username local
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_value = sharedPreferences.getString(username_key, "");
    }
}
