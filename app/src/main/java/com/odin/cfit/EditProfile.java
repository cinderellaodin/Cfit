package com.odin.cfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;

public class EditProfile extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private Uri filepath;
    private StorageReference storageReference;

    CircularImageView cimage;
    EditText etName;
    TextView tvemail, tvChangeImage;
    String name;
    private int REQUEST_CODE =1 ;
    private Uri photoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this, login.class));
        }
        user = firebaseAuth.getCurrentUser();

        /*firebasestorage*/
        storageReference = FirebaseStorage.getInstance().getReference();
        //db reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        cimage = (CircularImageView) findViewById(R.id.image);
        etName = (EditText) findViewById(R.id.etname);
        tvChangeImage = (TextView) findViewById(R.id.tvChangeImage);
        tvemail = (TextView) findViewById(R.id.tvemail);

        tvemail.setText(user.getEmail());

        tvChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImagefromfile();
            }
        });

        getIntent().hasExtra("profile_name");
        getIntent().hasExtra("image_url");
        String profilename = getIntent().getStringExtra("profile_name");
        String profilephoto = getIntent().getStringExtra("image_url");
        etName.setText(profilename);

        Glide.with(this).load(profilephoto)
                .centerCrop()
                .into(cimage);

    }

    private void getImagefromfile() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select picture"),REQUEST_CODE );
    }


    //getting our file extension
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            filepath = data.getData();
            try{

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath = data.getData());
                /*add clear imageview and vie new image*/
                cimage.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }



    //for uploading file
    private void uploadFile(){
        if(filepath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving Image....");
            progressDialog.show();
            StorageReference riversRef = storageReference.child(user.getUid()).child("profile").child(System.currentTimeMillis()
                    + "." + getFileExtension(filepath));

            try {
                riversRef.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, "image uploaded....", Toast.LENGTH_LONG).show();

                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(etName.getText().toString().trim())
                                                .setPhotoUri(Uri.parse(uri.toString()))
                                                .build();

                                        user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(EditProfile.this, "Profile Updated....", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        Toast.makeText(EditProfile.this, "Error....", Toast.LENGTH_LONG).show();
                                    }
                                });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                                Toast.makeText(EditProfile.this, exception.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage(((int) progress) + "%uploaded");
                            }
                        });
            }catch (Exception ex){
                Log.e("error", ex.getMessage());
            }

        } else {
            // error toast
            Toast.makeText(EditProfile.this,"no file chosen....", Toast.LENGTH_LONG).show();

        }
    }

    /*menu*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            uploadFile();
        }
        return super.onOptionsItemSelected(item);
    }

}