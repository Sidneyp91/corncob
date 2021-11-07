package com.pundo.corncob.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pundo.corncob.R;
import com.pundo.corncob.util.PreferenceUtils;

import static android.content.Context.MODE_PRIVATE;
import static com.pundo.corncob.auth.signup.MY_PREFS_NAME;


public class account_fragment extends Fragment{

    TextView  access;
    Button login;
    DatabaseReference dbase;
    LinearLayout  profile;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View account = inflater.inflate(R.layout.account_fragment, container,false);
        login = account.findViewById(R.id.login_signup);
        access = account.findViewById(R.id.access);
        profile = account.findViewById(R.id.profile);
        dbase = FirebaseDatabase.getInstance().getReference();

        final SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("mail", "");
        access.setText(name);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.savePassword(null,getContext());
                PreferenceUtils.saveEmail(null,getContext());
                Intent intent = new Intent(getContext(), com.pundo.corncob.auth.login.class);
                startActivity(intent);

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.pundo.corncob.activity.profile.class);
                startActivity(intent);
            }
        });
        return account;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
