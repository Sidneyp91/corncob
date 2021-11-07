package com.pundo.corncob.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pundo.corncob.R;
import com.pundo.corncob.errors.error404;
import com.pundo.corncob.errors.verification;
import com.pundo.corncob.home;
import com.pundo.corncob.util.PreferenceUtils;

import org.jetbrains.annotations.NotNull;

public class login extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private SharedPreferences mPrefs;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private TextInputLayout email, password;
    private FirebaseAuth mAuth;
    TextView welcome, access;
    Button login;
    ProgressDialog dialog;
    DatabaseReference farm_details;
    FirebaseDatabase database;
    Boolean verified = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializing views
         database = FirebaseDatabase.getInstance();
         farm_details = database.getReference("Farm Details");
         welcome = findViewById(R.id.welcome);
        access = findViewById(R.id.access);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        dialog = new ProgressDialog(this);



        //check if user has loggedin
        if(PreferenceUtils.getEmail(login.this)!= null && !PreferenceUtils.getEmail(login.this).isEmpty()){
            Intent intent = new Intent(login.this, home.class);
            startActivity(intent);
            finish();
        }else{

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(login.this);
                progressDialog.setMessage("Logging in ...");
                progressDialog.show();
                if(haveNetwork()){
                    farm_details = FirebaseDatabase.getInstance().getReference().child("email");

                    farm_details.orderByChild("email").equalTo(email.getEditText().getText().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    //vet the farm Details
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Farm Details");
                                    userRef.orderByChild("verified").equalTo("false")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {

                                                @Override
                                                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        Intent intent = new Intent(login.this, verification.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {

                                                        Intent intent = new Intent(login.this, home.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");

                                    firebaseDatabase.orderByChild("password").equalTo(password.getEditText().getText().toString())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()){

                                                        //savedata
                                                        PreferenceUtils.saveEmail(email.getEditText().getText().toString(),login.this);
                                                        PreferenceUtils.savePassword(password.getEditText().getText().toString(), login.this);

                                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                                        editor.putString("mail",email.getEditText().getText().toString() );


                                                        editor.apply();

                                                        progressDialog.dismiss();
                                                    }else{
                                                        Toast.makeText(login.this, "Incorrect login credentials", Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                }else{
                    startActivity(new Intent(login.this, error404.class));
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


    public void forgotPass(View view) {
        Intent intent = new Intent(login.this, forgetpass.class);
        startActivity(intent);

    }
    public void loginAction(View view){
        Intent intent = new Intent(login.this, signup.class);
        startActivity(intent);
    }




}


