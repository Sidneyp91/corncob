package com.pundo.corncob.form;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pundo.corncob.R;
import com.pundo.corncob.activity.profile;
import com.pundo.corncob.auth.login;
import com.pundo.corncob.home;
import com.pundo.corncob.model.pick;

import static android.content.Context.MODE_PRIVATE;
import static com.pundo.corncob.auth.signup.MY_PREFS_NAME;

public class pickup extends AppCompatActivity {

    Spinner crop, county;
    TextInputLayout acres,  bags, plot;
    EditText date2;
    Button apply;
    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference pickup_request;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int start;
    private String status = "false";
    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        //database
        database = FirebaseDatabase.getInstance();
        pickup_request = database.getReference("Pickup Requests");

        //toolbar
        toolbar = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yeild Pickup");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        plot = findViewById(R.id.plot);
        acres = findViewById(R.id.size);
        bags = findViewById(R.id.sac);
        crop = findViewById(R.id.crop);
        county = findViewById(R.id.county);
        apply = findViewById(R.id.submit);


        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.counties, android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        county.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.crop,
                android.R.layout.simple_spinner_dropdown_item);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        crop.setAdapter(adapter3);

        //setSharedPref
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        final String mail = prefs.getString("mail","");

        //get values
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Farm Details");
        ref.orderByChild("mail").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        String size = ds.child("acres").getValue(String.class);
                        String plotNo = ds.child("plotNo").getValue(String.class);
                        String crop = ds.child("crop").getValue(String.class);

                        plot.getEditText().setText(plotNo);
                        acres.getEditText().setText(size);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(plot.getEditText().getText().toString()) || TextUtils.isEmpty(acres.getEditText().getText().toString()
                )  || TextUtils.isEmpty(bags.getEditText().getText().toString())
                || TextUtils.isEmpty(crop.getSelectedItem().toString()) || TextUtils.isEmpty(county.getSelectedItem().toString())){
                    Toast.makeText(pickup.this, "Fill in en", Toast.LENGTH_SHORT).show();

                }


                final ProgressDialog dialog = new ProgressDialog(pickup.this);
                dialog.setMessage("Submitting Application");
                dialog.show();


                    pickup_request.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dialog.dismiss();


                            final String plots = plot.getEditText().getText().toString();
                            final String acre = acres.getEditText().getText().toString();
                            final String bag = bags.getEditText().getText().toString();
                            final String crops = crop.getSelectedItem().toString();
                            final String counties = county.getSelectedItem().toString();

                            pick save = new pick(plots, acre, bag, counties, crops, status);
                            pickup_request.child(plots).setValue(save);
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
                            ref.orderByKey().equalTo("pickup").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                                            String bigText = ds.child("bigText").getValue(String.class);
                                            String notification = ds.child("notification").getValue(String.class);
                                            String title = ds.child("title").getValue(String.class);


                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(pickup.this)
                                                    .setSmallIcon(R.drawable.corn)
                                                    .setContentTitle(title)
                                                    .setContentText(notification)
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                            .bigText(bigText))
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setAutoCancel(true);

                                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(1,  builder.build());
                                            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                            editor.putString("notificationID",ds.child("id").getValue(String.class));
                                            editor.apply();
                                            startActivity(new Intent(pickup.this, home.class));
                                            Toast.makeText(pickup.this, "Application for delivery submitted successfully",Toast.LENGTH_LONG).show();
                                            finish();

                                        }
                                    }else{
                                        //do nothing
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(pickup.this,"" + databaseError, Toast.LENGTH_SHORT).show();

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(pickup.this, "" +databaseError, Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.dots, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                Intent intent2 = new Intent(pickup.this, home.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent = new Intent(pickup.this, login.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Intent intent1 = new Intent(pickup.this, profile.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public void listDates(){


    }


}