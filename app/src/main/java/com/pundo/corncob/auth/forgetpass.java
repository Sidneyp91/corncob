package com.pundo.corncob.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.pundo.corncob.R;

public class forgetpass extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputLayout fpemail;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgetpass);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        fpemail = findViewById(R.id.fpmail);
        dialog = new ProgressDialog(this);
        Button fpsubmit = findViewById(R.id.fpsubmit);
        mAuth = FirebaseAuth.getInstance();

        fpsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPass();
            }
        });


    }

    private void forgotPass() {
        dialog.setMessage("Sending Email...");
        dialog.show();
        String email = fpemail.getEditText().getText().toString();
        if(!TextUtils.isEmpty(email)){
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("Forgot Email","Email sent");
                        Toast.makeText(forgetpass.this, "Check email for further instruction", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(forgetpass.this, login.class);
                        dialog.dismiss();
                        startActivity(intent);
                        finish();
                    }else{
                        dialog.dismiss();
                        Toast.makeText(forgetpass.this, "Something went wrong, try again later", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else{
            dialog.dismiss();
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
        }
    }
    public void close(View view) {
        Intent intent = new Intent(forgetpass.this, login.class);
        startActivity(intent);
    }

}
