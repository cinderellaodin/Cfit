package com.odin.cfit;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {
                Intent intent = new Intent(splash.this, IntroActivity.class );
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);

        }catch (Exception ex){
            Log.e("Error on contact", ex.getMessage());
        }


    }
}
