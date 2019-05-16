package com.customer.admin.cpepsi_customers.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.customer.admin.cpepsi_customers.Java_files.SearchModel;
import com.customer.admin.cpepsi_customers.R;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private static final String TAG = "SearchAdapter";
    private ArrayList<SearchModel> serchlist;
    public Context context;
    private String communStr = "http://heightsmegamart.com/CPEPSI/uploads/";
   // private String communStr = "https://paramgoa.com/satsung//uploads/";

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView placeText;
        int pos;

        public ViewHolder(View view) {
            super(view);

            placeText = (TextView) view.findViewById(R.id.placeText);

        }
    }

    public static Context mContext;

    public SearchAdapter(Context mContext, ArrayList<SearchModel> search_List) {
        context = mContext;
        serchlist = search_List;

    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);

        return new SearchAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchAdapter.ViewHolder viewHolder, final int position) {
        SearchModel searchModel = serchlist.get(position);
        viewHolder.placeText.setText(searchModel.getPlace());
        viewHolder.pos = position;

        viewHolder.placeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
    }

    @Override
    public int getItemCount() {
        return serchlist.size();
    }

}
