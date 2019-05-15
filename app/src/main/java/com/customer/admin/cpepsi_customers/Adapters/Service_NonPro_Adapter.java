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
import com.customer.admin.cpepsi_customers.Java_files.NonProModel;
import com.customer.admin.cpepsi_customers.R;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class Service_NonPro_Adapter extends RecyclerView.Adapter<Service_NonPro_Adapter.MyViewHolder> {
    private Context mContext;
    DownloadManager downloadManager;
    private Service_NonPro_Adapter service_nonPro_adapter;
    URL image_download_url;
    int pos_try;
    private String communStr = "http://heightsmegamart.com/CPEPSI/uploads/";
    // private String communStr = "http://heightsmegamart.com/CPEPSI/ ";

    DownloadManager.Request request;
    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();


    String service;

    public ArrayList<ApiModel> services_list;
    private ProgressDialog progressBar;
    private String id= "";
    private String Service= "";
    private String Image;
    String sId = "";

    public Service_NonPro_Adapter(Activity activity, ArrayList<ApiModel> services) {
        mContext = activity;
        this.services_list = services;

        setHasStableIds(true);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView service_doc;
        public ImageView animalDoctor;
        public LinearLayout mainclick;


        public MyViewHolder(View itemView) {
            super(itemView);
            service_doc = (TextView) itemView.findViewById(R.id.service_txt_id);
            mainclick = (LinearLayout) itemView.findViewById(R.id.mainclick);
            animalDoctor = (ImageView) itemView.findViewById(R.id.animalDoctor);

        }
    }

    public String Download_reports(String report) {

        return null;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_list, parent, false);


        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ApiModel apiModel = services_list.get(position);
        holder.service_doc.setText(apiModel.getService());

       /* this.pos_try = position;
        final Services services1;
        services1 = services_list.get(pos_try);
        Log.e("Position", "is " + pos_try);
        service = services1.getService_doc();
        StrictMode.setVmPolicy(builder.build());*/

        holder.mainclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiModel apiModel = services_list.get(position);
              /*  sId = apiModel.getId();
                AppPreference.setSid(mContext,sId);*/
                Intent intent = new Intent(mContext, After_service.class);
                intent.putExtra("ApiModel", apiModel);
                mContext.startActivity(intent);
              /*  Intent intent = new Intent(v.getContext(), After_service.class);
                intent.putExtra("ServiceId",id);
                intent.putExtra("ServiceName",Service);
                v.getContext().startActivity(intent);*/
            }
        });

        Image = apiModel.getImage();

        Picasso.with(mContext)
                .load(communStr + Image)
                .placeholder(R.drawable.doct1)
                .into(holder.animalDoctor);
//        holder.doc_date.setText(reports.getDoc_date());

        // notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return services_list.size();
    }


    @Override
    public long getItemId(int position) {
//        return super.getItemId(position);
        return position;
    }

    private class Get_The_Selected extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
