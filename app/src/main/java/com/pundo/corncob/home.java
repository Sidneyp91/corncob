package com.pundo.corncob;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.pundo.corncob.activity.about;
import com.pundo.corncob.activity.centers;
import com.pundo.corncob.activity.dates;
import com.pundo.corncob.activity.profile;
import com.pundo.corncob.auth.login;
import com.pundo.corncob.form.farmDetails;
import com.pundo.corncob.form.fertilizer;
import com.pundo.corncob.form.pickup;
import com.pundo.corncob.fragment.account_fragment;
import com.pundo.corncob.fragment.feed_fragment;
import com.pundo.corncob.fragment.notifications_fragment;

public class home extends AppCompatActivity {
    final Fragment fragment1 = new feed_fragment();
    final Fragment fragment2 = new notifications_fragment();
    final Fragment fragment3 = new account_fragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;
    Intent intent;
    TextView welcome, fertHead ,access;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcome = findViewById(R.id.welcome);
        access = findViewById(R.id.access);
        fertHead = findViewById(R.id.fertHead);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment1, "1").commit();



    }




    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;


                    switch (item.getItemId()) {
                        case R.id.feed:
                            fm.beginTransaction().hide(active).show(fragment1).commit();
                            active = fragment1;
                            return true;
                        case R.id.notification:
                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            return true;
                        case R.id.account:
                            fm.beginTransaction().hide(active).show(fragment3).commit();
                            active = fragment3;
                            return true;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };


    public void detailsAction(View view){
        Intent intent = new Intent (home.this, farmDetails.class);
        startActivity(intent);
    }

    public void loginAction(View view){
        Intent intent = new Intent (home.this, login.class);
        final ProgressDialog load = new ProgressDialog(home.this);
        load.setMessage("Logging out...");
        load.show();

        startActivity(intent);
        finish();
    }

    public void pickup(View view) {
        intent = new Intent(home.this, pickup.class);
        startActivity(intent);
    }


    public void apply(View view) {
        intent = new Intent(home.this, fertilizer.class);
        startActivity(intent);
        }

    public void centers(View view){
        intent = new Intent(home.this, centers.class);
        startActivity(intent);

    }
    public void Info(View view) {
        intent = new Intent(home.this, about.class);
        startActivity(intent);
    }

    public void dates(View view){
        intent = new Intent(home.this, dates.class);
        startActivity(intent);

    }


    public void profile(View view) {
        intent = new Intent(home.this, profile.class);
        startActivity(intent);
    }


}





