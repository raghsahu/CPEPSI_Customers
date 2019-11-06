package com.customer.admin.cpepsi_customers;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Adapters.NotificationAdapter;
import com.customer.admin.cpepsi_customers.Adapters.PagerView;
import com.customer.admin.cpepsi_customers.Adapters.ViewPagerAdapter;
import com.customer.admin.cpepsi_customers.Fragments.Accept_fragment;
import com.customer.admin.cpepsi_customers.Fragments.All_fragment;
import com.customer.admin.cpepsi_customers.Fragments.Complete_fragment;
import com.customer.admin.cpepsi_customers.Fragments.Pending_fragment;
import com.customer.admin.cpepsi_customers.Fragments.decline_fragment;
import com.customer.admin.cpepsi_customers.Java_files.NotificationModel;
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

public class Notification extends AppCompatActivity implements TabLayout.BaseOnTabSelectedListener {
   // RecyclerView recyclerNotification;
//    ArrayList<NotificationModel> noti_list;
//    private NotificationAdapter notificationAdapter;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

         tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.pager);

         setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.setCurrentItem(0);

    }

    private void setupViewPager(ViewPager viewPager) {
//        tabLayout.addTab(tabLayout.newTab().setText("All"));
//        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
//        tabLayout.addTab(tabLayout.newTab().setText("Accept"));
//        tabLayout.addTab(tabLayout.newTab().setText("Decline"));
//        tabLayout.addTab(tabLayout.newTab().setText("Complete"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        PagerView adapter = new PagerView(getSupportFragmentManager(), tabLayout.getTabCount());



        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new All_fragment(), "All");
        adapter.addFragment(new Pending_fragment(), "Pending");
        adapter.addFragment(new Accept_fragment(), "Accept");
        adapter.addFragment(new decline_fragment(), "Decline");
        adapter.addFragment(new Complete_fragment(), "Complete");


        viewPager.setAdapter(adapter);

    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Fragment fragment=null;
        switch (tabLayout.getTabCount()) {

        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //------------------------------------------------------

    public class PostNotification extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(Notification.this);
            dialog.show();
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://jntrcpl.com/CPEPSI/api/Custapprovedecline");
               // URL url = new URL("https://www.paramgoa.com/cpepsi/api/Custapprovedecline");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("customer_id", AppPreference.getId(Notification.this));

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
                    for (int i=0; i<dataArray.length(); i++){
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String pr_id = dataObj.getString("pr_id");
                        String customer_id = dataObj.getString("customer_id");
                        String provider_id = dataObj.getString("provider_id");
                        String discription = dataObj.getString("discription");
                        String Prostatus = dataObj.getString("Prostatus");
                       // noti_list.add(new NotificationModel(pr_id,customer_id,provider_id,discription,Prostatus));
                    }

//                    notificationAdapter = new NotificationAdapter(Notification.this, noti_list);
//                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Notification.this);
//                    recyclerNotification.setLayoutManager(mLayoutManager);
//                    recyclerNotification.setItemAnimator(new DefaultItemAnimator());
//                    recyclerNotification.setAdapter(notificationAdapter);

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

    //------------------------------------------------------

}
