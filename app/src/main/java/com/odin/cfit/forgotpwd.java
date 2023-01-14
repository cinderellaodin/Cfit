package com.odin.cfit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpwd extends AppCompatActivity implements View.OnClickListener {
        Button reset_btn;
        EditText etEmail;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpwd);
        setTitle("reset Password");

        firebaseAuth =FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() !=null){
            //starting home activity
            finish();
            startActivity(new Intent(getApplicationContext(), Home.class));

        }

        reset_btn = (Button) findViewById(R.id.reset_btn);
        etEmail = (EditText) findViewById(R.id.etEmail);

        progressDialog= new ProgressDialog(this);
        reset_btn.setOnClickListener(this);

    }

    private void resetpassword(){
        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            //if email is empty
            Toast.makeText(this, "Please enter email:(", Toast.LENGTH_SHORT).show();
            //stop function from executing further
            return;
        }

        progressDialog.setMessage("Confirming credentials....");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            //start home activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), login.class));
                        }else{
                            Toast.makeText(forgotpwd.this,"Incorrect email/password :( please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == reset_btn ){
            //reset password function
            resetpassword();
        }
    }
}
