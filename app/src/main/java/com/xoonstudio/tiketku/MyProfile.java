package com.xoonstudio.tiketku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MyProfile extends AppCompatActivity {
    ImageView ximage_sampul, xuser_profile, btn_back;
    TextView xname, xbio;
    Button btn_edit_profile, btn_sign_out;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_value = "";

    DatabaseReference reference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // load the element
        ximage_sampul = findViewById(R.id.ximage_sampul);
        xuser_profile = findViewById(R.id.xuser_profile);
        xname = findViewById(R.id.xname);
        xbio = findViewById(R.id.xbio);
        btn_edit_profile = findViewById(R.id.btn_edit_profile);
        btn_sign_out = findViewById(R.id.btn_sign_out);
        btn_back = findViewById(R.id.btn_back);

        // load username local
        getUsernameLocal();

        // load content
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_value);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xname.setText(dataSnapshot.child("name").getValue().toString());
                xbio.setText(dataSnapshot.child("bio").getValue().toString());
                Picasso.with(MyProfile.this).load((String) dataSnapshot.child("url_photo_profile").getValue()
                        .toString()).centerCrop().fit().into(xuser_profile);
                Picasso.with(MyProfile.this).load((String) dataSnapshot.child("url_photo_sampul").getValue()).centerCrop().fit().into(ximage_sampul);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoeditmyprofile = new Intent(MyProfile.this, EditMyProfile.class);
                startActivity(gotoeditmyprofile);
                finish();
            }
        });
        btn_sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // erase username local
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                // move activity
                Intent gotosignout = new Intent(MyProfile.this, GetStarted.class);
                startActivity(gotosignout);
                finish();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoHome = new Intent(MyProfile.this, Home.class);
                startActivity(backtoHome);
                finish();
            }
        });
    }

    // get usernamelocal
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_value  = sharedPreferences.getString(username_key, "");
    }
}
