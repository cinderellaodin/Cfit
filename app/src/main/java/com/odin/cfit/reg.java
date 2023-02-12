package com.odin.cfit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class reg extends AppCompatActivity implements View.OnClickListener {
    Button fbreg_btn, reg_btn;
    EditText etEmail, etPassword;
    TextView tvlog;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        firebaseAuth = FirebaseAuth.getInstance();
        //if user is already logged in
        if (firebaseAuth.getCurrentUser() !=null) {
            //starting home activity
            finish();
            startActivity(new Intent(getApplicationContext(), Home.class));
        }

        progressDialog = new ProgressDialog(this);

        //fbreg_btn = (Button) findViewById(R.id.fbreg_btn);
        reg_btn = (Button) findViewById(R.id.reg_btn);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvlog = (TextView) findViewById(R.id.tvlog);


        reg_btn.setOnClickListener(this);
        tvlog.setOnClickListener(this);

    }

    //register method
    private void registerUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //if email is empty
            Toast.makeText(this, "Please enter email:(", Toast.LENGTH_SHORT).show();
            //stop function from executing further
            return;
        }
        if (TextUtils.isEmpty(password)){
            //if password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stop function from executing
            return;
        }
        //if validation is okay
        //show progress dialog to show

        progressDialog.setMessage("Registering User....");
        progressDialog.show();

        //firebase to register users
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            /*//user is successfully registered and logged in
                            Toast.makeText(RegisterActivity.this, "Registered pro Sucessfullly :)", Toast.LENGTH_SHORT).show();
                               */ finish();
                            startActivity(new Intent(getApplicationContext(), Home.class));

                        }else{
                            Toast.makeText(reg.this,"could not be registered :( please try again", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if (view == reg_btn){
            //calling register function

            registerUser();
        }if (view == tvlog ){
            // for login activity

            finish();
            startActivity(new Intent(this, login.class));
        }

    }
}
