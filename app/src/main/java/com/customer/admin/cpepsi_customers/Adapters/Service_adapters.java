package com.customer.admin.cpepsi_customers.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.customer.admin.cpepsi_customers.Java_files.Services;
import com.customer.admin.cpepsi_customers.R;

import java.util.List;

public class Service_adapters extends BaseAdapter {

    Activity context;
    //    ArrayList<String> title;
    String description[];
    public List<Services> servicesList;

    public Service_adapters(Activity context, List<Services> servicesList) {
        super();
        this.context = context;
        this.servicesList = servicesList;
//        this.description = description;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return servicesList.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView service_name;
//        TextView txtViewDescription;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.services_list, null);
            holder = new ViewHolder();
            holder.service_name = (TextView) convertView.findViewById(R.id.service_txt_id);
//            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.service_name.setText(servicesList.);
//        holder.txtViewDescription.setText(description[position]);

        return convertView;
    }

}