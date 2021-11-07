package com.pundo.corncob.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pundo.corncob.R;
import com.pundo.corncob.home;
import com.pundo.corncob.auth.login;

import static com.pundo.corncob.auth.signup.MY_PREFS_NAME;

public class centers extends AppCompatActivity {
    Toolbar toolbar;
    TextView county, center1,center2,center3,center4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centers);

        county = findViewById(R.id.county);
        center1 = findViewById(R.id.center1);
        center2 = findViewById(R.id.center2);
        center3 = findViewById(R.id.center3);
        center4 = findViewById(R.id.center4);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String region = prefs.getString("county", "No county entry");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Centers");

        reference.orderByKey().equalTo(region).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String center= ds.child("center1").getValue(String.class);
                        String center0 = ds.child("center2").getValue(String.class);
                        String center9 = ds.child("center3").getValue(String.class);
                        String center8 = ds.child("center4").getValue(String.class);

                        county.setText(region);
                        center1.setText(center);
                        center2.setText(center0);
                        center3.setText(center9);
                        center4.setText(center8);
                    }
                }else{
                    Toast.makeText(centers.this, "error retreiving centers",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toolbar = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("PickUp centers");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(centers.this, home.class);
                startActivity(intent);
                finish();
            }
        });

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
                Intent intent2 = new Intent(centers.this, home.class);
                startActivity(intent2);
                return true;
            case R.id.profile:
                Intent intent1 = new Intent(centers.this, profile.class);
                startActivity(intent1);
                return true;
            case R.id.logout:
                Intent intent = new Intent(centers.this, login.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
