package com.pundo.corncob.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.pundo.corncob.util.PreferenceUtils;

import static com.pundo.corncob.activity.profile.MY_PREFS_NAME;

public class dates extends AppCompatActivity {
    Toolbar toolbar;
    CalendarView calendarView;
    TextView view, begin, end, pickup, yieldEnd;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dates);

        //initViews
        view = findViewById(R.id.deadline);
        begin = findViewById(R.id.beginsupply);
        end = findViewById(R.id.endsupply);
        pickup = findViewById(R.id.yieldStart);
        yieldEnd = findViewById(R.id.yieldEnd);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Feeds/Dates");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    String AppDead = ds.child("Application Deadline").getValue(String.class);
                    String supStart = ds.child("Supply Start").getValue(String.class);
                    String supEnd = ds.child("Supply End").getValue(String.class);
                    String pickStart = ds.child("Yield Start").getValue(String.class);
                    String pickEnd = ds.child("Yeild End").getValue(String.class);

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE).edit();
                    editor.putString("supS",supStart);
                    editor.putString("supE",supEnd);
                    editor.putString("pickS",pickStart);
                    editor.putString("pickE",pickEnd);
                    editor.apply();


                    view.setText(AppDead);
                    begin.setText(supStart);
                    end.setText(supEnd);
                    pickup.setText(pickStart);
                    yieldEnd.setText(pickEnd);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        toolbar = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Important dates");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Calendar view
        calendarView = findViewById(R.id.dates);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int d = dayOfMonth;
                int m = month;
                int y = year;

                String curDate = String.valueOf(d);
                String curD = String.valueOf(m);
                String curDa = String.valueOf(y);

                String date = curD + "/" + curDate + "/" + curDa;
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Feeds/Dates");

                mRef.orderByChild("Application Deadline").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            new AlertDialog.Builder(dates.this)
                                    .setTitle("")
                                    .setMessage("Application Deadline")
                                    .setPositiveButton(android.R.string.yes,null)
                                    .setIcon(android.R.drawable.ic_menu_my_calendar)
                                    .show();
                        }else{
                            new AlertDialog.Builder(dates.this)
                                    .setTitle("")
                                    .setMessage("No event")
                                    .setPositiveButton(android.R.string.yes,null)
                                    .setIcon(android.R.drawable.ic_menu_my_calendar)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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
                Intent intent2 = new Intent(dates.this, home.class);
                startActivity(intent2);
                return true;
            case R.id.profile:
                Intent intent1 = new Intent(dates.this, profile.class);
                startActivity(intent1);
                return true;
            case R.id.logout:
                PreferenceUtils.savePassword(null,dates.this);
                PreferenceUtils.saveEmail(null,dates.this);
                Intent intent = new Intent(dates.this, login.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
