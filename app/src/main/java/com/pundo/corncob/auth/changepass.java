package com.pundo.corncob.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pundo.corncob.R;
import com.pundo.corncob.util.PreferenceUtils;

import static com.pundo.corncob.activity.profile.MY_PREFS_NAME;

public class changepass extends AppCompatActivity {

    TextInputLayout oldPass, newPass, newPass2;
    Button submit;
    DatabaseReference reference;
    String oldPassword;
    String newPassword;
    String newPasswor2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

        initViews();
        changePassword();
    }


    private void initViews() {
        oldPass = findViewById(R.id.oldPass1);
        newPass = findViewById(R.id.newPass);
        newPass2 = findViewById(R.id.newPass2);
        submit = findViewById(R.id.change);


    }
    private void changePassword() {
          oldPassword = oldPass.getEditText().getText().toString().trim();
          newPassword = newPass.getEditText().getText().toString().trim();
          newPasswor2 = newPass2.getEditText().getText().toString().trim();

submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        final ProgressDialog dialog = new ProgressDialog(changepass.this);
        dialog.setMessage("Please wait ...");
        dialog.show();
        if(!TextUtils.equals(newPasswor2,newPassword)){
            Toast.makeText(changepass.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }else{
            SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String name = sharedPreferences.getString("name","notfound");


            reference = FirebaseDatabase.getInstance().getReference("Users").child(name);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        reference.child("password").setValue(newPassword);
                        dialog.dismiss();
                        Toast.makeText(changepass.this, "Password change successful, Kindly login", Toast.LENGTH_SHORT).show();
                        PreferenceUtils.savePassword(null,changepass.this);
                        PreferenceUtils.saveEmail(null,changepass.this);
                        startActivity(new Intent(changepass.this, login.class));
                        finish();
                    }else{
                        Toast.makeText(changepass.this, "Unable to change password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(changepass.this, "Unable to change n" +databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
});
            }


}
