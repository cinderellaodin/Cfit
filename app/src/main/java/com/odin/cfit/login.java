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

public class login extends AppCompatActivity implements View.OnClickListener {

    Button fblogin_btn, login_btn;
    EditText etEmail, etPassword;
    TextView tvfpwd, tvreg;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth =FirebaseAuth.getInstance();
        //to tract if the user is already logged in
        if (firebaseAuth.getCurrentUser() !=null){
            //starting home activity
            finish();
            startActivity(new Intent(getApplicationContext(), Home.class));

        }

        /*dec */
        fblogin_btn = (Button)findViewById(R.id.fblogin_btn);
        login_btn = (Button) findViewById(R.id.login_btn);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvfpwd = (TextView) findViewById(R.id.tvfpwd);
        tvreg = (TextView) findViewById(R.id.tvreg);

        progressDialog= new ProgressDialog(this);
        login_btn.setOnClickListener(this);
        tvfpwd.setOnClickListener(this);
        tvreg.setOnClickListener(this);
    }
    private void userLogin() {
        //gettting the values
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //if email is empty
            Toast.makeText(this, "Please enter email:(", Toast.LENGTH_SHORT).show();
            //stop function from executing further
            return;
        }
        if (TextUtils.isEmpty(password)) {
            //if password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stop function from executing
            return;
        }
        //if validation is okay
        //show progress dialog to show

        progressDialog.setMessage("Confirming credentials....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){
                            //start home activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        }else{
                            Toast.makeText(login.this,"Incorrect email/password :( please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    @Override
    public void onClick(View view) {
        if (view ==login_btn){
            /*login method*/
           userLogin();
        }if (view == tvfpwd){

            finish();
            startActivity(new Intent(this, forgotpwd.class));
        }if (view == tvreg){

            finish();
            startActivity(new Intent(this, reg.class));
        }
    }
}
