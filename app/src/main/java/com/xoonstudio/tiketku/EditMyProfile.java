package com.xoonstudio.tiketku;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditMyProfile extends AppCompatActivity {
    ImageView btn_back, user_profile, image_sampul;
    EditText xname, xbio, xusername, xpassword, xemail_address;
    Button btn_save_profile;
    LinearLayout btn_add_user_profile, btn_add_sampul, notif_success;

    DatabaseReference reference;
    StorageReference storageReference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_value = "";

    Uri photo_location;
    Integer photo_max = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        // load the element
        btn_back = findViewById(R.id.btn_back);
        user_profile = findViewById(R.id.user_profile);
        image_sampul = findViewById(R.id.image_sampul);
        xname = findViewById(R.id.xname);
        xbio = findViewById(R.id.xbio);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);
        xemail_address = findViewById(R.id.xemail_address);
        btn_save_profile = findViewById(R.id.btn_save_profile);
        btn_add_user_profile = findViewById(R.id.btn_add_user_profile);
        btn_add_sampul = findViewById(R.id.btn_add_sampul);
        notif_success = findViewById(R.id.notif_success);

        // load username local
        getUsernameLocal();

        //hide notif & so on
        notif_success.setVisibility(View.GONE);

        btn_add_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });

        // firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_value);
        storageReference = FirebaseStorage.getInstance().getReference().child("PhotoProfile").child(username_value);

        // load conent content
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Picasso.with(EditMyProfile.this).load((String) dataSnapshot.child("url_photo_sampul").getValue()).centerCrop().fit().into(image_sampul);
                Picasso.with(EditMyProfile.this).load((String) dataSnapshot.child("url_photo_profile").getValue()).centerCrop().fit().into(user_profile);
                xname.setText(dataSnapshot.child("name").getValue().toString());
                xbio.setText(dataSnapshot.child("bio").getValue().toString());
                xusername.setText(dataSnapshot.child("username").getValue().toString());
                xpassword.setText(dataSnapshot.child("password").getValue().toString());
                xemail_address.setText(dataSnapshot.child("email_address").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtomyprofile = new Intent(EditMyProfile.this, MyProfile.class);
                startActivity(backtomyprofile);
                finish();
            }
        });
        btn_save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save data local
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, xusername.getText().toString());
                editor.apply();

                if(photo_location != null){
                    final StorageReference storage = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                    storage.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String photo_url = uri.toString();
                                    reference.getRef().child("url_photo_profile").setValue(photo_url);
                                }
                            });
                            reference.getRef().child("name").setValue(xname.getText().toString());
                            reference.getRef().child("bio").setValue(xbio.getText().toString());
                            reference.getRef().child("username").setValue(xusername.getText().toString());
                            reference.getRef().child("password").setValue(xpassword.getText().toString());
                            reference.getRef().child("email_address").setValue(xemail_address.getText().toString());
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            btn_save_profile.setEnabled(false);
                            btn_save_profile.setText("Loading");
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            notif_success.setVisibility(View.VISIBLE);
                            // moving activity
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent backtomyprofile = new Intent(EditMyProfile.this, MyProfile.class);
                                    startActivity(backtomyprofile);
                                    finish();
                                }
                            }, 400);
                        }
                    });
                }
                if(photo_location == null){

                    btn_save_profile.setEnabled(false);
                    btn_save_profile.setText("Loading");

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("name").setValue(xname.getText().toString());
                            dataSnapshot.getRef().child("bio").setValue(xbio.getText().toString());
                            dataSnapshot.getRef().child("username").setValue(xusername.getText().toString());
                            dataSnapshot.getRef().child("password").setValue(xpassword.getText().toString());
                            dataSnapshot.getRef().child("email_address").setValue(xemail_address.getText().toString());

                            notif_success.setVisibility(View.VISIBLE);
                            // moving activity
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent backtomyprofile = new Intent(EditMyProfile.this, MyProfile.class);
                                    startActivity(backtomyprofile);
                                    finish();
                                }
                            }, 400);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
    // get username local
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
       username_value = sharedPreferences.getString(username_key, "");
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null){
            photo_location = data.getData();
            Picasso.with(EditMyProfile.this).load(photo_location).centerCrop().fit().into(user_profile);
        }
    }
}
