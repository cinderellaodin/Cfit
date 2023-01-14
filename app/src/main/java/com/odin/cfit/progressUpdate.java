package com.odin.cfit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class progressUpdate extends AppCompatActivity {
    /*firebase*/

    /*firebase*/
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageTask mUploadTask;

    /*widget*/
    ImageView mImageView;
    Uri imageuri;
    EditText etCWeight, etCwaist, etCchest, etChip, etCthigh, etCarm, etCdate;

    int day, month, year, yearfinal;
    Calendar calendar;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_update);

        /*firebase*/
        //initializing firebase
        firebaseAuth =FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(progressUpdate.this, login.class));
        }
        /*FirebaseUser*/
        user = firebaseAuth.getCurrentUser();

        /*firebasestorage*/
        storageReference = FirebaseStorage.getInstance().getReference();
        //db reference
        databaseReference = FirebaseDatabase.getInstance().getReference();


        /*for the selected cloth image*/
        mImageView = (ImageView) findViewById(R.id.Cimageview);
        imageuri = getIntent().getData();
        mImageView.setImageURI(imageuri);

        /*widget*/
        etCWeight =  (EditText) findViewById(R.id.etCweight);
        etCwaist = (EditText) findViewById(R.id.etwaist);
        etCchest = (EditText) findViewById(R.id.etChest);
        etChip = (EditText) findViewById(R.id.ethip);
        etCthigh =(EditText) findViewById(R.id.etthigh);
        etCarm = (EditText) findViewById(R.id.etarm);
        etCdate = (EditText) findViewById(R.id.etprogDate);

        etCdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                //used for age
                yearfinal = calendar.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(progressUpdate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String tdate = String.valueOf(year) + "/" + String.valueOf(month) + "/" + String.valueOf(dayOfMonth);
                        etCdate.setText(tdate);

                      /*  int yeardiff = yearfinal - year;
                        uage = String.valueOf(yeardiff);*/
                        // etdob.setText(uage);


                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
    }

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
            if (mUploadTask !=null && mUploadTask.isInProgress()){
                Toast.makeText(progressUpdate.this, "Upload in Progress", Toast.LENGTH_SHORT).show();

            }else {
                uploadFile();


            }
        }
        return super.onOptionsItemSelected(item);
    }

    //getting our file extension
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    //for uploading file
    private void uploadFile(){
        if(imageuri != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Saving Image....");
            progressDialog.show();

            final StorageReference filestoreage = storageReference.child(user.getUid()).child("Progress Report").child(System.currentTimeMillis()
                    + "." + getFileExtension(imageuri));
            try {
                mUploadTask = filestoreage.putFile(imageuri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(progressUpdate.this, "image uploaded....", Toast.LENGTH_LONG).show();


                                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //   String url = uri.toString();

                                        // Got the uri

                                        saveinfo(Double.parseDouble(etCWeight.getText().toString().trim()), Double.parseDouble(etCwaist.getText().toString().trim()),
                                                Double.parseDouble(etCchest.getText().toString().trim()), Double.parseDouble(etChip.getText().toString().trim()),
                                                Double.parseDouble(etCthigh.getText().toString().trim()),
                                                Double.parseDouble(etCarm.getText().toString().trim()), uri.toString());


                                        updateweight(etCdate.getText().toString().trim(), Double.
                                                parseDouble(etCWeight.getText().toString().trim()));



                                        //    Toast.makeText(EditProfile.this, url, Toast.LENGTH_LONG).show();

                                        //  ImageUpload imageUpload = new ImageUpload(editText5.getText().toString(), uri.toString());
                                        // Wrap with Uri.parse() when retrieving

                                        //  String uploadId = mDatabaseRef.push().getKey();
                                        // mDatabaseRef.child(uploadId).setValue(imageUpload);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        Toast.makeText(progressUpdate.this, "ERRoR....", Toast.LENGTH_LONG).show();

                                    }
                                });

                            }




                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                                Toast.makeText(progressUpdate.this, exception.getMessage(), Toast.LENGTH_LONG).show();

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
            Toast.makeText(progressUpdate.this,"no file chosen....", Toast.LENGTH_LONG).show();

        }
    }

    private void saveinfo(double currentweight, double currentwaist, double currentchest, double currenthip, double currentthigh, double currentarm, String imageUrl) {

        Upload upload = new Upload(currentweight,currentwaist,currentchest,currenthip,currentthigh,currentarm,imageUrl);

        String uploadId =   databaseReference.push().getKey();
        databaseReference.child(user.getUid()).child("Weightloss Progress").child(uploadId).setValue(upload);

        Toast.makeText(progressUpdate.this, "Weightloss Progress Updated" + upload.toString(), Toast.LENGTH_SHORT).show();


    }

    private void updateweight(String progDate, double progweight) {
        WeighTracker weightTracker = new WeighTracker(progDate, progweight);
        String uploadCId =   databaseReference.push().getKey();
        databaseReference.child(user.getUid()).child("Weight Tracker").child(uploadCId).setValue(weightTracker);


    }


}