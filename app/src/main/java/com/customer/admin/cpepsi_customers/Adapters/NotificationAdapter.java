package com.customer.admin.cpepsi_customers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.admin.cpepsi_customers.Java_files.NotificationModel;
import com.customer.admin.cpepsi_customers.R;
import com.customer.admin.cpepsi_customers.StatusAct;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>   {

    private static final String TAG = "NotificationAdapter";
    private ArrayList<NotificationModel> notilist;
    public Context context;
    String resId = "";
    String finalStatus = "";

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView providerName, description,status,date,pro_service_name;
        CardView card;
        ImageView recpdeleteBtn;
        int pos;

        public ViewHolder(View view) {
            super(view);

            providerName = (TextView) view.findViewById(R.id.pro_nam);
            pro_service_name = (TextView) view.findViewById(R.id.pro_service);
            description = (TextView) view.findViewById(R.id.description);
            status = (TextView) view.findViewById(R.id.status);
            date = (TextView) view.findViewById(R.id.date);
            card = (CardView) view.findViewById(R.id.card);
        }
    }

    public static Context mContext;

    public NotificationAdapter(Context mContext, ArrayList<NotificationModel> noti_list) {
        context = mContext;
        notilist = noti_list;

    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new NotificationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.ViewHolder viewHolder, final int position) {
        NotificationModel notificationModel = notilist.get(position);
        viewHolder.date.setText(notificationModel.getDate());
        viewHolder.pro_service_name.setText(notificationModel.getService_sub());
        viewHolder.description.setText("Service Query: "+notificationModel.getDiscription());


        finalStatus = (notificationModel.getProstatus());
        if (finalStatus.equals("0")) {
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.orange));
            viewHolder.status.setText("Pending...");
        } else if (notificationModel.getProstatus().equals("1")) {
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.color2));
            viewHolder.status.setText("Accept...");
            viewHolder.providerName.setText(notificationModel.getName());
        } else if (notificationModel.getProstatus().equals("2")) {
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.color1));
            viewHolder.status.setText("Decline...");
        } else if (notificationModel.getProstatus().equals("3")) {
            viewHolder.status.setTextColor(context.getResources().getColor(R.color.newone));
            viewHolder.status.setText("Completed...");
            viewHolder.providerName.setText(notificationModel.getName());
        }
        viewHolder.card.setTag(viewHolder);
        viewHolder.pos = position;

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationModel notificationModel1 = notilist.get(position);
                Intent intent = new Intent(context, StatusAct.class);
                intent.putExtra("NotificationModel", notificationModel1);
                context.startActivity(intent);
               // ((Activity)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notilist.size();
    }

}
