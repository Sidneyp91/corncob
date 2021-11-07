package com.pundo.corncob.form;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pundo.corncob.R;
import com.pundo.corncob.activity.profile;
import com.pundo.corncob.auth.login;
import com.pundo.corncob.errors.error404;
import com.pundo.corncob.errors.verification;
import com.pundo.corncob.home;
import com.pundo.corncob.model.farm;
import com.pundo.corncob.util.PreferenceUtils;

import java.io.IOException;
import java.util.UUID;

import static com.pundo.corncob.auth.signup.MY_PREFS_NAME;


public class farmDetails extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 234;
    Spinner crop, county;
    FirebaseDatabase database;
    DatabaseReference farm_details;
    String storage_path = "images/";
    TextInputLayout plot, id, acres, bags, fertilizer, price, phone;
    EditText  click;
    String imgurl;
    Button submit, upload;
    Toolbar toolbar;
    Boolean verified= false;
    Uri filePath;
    StorageReference storageReference;
    ImageView uploadDoc, uploadDoc1;
    public  Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_details);

        initViews();

        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        farm_details = database.getReference("Farm Details");


        //Toolbar
        toolbar = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Farm Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });




        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showfile();
            }
        });

        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.counties, android.R.layout.simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        county.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.crop,
                android.R.layout.simple_spinner_dropdown_item);

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        crop.setAdapter(adapter3);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(haveNetwork()) {
                    if (TextUtils.isEmpty(plot.getEditText().getText().toString()) || TextUtils.isEmpty(id.getEditText().getText().toString()) ||
                            TextUtils.isEmpty(county.getSelectedItem().toString())
                            || TextUtils.isEmpty(crop.getSelectedItem().toString()) || TextUtils.isEmpty(bags.getEditText().getText().toString())
                            || TextUtils.isEmpty(price.getEditText().getText().toString()) || TextUtils.isEmpty(acres.getEditText().getText().toString())) {


                        Toast.makeText(farmDetails.this, "Fill in all fields", Toast.LENGTH_SHORT).show();
                    }

                    final ProgressDialog dialog = new ProgressDialog(farmDetails.this);
                    dialog.setMessage("Saving...");
                    dialog.show();


                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("phoneNumber", phone.getEditText().getText().toString());
                    editor.putString("region", county.getSelectedItem().toString());
                    editor.putString("plotNo", plot.getEditText().getText().toString());
                    editor.putString("email", id.getEditText().getText().toString());
                    editor.putString("acres",acres.getEditText().getText().toString());
                    editor.apply();

                    farm_details.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(id.getEditText().getText().toString()).exists()) {
                                dialog.dismiss();
                                Toast.makeText(farmDetails.this, "Farm exists", Toast.LENGTH_SHORT).show();

                            } else {
                                final String plotNo = plot.getEditText().getText().toString();
                                final String number = id.getEditText().getText().toString();
                                final String counties = county.getSelectedItem().toString();
                                final String crops = crop.getSelectedItem().toString();
                                final String sacs = bags.getEditText().getText().toString();
                                final String acre = acres.getEditText().getText().toString();
                                final String fertilizers = fertilizer.getEditText().getText().toString();
                                final String prices = price.getEditText().getText().toString();
                                final String phones = phone.getEditText().getText().toString();
                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                final String mail = prefs.getString("mail", "No name defined");
                                final String name = prefs.getString("name", "");
                                uploadFile(bitmap);
                                dialog.dismiss();

                                farm save = new farm(plotNo, name, mail, number, acre, counties, crops, sacs, fertilizers, prices, phones, verified,imgurl);
                                farm_details.child(plotNo).setValue(save);
                                Toast.makeText(farmDetails.this, "Details saved successfully.", Toast.LENGTH_SHORT).show();
                                vett();


                          /*  if(!verified){
                                Intent intent = new Intent(farmDetails.this, verification.class );
                                startActivity(intent);
                                finish();
                            }
                            startActivity(new Intent(farmDetails.this, home.class));
                            finish();*/
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }else{
                    startActivity(new Intent(farmDetails.this, error404.class));
                }
            }
        });
    }

    private void initViews() {
        plot = findViewById(R.id.plot);
        id = findViewById(R.id.IdNumber);
        acres = findViewById(R.id.size);
        county = findViewById(R.id.county);
        crop = findViewById(R.id.crop);
        bags = findViewById(R.id.sac);
        fertilizer = findViewById(R.id.fert);
        phone = findViewById(R.id.phone);
        click =findViewById(R.id.click);
        price = findViewById(R.id.price);
        submit = findViewById(R.id.submit);
        upload = findViewById(R.id.upload);
        uploadDoc = findViewById(R.id.uploadDoc);
        uploadDoc1 = findViewById(R.id.uploadDoc2);
    }

    public String GetFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    private void showfile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"),PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData() != null){
            filePath = data.getData();


            try {
               bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadDoc.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//check if farmer is vetted
    private  void vett(){
        //initialize firebase reference
        farm_details = FirebaseDatabase.getInstance().getReference().child("verified");
        //get value of verified field
        farm_details.orderByChild("verified").equalTo(verified)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check if entry exists
                        if (dataSnapshot.exists()) {
                            Intent intent = new Intent(farmDetails.this, verification.class);
                            startActivity(intent);
                            //send notification for vetting
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Notifications");
                            ref.orderByKey().equalTo("vetting").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                                            String bigText = ds.child("bigText").getValue(String.class);
                                            String notification = ds.child("notification").getValue(String.class);
                                            String title = ds.child("title").getValue(String.class);

                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(farmDetails.this)
                                                    .setSmallIcon(R.drawable.corn)
                                                    .setContentTitle(title)
                                                    .setContentText(notification)
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                            .bigText(bigText))
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setAutoCancel(true);


                                            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.notify(1,  builder.build());

                                        }
                                    }else{
                                        //do nothing
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Intent intent = new Intent(farmDetails.this, home.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    private void upload() {
        StorageReference riversRef = storageReference.child(storage_path+ System.currentTimeMillis() + "." + GetFileExtension(filePath));

        riversRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

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
                Intent intent2 = new Intent(farmDetails.this, home.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                PreferenceUtils.savePassword(null,farmDetails.this);
                PreferenceUtils.saveEmail(null,farmDetails.this);
                Intent intent = new Intent(farmDetails.this, login.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.profile:
                Intent intent1 = new Intent(farmDetails.this, profile.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
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



    private void uploadFile(Bitmap bitmap) {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imgurl = String.valueOf(taskSnapshot.getStorage().getDownloadUrl());

                            Toast.makeText(farmDetails.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(farmDetails.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());

                        }
                    });
        }

    }
}
