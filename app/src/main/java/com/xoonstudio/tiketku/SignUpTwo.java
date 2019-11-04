package com.xoonstudio.tiketku;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class SignUpTwo extends AppCompatActivity {
    TextView hello_tag;
    LinearLayout btn_add_photo_profile;
    EditText xname, xbio;
    ImageView btn_back, user_profile;
    Button btn_continue;

    DatabaseReference reference;
    StorageReference storageReference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_value = "";
    String name, bio;

    Integer photo_max = 1;
    Uri photo_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_two);

        // load the element
        hello_tag = findViewById(R.id.hello_tag);
        user_profile = findViewById(R.id.user_profile);
        btn_add_photo_profile = findViewById(R.id.btn_add_photo_profile);
        xname = findViewById(R.id.xname);
        xbio = findViewById(R.id.xbio);
        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        // Load usernamelocal
        getUsernameLocal();

        // set value
        hello_tag.setText("Hello, \n" +username_value);

        // event
        btn_add_photo_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move activity
                Intent backtosignupone = new Intent(SignUpTwo.this, SignUpOne.class);
                startActivity(backtosignupone);
                finish();
            }
        });
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get data to validate
                name = xname.getText().toString();
                bio = xbio.getText().toString();

                reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_value);
                storageReference = FirebaseStorage.getInstance().getReference().child("PhotoProfile").child(username_value);

                // validation photo
                if(photo_location != null) {

                    if(name != null && bio != null && !name.equals("") && !bio.equals("")){

                        // upload photo
                        final StorageReference storage = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(photo_location));
                        storage.putFile(photo_location).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String url_photo = uri.toString();
                                        reference.getRef().child("url_photo_profile").setValue(url_photo);
                                    }
                                });
                                reference.getRef().child("name").setValue(xname.getText().toString());
                                reference.getRef().child("bio").setValue(xbio.getText().toString());
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                btn_continue.setEnabled(false);
                                btn_continue.setText("Loading");
                            }
                        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                // move activity
                                Intent gotosuccess = new Intent(SignUpTwo.this, SuccessSignUp.class);
                                startActivity(gotosuccess);
                                finish();
                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(), "Fill all field", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Please pick your picture", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    // get username
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_value = sharedPreferences.getString(username_key, "");
    }

    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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
            Picasso.with(this).load(photo_location).centerCrop().fit().into(user_profile);
        }
    }
}
