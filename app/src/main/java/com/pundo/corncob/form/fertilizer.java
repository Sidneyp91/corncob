package com.pundo.corncob.form;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.pundo.corncob.model.fert;
import com.pundo.corncob.payment.payment;

import static com.pundo.corncob.auth.signup.MY_PREFS_NAME;

public class fertilizer extends AppCompatActivity {

    Toolbar toolbar;
    TextInputLayout plot, acres, yield, amount, date;
    EditText date2, bags;
    FirebaseDatabase database;
    DatabaseReference fertilizer_requests;
    LinearLayout planting, topdress;
    Button submit;
    Spinner region, type, crop, dressing, plant;
    private int mYear;
    private int mMonth;
    private int mDay;
    public int price = 1500;
    static final int DATE_DIALOG_ID = 0;
    public int divisor = 10;
    public int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer);

        //initialize views
        initViews();

        //Firebase
        database = FirebaseDatabase.getInstance();
        fertilizer_requests = database.getReference("Fertilizer Request");

        //Toolbar
        Apptoolbar();

        //sharedPref
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String mail = prefs.getString("mail", "");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Farm Details");
        ref.orderByChild("mail").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String plotNo = ds.child("plotNo").getValue(String.class);
                        String Acres = ds.child("acres").getValue(String.class);

                        plot.getEditText().setText(plotNo);
                        acres.getEditText().setText(Acres);

                    }

                } else {
                    Toast.makeText(fertilizer.this, "Farmer not verified", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Spinner
        spinners();


        //fertilizer example
        fertExample();

        //apply for fertilizer
        sendFert();

    }

    private void fertExample() {
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equalsIgnoreCase("Top Dressing Fertilizer")) {
                    topdress.setVisibility(View.VISIBLE);
                    planting.setVisibility(View.GONE);
                } else if (selectedItem.equalsIgnoreCase("Planting Fertilizer")) {
                    planting.setVisibility(View.VISIBLE);
                    topdress.setVisibility(View.GONE);
                } else {
                    topdress.setVisibility(View.GONE);
                    planting.setVisibility(View.GONE);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void sendFert() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(plot.getEditText().getText().toString()) || TextUtils.isEmpty(acres.getEditText().getText().toString()) ||
                        TextUtils.isEmpty(crop.getSelectedItem().toString()) ||
                        TextUtils.isEmpty(amount.getEditText().getText().toString())
                        || TextUtils.isEmpty(type.getSelectedItem().toString()) || TextUtils.isEmpty(region.getSelectedItem().toString())) {
                    Toast.makeText(fertilizer.this, "Fill in all fields to continue", Toast.LENGTH_LONG).show();


                } else {
                    final ProgressDialog dialog = new ProgressDialog(fertilizer.this);
                    dialog.setMessage("Submitting Application...");
                    dialog.show();


                    fertilizer_requests.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            final String plotNo = plot.getEditText().getText().toString();
                            final String acre = acres.getEditText().getText().toString();
                            final String crops = crop.getSelectedItem().toString();
                            final String amounts = amount.getEditText().getText().toString();
                            final String types = type.getSelectedItem().toString();
                            final String regions = region.getSelectedItem().toString();
                            final String example = dressing.getSelectedItem().toString();
                            final String example1 = plant.getSelectedItem().toString();

                            num = Integer.parseInt(acres.getEditText().getText().toString());

                            final int sac = (num) / (divisor);
                            bags.setText(Integer.toString(sac));


                            int amnt = Integer.parseInt(amounts);
                            if (amnt > sac) {

                                bags.setText(Integer.toString(sac));

                                Toast.makeText(fertilizer.this, "Amount needed changed to " + sac + ". 1 bag per 10 acre limit.", Toast.LENGTH_LONG).show();
                            }


                            int bag = Integer.parseInt(amount.getEditText().getText().toString());
                            final int price2 = price * bag;

                            dialog.dismiss();
                            new AlertDialog.Builder(fertilizer.this)
                                    .setTitle("Payment")
                                    .setMessage("You'll be required to pay " + price2 + " for " + bag + " bags of " + types + " to NCPB if awarded.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            fert save = new fert(plotNo, acre, crops, amounts, types, regions, example, example1);
                                            fertilizer_requests.child(plotNo).setValue(save);

                                            Toast.makeText(fertilizer.this, "Application submitted successfully", Toast.LENGTH_SHORT).show();
//                                            startActivity(new Intent(fertilizer.this, home.class));
//                                            finish();

                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
                                            ref.orderByKey().equalTo("fertilizer").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                            String bigText = ds.child("bigText").getValue(String.class);
                                                            String notification = ds.child("notification").getValue(String.class);
                                                            String title = ds.child("title").getValue(String.class);

                                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(fertilizer.this)
                                                                    .setSmallIcon(R.drawable.corn)
                                                                    .setContentTitle(title)
                                                                    .setContentText(notification)
                                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                                            .bigText(bigText))
                                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                                    .setAutoCancel(true);

                                                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                                            notificationManager.notify(1, builder.build());

                                                        }
                                                    } else {
                                                        //do nothing
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                            startActivity(new Intent(fertilizer.this, payment.class));
                                        }
                                    })
                                    .setIcon(R.drawable.cash_usd)

                                    .setNegativeButton(android.R.string.no, null)
                                    .setCancelable(true)
                                    .show();


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }


        });
    }


    private void spinners() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.counties, android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        region.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.type,
                android.R.layout.simple_spinner_dropdown_item);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        type.setAdapter(adapter2);

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.crop,
                android.R.layout.simple_spinner_dropdown_item);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        crop.setAdapter(adapter3);


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.topdress,
                android.R.layout.simple_spinner_dropdown_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dressing.setAdapter(adapter1);

        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.planting,
                android.R.layout.simple_spinner_dropdown_item);

        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        plant.setAdapter(adapter4);
    }

    private void Apptoolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mkulima Bora Fertilizers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initViews() {
        planting = findViewById(R.id.lyt_planting);
        topdress = findViewById(R.id.lyt_dressing);
        planting.setVisibility(View.GONE);
        topdress.setVisibility(View.GONE);
        plot = findViewById(R.id.plot);
        acres = findViewById(R.id.size);
        crop = findViewById(R.id.crop);
        yield = findViewById(R.id.sac);
        amount = findViewById(R.id.fert);
        type = findViewById(R.id.type);
        submit = findViewById(R.id.submit);
        region = findViewById(R.id.county);
        dressing = findViewById(R.id.dressing);
        plant = findViewById(R.id.planting);
        bags = findViewById(R.id.bags);
        toolbar = findViewById(R.id.home);
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
                Intent intent2 = new Intent(fertilizer.this, home.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent = new Intent(fertilizer.this, login.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Intent intent1 = new Intent(fertilizer.this, profile.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


}
