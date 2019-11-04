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

public class SignUpOne extends AppCompatActivity {
    ImageView btn_back;
    EditText xusername, xpassword, xemail_address;
    Button btn_continue;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username, password, email_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_one);

        // load element
        btn_back = findViewById(R.id.btn_back);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);
        xemail_address = findViewById(R.id.xemail_address);
        btn_continue = findViewById(R.id.btn_continue);

        // event
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // moving activity
                Intent backtosignup = new Intent(SignUpOne.this, SignUp.class);
                startActivity(backtosignup);
                finish();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = xusername.getText().toString();
                password = xpassword.getText().toString();
                email_address = xemail_address.getText().toString();

                if(username != null && password != null && email_address != null && !username.equals("") && !password.equals("") && !email_address.equals("")){

                    //validate buttom
                    btn_continue.setEnabled(false);
                    btn_continue.setText("Loading");

                    // load database
                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(xusername.getText().toString());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(dataSnapshot.exists()){
                                Toast.makeText(getApplicationContext(), "The Username has been take", Toast.LENGTH_SHORT).show();
                                btn_continue.setText("Continue");
                                btn_continue.setEnabled(true);
                            }else{
                                // save data to database
                                dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                                dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
                                dataSnapshot.getRef().child("email_address").setValue(xemail_address.getText().toString());
                                dataSnapshot.getRef().child("user_balance").setValue("800");
                                dataSnapshot.getRef().child("url_photo_sampul")
                                        .setValue("https://firebasestorage.googleapis.com/v0/b/tiketku-31155.appspot.com/o/SampulProfile%2Fpablo-home-movie-theatre.png?alt=media&token=14cf63b7-6d0e-46e9-998b-e16a78cfccce");

                                // save data local
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key, xusername.getText().toString());
                                editor.apply();

                                // moving activity
                                Intent gotosignuptwo = new Intent(SignUpOne.this, SignUpTwo.class);
                                startActivity(gotosignuptwo);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(), "Please fill all field", Toast.LENGTH_SHORT).show();
                    btn_continue.setText("Continue");
                    btn_continue.setEnabled(true);
                }
            }
        });
    }
}
