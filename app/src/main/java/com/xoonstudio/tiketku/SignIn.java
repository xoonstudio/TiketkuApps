package com.xoonstudio.tiketku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    Button btn_sign_in;
    EditText xusername, xpassword;
    ImageView btn_back;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // load the element
        btn_sign_in = findViewById(R.id.btn_sign_in);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);
        btn_back = findViewById(R.id.btn_back);

        // event
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // moving activity
                Intent backtogetstarted = new Intent(SignIn.this, GetStarted.class);
                startActivity(backtogetstarted);
                finish();
            }
        });
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validate button
                btn_sign_in.setEnabled(false);
                btn_sign_in.setText("Loading");

                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(xusername.getText().toString());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String password = xpassword.getText().toString();
                            String passwordDatabse = dataSnapshot.child("password").getValue().toString();
                            if (password.equals(passwordDatabse)){
                                // save data local
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key, xusername.getText().toString());
                                editor.apply();

                                // moving activity
                                Intent gotohome = new Intent(SignIn.this, Home.class);
                                startActivity(gotohome);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Please check your password", Toast.LENGTH_SHORT).show();

                                //validate button
                                btn_sign_in.setEnabled(true);
                                btn_sign_in.setText("Sign In");
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Invalid Username", Toast.LENGTH_SHORT).show();

                            //validate button
                            btn_sign_in.setEnabled(true);
                            btn_sign_in.setText("Sign In");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
