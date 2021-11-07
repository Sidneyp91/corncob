package com.pundo.corncob.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pundo.corncob.R;
import com.pundo.corncob.auth.changepass;
import com.pundo.corncob.auth.login;
import com.pundo.corncob.form.farmDetails;
import com.pundo.corncob.home;
import com.pundo.corncob.util.PreferenceUtils;

import static com.pundo.corncob.auth.signup.MY_PREFS_NAME;

public class profile extends AppCompatActivity {
    Intent intent;
    Toolbar toolbar;
    LinearLayout pass;
    ImageView imageView;
    TextView names, emails, number, county, plotNo;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        names = findViewById(R.id.name);
        emails = findViewById(R.id.email);
        number = findViewById(R.id.phone);
        county = findViewById(R.id.region);
        plotNo = findViewById(R.id.plot);

        pass = findViewById(R.id.passChange);



        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String name = prefs.getString("mail", "");



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Farm Details");

        final String pln = reference.getKey();

        reference.orderByChild("mail").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        String uName = ds.child("name").getValue(String.class);
                        String uCounty = ds.child("county").getValue(String.class);
                        String uNumber = ds.child("phones").getValue(String.class);
                        String uMail = ds.child("mail").getValue(String.class);

                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("county",uCounty);
                        editor.putString("name",uName);
                        editor.apply();
                        emails.setText(uMail);
                        number.setText(uNumber);
                        county.setText(uCounty);
                        names.setText(uName);



                    }
                }else{
                    Toast.makeText(profile.this, "Try signing in again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*
        plotNo.setText(plot);
        county.setText(region);
        number.setText(phone);
        name.setText(jina);
*/

        //Profile Image
        imageView = findViewById(R.id.profile_image);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.account);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
        roundedBitmapDrawable.setCircular(true);
        imageView.setImageDrawable(roundedBitmapDrawable);


            pass.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                intent = new Intent(profile.this, changepass.class);
                startActivity(intent);
            }
        });

        //Toolbar
        toolbar = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });


    }

    public void imageUpload(View view){

    }

    public void detailsAction(View view) {
        Intent intent = new Intent(profile.this, farmDetails.class);
        startActivity(intent);
    }

    public void editPhone(View view) {


    }

    public void reports(View view) {
        Intent intent = new Intent(profile.this, home.class);
        Toast.makeText(this, "Activity under Construction", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.dots, menu);

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.home:
                Intent intent2 = new Intent(profile.this, home.class);
                startActivity(intent2);
                return true;
            case R.id.profile:
                Intent intent1 = new Intent(profile.this, profile.class);
                startActivity(intent1);
                return true;
            case R.id.logout:
                PreferenceUtils.savePassword(null,profile.this);
                PreferenceUtils.saveEmail(null,profile.this);
                Intent intent = new Intent(profile.this, login.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
