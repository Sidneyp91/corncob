package com.pundo.corncob.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pundo.corncob.R;

public class accountAdapter extends ArrayAdapter {
    ListView lv;
    private String[] deet;
    private int[] icons1;
    private Context context;
    private LayoutInflater layoutInflater;



    public accountAdapter(@NonNull Context context, String[] deet, int[] icons1) {
        super(context, R.layout.account, deet);

        this.deet = deet;
        this.context= context;
        this.icons1= icons1;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.account, null);
        }else{
            TextView deets = convertView.findViewById(R.id.accountlisting);
            ImageButton icons = convertView.findViewById(R.id.accounticon);

            deets.setText(deet[position]);
            icons.setImageResource(icons1[position]);
        }
        return convertView;
    }
}
