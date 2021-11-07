package com.pundo.corncob.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pundo.corncob.Adapters.notificationsAdapter;
import com.pundo.corncob.R;
import com.pundo.corncob.model.notificationMessage;
import com.pundo.corncob.model.notifications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.pundo.corncob.auth.signup.MY_PREFS_NAME;

public class notifications_fragment extends Fragment {


    Context context;
    private List<notificationMessage> notificationsList;
    private RecyclerView recyclerView;
    private notificationsAdapter notAdapter;

    public notifications_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View notifications = inflater.inflate(R.layout.notifications_fragment, container, false);

        recyclerView = notifications.findViewById(R.id.notificationView);

        return notifications;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        notificationsList = new ArrayList<notificationMessage>();

        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
         String xxx = prefs.getString("mail", null);

         DatabaseReference notRef = FirebaseDatabase.getInstance().getReference("Farm Details");
         notRef.orderByChild("mail").equalTo(xxx).addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()){
                     for(DataSnapshot ds: dataSnapshot.getChildren()){
                         String plotDig = ds.child("plotNo").getValue(String.class);

                         final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications/" +plotDig);
                         databaseReference.addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                 if (dataSnapshot.exists()) {
                                     for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                         notificationMessage message = dataSnapshot1.getValue(notificationMessage.class);
                                         notificationsList.add(message);
//                       class notificationMessage ss = new notificationMessage(message);
//                        notificationsList.add(ss);

                                     }
                                     notAdapter = new notificationsAdapter(notificationsList, context);
                                     recyclerView.setAdapter(notAdapter);

                                     Log.d("Notif", "onDataChange: " + notificationsList.get(0).toString());


                                     for (int i = 0; i < notificationsList.size(); i++) {

                                         Log.d("Notif", "onDataChange: " + notificationsList.get(i).toString());
                                     }

                                 }

                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });

                     }
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });




    }
}
