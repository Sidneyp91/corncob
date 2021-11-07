package com.pundo.corncob.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pundo.corncob.R;
import com.pundo.corncob.util.Funds;
import com.pundo.corncob.util.News;
import com.pundo.corncob.util.News2;

public class feed_fragment extends BottomSheetDialogFragment {

    Dialog dialog;
    ImageView close;
    private BottomSheetDialogFragment bottomSheetDialog;
    Context context;
    private View rootView;
    private CardView cardView, cardView2, cardView3;
    BottomSheetBehavior sheetBehavior;
    private DatabaseReference mDatabase;
    private TextView  fertHead, pickupHead, marketHead, otherHead, otherBody;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.feed_fragment, container, false);

        CoordinatorLayout coordinatorLayout = rootView.findViewById(R.id.a);




        cardView = rootView.findViewById(R.id.name);
        cardView2 = rootView.findViewById(R.id.maize);
        cardView3 = rootView.findViewById(R.id.funds);
        fertHead = rootView.findViewById(R.id.fertHead);
        pickupHead = rootView.findViewById(R.id.pickupHead);
        marketHead = rootView.findViewById(R.id.marketHead);
        otherHead = rootView.findViewById(R.id.otherHead);
        otherBody = rootView.findViewById(R.id.otherBody);
//get Fertilizer data
        DatabaseReference fert = FirebaseDatabase.getInstance().getReference("Feeds/Fertilizer");

        fert.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String title = ds.child("title").getValue(String.class);
                    fertHead.setText(title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get pickup news


        DatabaseReference pickup = FirebaseDatabase.getInstance().getReference("Feeds/Pickup");

        pickup.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps: dataSnapshot.getChildren()){
                    String title = ps.child("title").getValue(String.class);
                    pickupHead.setText(title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //get market news
        DatabaseReference market = FirebaseDatabase.getInstance().getReference("Feeds/Market");

        market.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps: dataSnapshot.getChildren()){
                    String title = ps.child("title").getValue(String.class);
                    marketHead.setText(title);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //other news
        DatabaseReference other = FirebaseDatabase.getInstance().getReference("Feeds/Other");

        other.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ps: dataSnapshot.getChildren()){
                    String title = ps.child("title").getValue(String.class);
                    String body = ps.child("body").getValue(String.class);
                    otherHead.setText(title);
                    otherBody.setText(body);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                news();
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                news2();
            }
        });

        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Funds();
            }
        });
         return rootView;


    }

    private void news(){
        News bottomSheetFragment = new News();
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

         }

    private void news2(){
        News2 bottomSheetFragment = new News2();
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

    }
    private void Funds(){
        Funds bottomSheetFragment = new Funds();
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }

}
