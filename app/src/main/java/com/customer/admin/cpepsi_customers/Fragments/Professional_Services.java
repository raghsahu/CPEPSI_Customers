package com.customer.admin.cpepsi_customers.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.customer.admin.cpepsi_customers.Adapters.Service_Recycler_Adapter;
import com.customer.admin.cpepsi_customers.Java_files.ApiModel;
import com.customer.admin.cpepsi_customers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Professional_Services extends Fragment implements Service_Recycler_Adapter.Service_Recycler_Adapter_Listener{
    RecyclerView ser_list_view;
    TextView service_txt_id;
    private Service_Recycler_Adapter service_recycler_adapter;
    private PopupWindow service_popup_window;

    ArrayList<ApiModel> serviceList = new ArrayList<>();
    private LinearLayoutManager layoutManager_in;
    int Service_id = 1;
//    ArrayList<String> Service_names=new ArrayList<String>();
//    ArrayList[] Service_names ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        final View ser_view = inflater.inflate(R.layout.professional_services, container, false);
        ser_list_view = (RecyclerView) ser_view.findViewById(R.id.ser_list_view);
        service_txt_id = (TextView) ser_view.findViewById(R.id.service_txt_id);
//        service_recycler_adapter = new Service_Recycler_Adapter(getActivity() , serviceList);
        layoutManager_in = new LinearLayoutManager(getActivity());

        new Get_All_Profossional_Services(ser_view, Service_id).execute();
        service_recycler_adapter = new Service_Recycler_Adapter(getActivity(), serviceList, Professional_Services.this);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        ser_list_view.setLayoutManager(manager);


//
//        ser_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ser_list_view = (ListView) ser_view.findViewById(R.id.ser_list_view);
////                ser_list_view = null;
////                ser_list_view.setAdapter(service_adapter);
//                String service = ser_list_view.getItemAtPosition(position).toString();
//
//                Snackbar.make(ser_view , "Item name is"+service,Toast.LENGTH_LONG +1).show();
//            }
//        });


        return ser_view;
    }

    @Override
    public void onItemSelected(ApiModel apiModel) {

    }

    private class Get_All_Profossional_Services extends AsyncTask<Void, Void, String> {
        int service_id;
        ProgressDialog dialog;
        int size_of_services = 0;
        View v;

        public Get_All_Profossional_Services(View v, int service_txt_id) {
            this.service_id = service_txt_id;
            this.v = v;

        }


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.show();

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... voids) {

            try {


                URL url = new URL("http://jntrcpl.com/CPEPSI/api/Get_Services");
              //  URL url = new URL("https://www.paramgoa.com/cpepsi/api/Get_Services");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("type", service_id);
//                postDataParams.put("password", password);

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        result.append(line);
                    }
                    r.close();
                    return result.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        public String getPostDataString(JSONObject params) throws Exception {

            StringBuilder result = new StringBuilder();
            boolean first = true;

            Iterator<String> itr = params.keys();

            while (itr.hasNext()) {

                String key = itr.next();
                Object value = params.get(key);

                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(key, "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(value.toString(), "UTF-8"));

            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (result != null) {
                //  dialog.dismiss();


                Log.e("PostRegistration", result.toString());

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    boolean response = jsonObject.getBoolean("responce");
                    if (response) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Service_json_object = jsonArray.getJSONObject(i);
                            String id = Service_json_object.getString("id");
                            String Service = Service_json_object.getString("Service");
                            String type = Service_json_object.getString("type");
                            String image = Service_json_object.getString("image");
                            String status = Service_json_object.getString("status");
                            serviceList.add(new ApiModel(id,Service,type,image,status));

                            size_of_services++;

                        }
                        if (size_of_services == jsonArray.length()) {

                            ser_list_view.setAdapter(service_recycler_adapter);
                        }


//                        Service_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, R.id.service_txt_id, Service_names);

//                        ser_list_view.deferNotifyDataSetChanged();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(result);
        }

    }
}
