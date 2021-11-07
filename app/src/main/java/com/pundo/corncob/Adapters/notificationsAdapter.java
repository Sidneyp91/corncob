package com.pundo.corncob.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pundo.corncob.R;
import com.pundo.corncob.model.notificationMessage;
import com.pundo.corncob.model.notifications;

import java.util.List;

public class notificationsAdapter extends RecyclerView.Adapter<notificationsAdapter.ViewHolder> {
    private List<notificationMessage> notificationList;

    public notificationsAdapter(List<notificationMessage> notificationList, Context context) {
        this.notificationList = notificationList;
    }
    @NonNull
    @Override
    public notificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View  view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        notificationMessage nModel = notificationList.get(position);
        holder.bind(nModel);

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView notH;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            notH = itemView.findViewById(R.id.notificationH);
        }

        public void bind(notificationMessage nModel) {

            notH.setText(nModel.getMessage());


        }
    }




}
