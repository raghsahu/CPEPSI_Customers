package com.customer.admin.cpepsi_customers;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Adapters.Recycler_Payment_Adapter;
import com.customer.admin.cpepsi_customers.Adapters.Service_Recycler_Adapter;
import com.customer.admin.cpepsi_customers.Java_files.ApiModel;
import com.customer.admin.cpepsi_customers.Java_files.Pay_History_Model;
import com.customer.admin.cpepsi_customers.util.AppPreference;

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

public class Payment_History extends AppCompatActivity {

    RecyclerView recyclerView_pay_history;
    ArrayList<Pay_History_Model> Payment_List = new ArrayList<>();
    Recycler_Payment_Adapter recycler_payment_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment__history);

        recyclerView_pay_history=findViewById(R.id.recycler_view_payment);

        if (Connectivity.isNetworkAvailable(Payment_History.this)) {
            new Get_Payment_History().execute();


        } else {
            Toast.makeText(Payment_History.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

    }

    private class Get_Payment_History extends AsyncTask<Void, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Payment_History.this);
            dialog.show();

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/user_payment");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", AppPreference.getId(Payment_History.this));

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

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if (result != null) {

                Log.e("PostRegistration", result.toString());

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    String response = jsonObject.getString("responce");
                    if (response.equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Service_json_object = jsonArray.getJSONObject(i);
                            String ph_id = Service_json_object.getString("ph_id");
                            String user_id = Service_json_object.getString("user_id");
                            String amount = Service_json_object.getString("amount");
                            String remark = Service_json_object.getString("remark");
                            String payment_type = Service_json_object.getString("payment_type");
                            String datetime = Service_json_object.getString("datetime");
                            String provider_name = Service_json_object.getString("provider_name");
                            String service_sub = Service_json_object.getString("service_sub");

                            Payment_List.add(0,new Pay_History_Model(ph_id, user_id, amount, remark, payment_type,datetime,provider_name,service_sub));
                        }

                        recycler_payment_adapter = new Recycler_Payment_Adapter(Payment_History.this, Payment_List);
                         RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Payment_History.this);
                         recyclerView_pay_history.setLayoutManager(mLayoutManager);
                        recyclerView_pay_history.setItemAnimator(new DefaultItemAnimator());
                        recyclerView_pay_history.setAdapter(recycler_payment_adapter);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(result);
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

    }
}
