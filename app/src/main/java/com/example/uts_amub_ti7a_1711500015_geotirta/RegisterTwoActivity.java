package com.example.uts_amub_ti7a_1711500015_geotirta;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class RegisterTwoActivity extends AppCompatActivity {
    Button btn_register, btn_addphoto;
    ImageView pic_photo_register_user;
    EditText hobi, alamat;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();
        pic_photo_register_user = findViewById(R.id.picphotoregister);
        btn_addphoto = findViewById(R.id.addphoto);
        btn_register = findViewById(R.id.bdaftar);
        hobi = findViewById(R.id.daftarhobi);
        alamat = findViewById(R.id.daftaralamat);

        btn_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhoto();
            }
        });



        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ubah state menjadi loading
                btn_register.setEnabled(false);
                btn_register.setText("Loading ...");

                //menyimpan pada firebase
                // reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username.getText().toString());

                reference = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(username_key_new);
                storage = FirebaseStorage.getInstance().getReference().child("Photousers")
                        .child(username_key_new);


                //validasi file
                if (photo_location != null){
                    final StorageReference storageReference1 =
                            storage.child(System.currentTimeMillis() + "." +
                                    getFileExtension(photo_location));

                    storageReference1.putFile(photo_location)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){

                                    storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String uri_photo = uri.toString();
                                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                            reference.getRef().child("hobi").setValue(hobi.getText().toString());
                                            reference.getRef().child("alamat").setValue(alamat.getText().toString());
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Intent gotosucces = new Intent(com.example.uts_amub_ti7a_1711500015_geotirta.RegisterTwoActivity.this, com.example.uts_amub_ti7a_1711500015_geotirta.MainActivity.class);
                                            startActivity(gotosucces);
                                        }
                                    });


                                }
                            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Intent gotosucces = new Intent(com.example.uts_amub_ti7a_1711500015_geotirta.RegisterTwoActivity.this, com.example.uts_amub_ti7a_1711500015_geotirta.MainActivity.class);
                            startActivity(gotosucces);
                        }
                    });
                }


            }
        });


    }
    //menyimpan uri pada firebase
    String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    //untuk mencari foto
    public void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    //mengecek apakah ada fotonya
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_register_user);
        }
    }
    public void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"" );
    }
}