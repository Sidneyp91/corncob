package com.pundo.corncob.errors;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.pundo.corncob.R;
import com.pundo.corncob.auth.login;

public class verification extends AppCompatActivity {
    private static int TIME_OUT = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(verification.this, login.class);
                startActivity(intent);
            }
        },TIME_OUT);
    }
}
