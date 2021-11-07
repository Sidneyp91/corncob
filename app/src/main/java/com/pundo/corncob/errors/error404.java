package com.pundo.corncob.errors;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.pundo.corncob.R;

public class error404 extends AppCompatActivity {
    private LinearLayout lyt_no_connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error404);

        initComponent();
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


    private void initComponent() {

        lyt_no_connection = findViewById(R.id.lyt_no_connection);
        MaterialButton bt_retry = findViewById(R.id.retry);


        bt_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ProgressDialog dialog = new ProgressDialog(error404.this);
                dialog.setMessage("Retrying ...");
                dialog.show();
                if (haveNetwork()){
                    lyt_no_connection.setVisibility(View.GONE);
                    dialog.dismiss();
                    finish();
                }else if (!haveNetwork()){
                    lyt_no_connection.setVisibility(View.GONE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lyt_no_connection.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        }
                    }, 1000);
                }
            }
        });
    }
}
