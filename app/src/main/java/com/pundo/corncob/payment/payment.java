package com.pundo.corncob.payment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;
import com.google.android.material.textfield.TextInputLayout;
import com.pundo.corncob.R;
import com.pundo.corncob.activity.profile;
import com.pundo.corncob.auth.login;
import com.pundo.corncob.form.fertilizer;
import com.pundo.corncob.home;


public class payment extends AppCompatActivity {
    Toolbar toolbar;
    TextInputLayout number;
    Button pay;
    Daraja daraja;


    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        //Toolbar
        toolbar = findViewById(R.id.home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Make Payment");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //initializing
        pay = findViewById(R.id.pay);
        number = findViewById(R.id.number);


        //initialize daraja
        daraja = Daraja.with("2SY5hGZp3xRUqeFG4AXzdUu6thb4Kf9b", "YF2HrxgYTOcf9np7", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(payment.this.getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(payment.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(payment.this.getClass().getSimpleName(), error);
            }
        });


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = number.getEditText().getText().toString();

                final ProgressDialog dialog = new ProgressDialog(payment.this);
                dialog.setMessage("Processing payment ...");
                dialog.show();

                if (TextUtils.isEmpty(phoneNumber)) {
//                editTextPhoneNumber.setError("Please Provide a Phone Number");
                    Toast.makeText(payment.this, "Provide phone number", Toast.LENGTH_SHORT).show();
                } else {

                    LNMExpress lnmExpress = new LNMExpress(
                            "174379",
                            "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                            TransactionType.CustomerPayBillOnline,
                            "10",
                            "254791666364",
                            "174379",
                            phoneNumber,
                            "http://mycallbackurl.com/checkout.php",
                            "Corncob",
                            "Fertilizer Application Fee"
                    );

                    daraja.requestMPESAExpress(lnmExpress,
                            new DarajaListener<LNMResult>() {
                                @Override
                                public void onResult(@NonNull LNMResult lnmResult) {
//                                Log.i(payment.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                                    Toast.makeText(payment.this, "A request has been sent", Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(getApplicationContext(), home.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("notifications_fragment", "MyFragment");
                                    PendingIntent pendingIntent = PendingIntent.getActivity(payment.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(payment.this)
                                                    .setSmallIcon(R.drawable.corn)
                                                    .setContentTitle("Application received")
                                                    .setContentText("Your fertilizer application has been received, we'll keep you posted.")
                                                    .setStyle(new NotificationCompat.BigTextStyle())
                                                    .setContentIntent(pendingIntent)
                                                    .setAutoCancel(true);
                                    mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(1, mBuilder.build());


                                    dialog.dismiss();
                                    Intent intent1 = new Intent(payment.this, fertilizer.class);
                                    startActivity(intent1);
                                    finish();


                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(payment.this, "Unable to process payment", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
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
                Intent intent2 = new Intent(payment.this, home.class);
                startActivity(intent2);
                return true;
            case R.id.logout:
                Intent intent = new Intent(payment.this, login.class);
                startActivity(intent);
                return true;
            case R.id.profile:
                Intent intent1 = new Intent(payment.this, profile.class);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("result>>>", String.valueOf(resultCode));
    }
}