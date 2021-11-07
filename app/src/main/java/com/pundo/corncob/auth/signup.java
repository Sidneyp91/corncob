package com.pundo.corncob.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pundo.corncob.MainActivity;
import com.pundo.corncob.R;
import com.pundo.corncob.form.farmDetails;
import com.pundo.corncob.home;
import com.pundo.corncob.model.user;
import com.pundo.corncob.util.PreferenceUtils;

public class signup extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference users;
    private TextInputLayout fname, mail, pass, password2;
    Button trial;
    private FirebaseAuth mAuth;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");

        fname = findViewById(R.id.fname);
        mail = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        final Button signup = findViewById(R.id.sign_up);
        mAuth = FirebaseAuth.getInstance();

        if(PreferenceUtils.getEmail(signup.this)!= null && !PreferenceUtils.getEmail(signup.this).isEmpty()){
            Intent intent = new Intent(signup.this, home.class);
            startActivity(intent);
            finish();
        }else{

        }


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (haveNetwork()) {
                    if(TextUtils.isEmpty(fname.getEditText().getText().toString()) || TextUtils.isEmpty(mail.getEditText().getText().toString())
                            || TextUtils.isEmpty(pass.getEditText().getText().toString())){
                        Toast.makeText(signup.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                    }else if (!TextUtils.equals(pass.getEditText().getText().toString(), password2.getEditText().getText().toString())){
                        Toast.makeText(signup.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }

                    else{
                        final ProgressDialog progressDialog = new ProgressDialog(signup.this);
                        progressDialog.setMessage("Creating account");
                        progressDialog.show();

                        users.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String names = fname.getEditText().getText().toString();
                                final String email = mail.getEditText().getText().toString();
                                final String password = pass.getEditText().getText().toString();

                                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("mail",mail.getEditText().getText().toString() );
                                editor.putString("name",names);
                                editor.apply();

                                user save = new user(email,password);
                                users.child(names).setValue(save);
                                progressDialog.dismiss();
                                Toast.makeText(signup.this, "Account created successfully", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(signup.this, farmDetails.class));
                                finish();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }

                }else if (!haveNetwork()){
                    Intent intent = new Intent(signup.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });



    }


    private boolean haveNetwork(){
        boolean have_WIFI = false;
        boolean have_MobileData = false;


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info:networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI"))
                if(info.isConnected())
                    have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    have_MobileData=true;
        }
        return have_MobileData || have_WIFI;
    }
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    public void loginAction(View view) {
        Intent intent = new Intent(signup.this, login.class);
        startActivity(intent);
    }
    public void close(View view){
        Intent intent = new Intent(signup.this, home.class);
        startActivity(intent);
    }

}
