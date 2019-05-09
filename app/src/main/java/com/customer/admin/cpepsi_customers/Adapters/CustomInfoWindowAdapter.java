package com.customer.admin.cpepsi_customers.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.customer.admin.cpepsi_customers.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by User on 10/2/2017.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.provider_info, null);
    }


    private void rendowWindowText(final Marker marker, View view) {

        String title = marker.getTitle();
        String another_snip = marker.getSnippet();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        TextView name = (TextView) view.findViewById(R.id.name);
//
//        if(!another_snip.equals("snip")){
//            name.setText(another_snip);
//        }
        try {

            if (!title.equals("")) {
                tvTitle.setText(title);
            }

            String snippet = marker.getSnippet();
            TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);

            if (!snippet.equals("")) {
                tvSnippet.setText(snippet);
            }
        } catch (Exception e) {

        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);

        return mWindow;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        rendowWindowText(marker, mWindow);
        mWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "snip ", Toast.LENGTH_SHORT).show();
            }
        });

        return mWindow;
    }


}
