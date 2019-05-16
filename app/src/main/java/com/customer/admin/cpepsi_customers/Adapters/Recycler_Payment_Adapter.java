package com.customer.admin.cpepsi_customers.Adapters;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.customer.admin.cpepsi_customers.After_service;
import com.customer.admin.cpepsi_customers.Java_files.ApiModel;
import com.customer.admin.cpepsi_customers.Java_files.Pay_History_Model;
import com.customer.admin.cpepsi_customers.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class Recycler_Payment_Adapter extends RecyclerView.Adapter<Recycler_Payment_Adapter.MyViewHolder> {
    private Context mContext;
    private Recycler_Payment_Adapter recycler_payment_adapter;
    private String communStr = "http://heightsmegamart.com/CPEPSI/uploads/";

    public ArrayList<Pay_History_Model> Payment_list;
    private String Image;

    public Recycler_Payment_Adapter(Activity activity, ArrayList<Pay_History_Model> services) {
        mContext = activity;
        this.Payment_list = services;

        setHasStableIds(true);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_date,txt_pay_mode,txt_amount,txt_description,txt_proname;



        public MyViewHolder(View itemView) {
            super(itemView);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_pay_mode = (TextView) itemView.findViewById(R.id.txt_payment_mode);
            txt_amount = (TextView) itemView.findViewById(R.id.amount);
            txt_proname = (TextView) itemView.findViewById(R.id.txt_pro_name);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Pay_History_Model pay_history_model = Payment_list.get(position);

        holder.txt_date.setText(pay_history_model.getDatetime());
        holder.txt_pay_mode.setText(pay_history_model.getPayment_type());
        holder.txt_amount.setText(pay_history_model.getAmount());
        holder.txt_proname.setText(pay_history_model.getProvider_name());
        holder.txt_description.setText(pay_history_model.getRemark());

    }


    @Override
    public int getItemCount() {
        return Payment_list.size();
    }


    @Override
    public long getItemId(int position) {

        return position;
    }


}
