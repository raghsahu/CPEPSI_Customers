package com.customer.admin.cpepsi_customers.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Adapters.NotificationAdapter;
import com.customer.admin.cpepsi_customers.Connectivity;
import com.customer.admin.cpepsi_customers.Java_files.NotificationModel;
import com.customer.admin.cpepsi_customers.R;
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

public class Pending_fragment extends Fragment implements View.OnClickListener {
    RecyclerView recyclerNotification;
    ArrayList<NotificationModel> noti_list = new ArrayList<>();
    private NotificationAdapter notificationAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View allView = inflater.inflate(R.layout.fragment_pending, container, false);

        recyclerNotification = (RecyclerView) allView.findViewById(R.id.recyclerNotification);

        if (Connectivity.isNetworkAvailable(getActivity())) {
            new PostNotification().execute();
        }else {
            Toast.makeText(getActivity(), "No Internet", Toast.LENGTH_SHORT).show();
        }
        return allView;

    }
    @Override
    public void onClick(View v) {

    }

    public class PostNotification extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Custapprovedecline");
            //    URL url = new URL("https://www.paramgoa.com/cpepsi/api/Custapprovedecline");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("customer_id", AppPreference.getId(getContext()));

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

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        StringBuffer Ss = sb.append(line);
                        Log.e("Ss", Ss.toString());
                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                dialog.dismiss();

                JSONObject jsonObject = null;
                String s = result.toString();
                try {
                    jsonObject = new JSONObject(result);
                    String responce = jsonObject.getString("responce");
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String pr_id = dataObj.getString("pr_id");
                        String customer_id = dataObj.getString("customer_id");
                        String provider_id = dataObj.getString("provider_id");
                        String discription = dataObj.getString("discription");
                        String date = dataObj.getString("date");
                        String Prostatus = dataObj.getString("Prostatus");
                        String user_id = dataObj.getString("user_id");
                        String TypeofFirm = dataObj.getString("TypeofFirm");
                        String Designation = dataObj.getString("Designation");
                        String business = dataObj.getString("business");
                        String City = dataObj.getString("City");
                        String state = dataObj.getString("state");
                        String place = dataObj.getString("place");
                        String number = dataObj.getString("number");
                        String name = dataObj.getString("name");
                        String dob = dataObj.getString("dob");
                        String adharno = dataObj.getString("adharno");
                        String middle = dataObj.getString("middle");
                        String sirname = dataObj.getString("sirname");
                        String emailid = dataObj.getString("emailid");
                        String password = dataObj.getString("password");
                        String service = dataObj.getString("service");
                        String sub_service = dataObj.getString("sub_service");
                        String status = dataObj.getString("status");
                        String image = dataObj.getString("image");
                        if (Prostatus.equals("0")) {
                            noti_list.add(0,new NotificationModel(pr_id, customer_id, provider_id, discription, date,Prostatus,user_id
                                    ,TypeofFirm,Designation,business,City,state,place,number,name,dob,adharno,middle,sirname,emailid,
                                    password,service,sub_service,status,image));
                        }
                    }

                    notificationAdapter = new NotificationAdapter(getContext(), noti_list);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerNotification.setLayoutManager(mLayoutManager);
                    recyclerNotification.setItemAnimator(new DefaultItemAnimator());
                    recyclerNotification.setAdapter(notificationAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
