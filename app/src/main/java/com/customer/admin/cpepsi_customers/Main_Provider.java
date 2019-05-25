package com.customer.admin.cpepsi_customers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Adapters.Service_NonPro_Adapter;
import com.customer.admin.cpepsi_customers.Adapters.Service_Pro_Adapter;
import com.customer.admin.cpepsi_customers.Adapters.Service_Recycler_Adapter;
import com.customer.admin.cpepsi_customers.Fragments.Free_Services;
import com.customer.admin.cpepsi_customers.Fragments.Non_Professional_Services;
import com.customer.admin.cpepsi_customers.Fragments.Professional_Services;
import com.customer.admin.cpepsi_customers.Java_files.ApiModel;
import com.customer.admin.cpepsi_customers.Java_files.NonProModel;
import com.customer.admin.cpepsi_customers.Java_files.ProModel;
import com.customer.admin.cpepsi_customers.util.AppPreference;
import com.customer.admin.cpepsi_customers.util.SessionManager;
import com.squareup.picasso.Picasso;

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
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Response;


public class Main_Provider extends AppCompatActivity implements Service_Recycler_Adapter.Service_Recycler_Adapter_Listener {

    private TextView mTextMessage;
    android.support.v4.widget.NestedScrollView scro_pro;
    Button topup;
    Toolbar toolbarHome;
    CardView card1, card2, card3;
    LinearLayout profService, freeService, techService;
    SessionManager manager;

    TextView search_service;
    String Status_Plan;
    String Id;

    RecyclerView recycler_freeservice, recycler_professional, recycler_technical;

    ArrayList<ApiModel> serviceList = new ArrayList<>();
    ArrayList<ApiModel> MainModelArrayList = new ArrayList<>();
    Service_Recycler_Adapter service_recycler_adapter;

    ArrayList<ApiModel> serviceProList = new ArrayList<>();
    Service_Pro_Adapter service_Pro_adapter;

    ArrayList<ApiModel> serviceNonProList = new ArrayList<>();
    Service_NonPro_Adapter service_NonPro_adapter;

    ApiModel ApiModel;

    //************************************************************
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent to_home = new Intent(Main_Provider.this, Main_Provider.class);
                    startActivity(to_home);
                    finish();
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.free_services:
                    Free_Services free_services = new Free_Services();
                    Load_Free_Services(free_services);
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.non_professional:
                    Non_Professional_Services non_professional_services = new Non_Professional_Services();
                    Load_Non_Professional_Services(non_professional_services);
//                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.professional_tools:
                    Professional_Services professional_services = new Professional_Services();
                    LoadProfessional_Services(professional_services);
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;

            }
            return false;
        }
    };

    private void Load_Free_Services(Free_Services free_services) {
//        scro_pro.setVisibility(View.GONE);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        ExampleFragments fragment = new ExampleFragments();
        fragmentTransaction.replace(R.id.provider_frame, new Free_Services());
        fragmentTransaction.commit();
    }

    private void Load_Non_Professional_Services(Non_Professional_Services non_professional_services) {
//        scro_pro.setVisibility(View.GONE);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        ExampleFragments fragment = new ExampleFragments();
        fragmentTransaction.replace(R.id.provider_frame, new Non_Professional_Services());
        fragmentTransaction.commit();
    }

    private void LoadProfessional_Services(Professional_Services professional_services) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        ExampleFragments fragment = new ExampleFragments();
        fragmentTransaction.replace(R.id.provider_frame, new Professional_Services());
        fragmentTransaction.commit();
    }
//********************************************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__provider);

        recycler_freeservice = findViewById(R.id.recycler_freeservice);
        search_service = findViewById(R.id.search_service);


//********************************************************
        search_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Main_Provider.this,Service_Filter_Activity.class);
                startActivity(intent);

            }
        });
        // recycler_professional=findViewById(R.id.recycler_professional);
        // recycler_technical=findViewById(R.id.recycler_technical);

        if (Connectivity.isNetworkAvailable(Main_Provider.this)) {
            new Get_All_Non_Free_Services(3).execute();
            new Get_All_Non_Profossional_Services(2).execute();
            new Get_All_Profossional_Services(1).execute();

        } else {
            Toast.makeText(Main_Provider.this, "No Internet", Toast.LENGTH_SHORT).show();
        }
        ////*****************************************************

        if (Connectivity.isNetworkAvailable(Main_Provider.this)) {
            new CheckFirstPaymentStatusm().execute();
        }else {
            Toast.makeText(Main_Provider.this, "No Internet", Toast.LENGTH_SHORT).show();
        }

//*******************************************************************************
        if (ActivityCompat.checkSelfPermission(Main_Provider.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(Main_Provider.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {

        }

        manager = new SessionManager(this);

//         toolbarHome = (Toolbar) findViewById(R.id.toolbarHome);
//        setSupportActionBar(toolbarHome);

        // scro_pro = (android.support.v4.widget.NestedScrollView) findViewById(R.id.scro_pro);
        //     mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //   topup = (Button)findViewById(R.id.topup);

        final Animation right_to_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_to_left);
        //    final Animation left_to_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.left_to_right);

//        profService = (LinearLayout)findViewById(R.id.profService);
//        profService.startAnimation(right_to_left);
//
//        freeService = (LinearLayout)findViewById(R.id.freeService);
//        freeService.startAnimation(right_to_left);
//
//        techService = (LinearLayout)findViewById(R.id.techService);
//        techService.startAnimation(right_to_left);
//
//        card1 = (CardView)findViewById(R.id.card1);
//        card2 = (CardView)findViewById(R.id.card2);
//        card3 = (CardView)findViewById(R.id.card3);
//
//        card1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FragmentManager fm = getSupportFragmentManager();
//                Professional_Services fragment = new Professional_Services();
//                fm.beginTransaction().add(R.id.provider_frame,fragment).commit();
//            }
//        });
//
//        card2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getSupportFragmentManager();
//                Free_Services fragment = new Free_Services();
//                fm.beginTransaction().add(R.id.provider_frame,fragment).commit();
//            }
//        });
//
//        card3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentManager fm = getSupportFragmentManager();
//                Non_Professional_Services fragment = new Non_Professional_Services();
//                fm.beginTransaction().add(R.id.provider_frame,fragment).commit();
//            }
//        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        MenuItem item1 = menu.findItem(R.id.action_prof);
        MenuItem item2 = menu.findItem(R.id.action_p_history);
        MenuItem item3 = menu.findItem(R.id.action_notification);
        MenuItem item4 = menu.findItem(R.id.action_login);
        if (manager.isLoggedIn()) {
            item.setVisible(true);
            item1.setVisible(true);
            item2.setVisible(true);
            item3.setVisible(true);
            item4.setVisible(false);
        } else {
            item.setVisible(false);
            item1.setVisible(false);
            item2.setVisible(false);
            item3.setVisible(false);
            item4.setVisible(true);

        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomePrev/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_notification) {
            if (manager.isLoggedIn()) {
                Intent intent = new Intent(Main_Provider.this, Notification.class);
                startActivity(intent);
                //finish();
                return true;
            } else {
                Toast.makeText(this, "Please Login First", Toast.LENGTH_SHORT).show();
            }


        } else if (id == R.id.action_settings) {

            final AlertDialog.Builder dialog = new AlertDialog.Builder(Main_Provider.this).setTitle("CPEPSI")
                    .setMessage("Are you sure, you want to logout this app");

            dialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {
                    exitLauncher();
                }

                private void exitLauncher() {
                    manager.logoutUser();
                    manager.setAfterName(null);
                    AppPreference.setAfterId(getApplicationContext(), "null");
                    Intent intent = new Intent(Main_Provider.this, Login_Constomer.class);
                    startActivity(intent);
                    finish();
                }
            });
            final AlertDialog alert = dialog.create();
            alert.show();

            return true;

        } else if (id == R.id.action_prof) {
            Intent intent = new Intent(Main_Provider.this, ProfileActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.action_share) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
            return true;
        } else if (id == R.id.action_p_history) {
            Intent intent = new Intent(Main_Provider.this, Payment_History.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_login) {
            Intent intent = new Intent(Main_Provider.this, Login_Constomer.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(ApiModel apiModel) {
        Toast.makeText(Main_Provider.this, "Selected: " + apiModel.getService(), Toast.LENGTH_SHORT).show();

    }

    private class Get_All_Non_Free_Services extends AsyncTask<Void, Void, String> {
        int service_id;
        ProgressDialog dialog;

        public Get_All_Non_Free_Services(int service_txt_id) {
            this.service_id = service_txt_id;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Main_Provider.this);
            dialog.show();

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_Services");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("type", service_id);

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

                            serviceList.add(new ApiModel(id, Service, type, image, status));
                            MainModelArrayList.add(new ApiModel(id, Service, type, image, status));

                        }

                        service_recycler_adapter = new Service_Recycler_Adapter(Main_Provider.this, serviceList, Main_Provider.this);

                        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Main_Provider.this);
                        GridLayoutManager manager = new GridLayoutManager(Main_Provider.this, 3, GridLayoutManager.VERTICAL, false);
                        recycler_freeservice.setLayoutManager(manager);
                        recycler_freeservice.setItemAnimator(new DefaultItemAnimator());
                        recycler_freeservice.setAdapter(service_recycler_adapter);

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

    //************************************************************
    private class Get_All_Non_Profossional_Services extends AsyncTask<Void, Void, String> {
        int service_id;
        ProgressDialog dialog;
        int size_of_services = 0;

        public Get_All_Non_Profossional_Services(int service_txt_id) {
            this.service_id = service_txt_id;

        }


        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(Main_Provider.this);
            dialog.show();

            super.onPreExecute();

        }


        @Override
        protected String doInBackground(Void... voids) {

            try {


                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_Services");
                //   URL url = new URL("https://www.paramgoa.com/cpepsi/api/Get_Services");

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

                            serviceNonProList.add(new ApiModel(id, Service, type, image, status));
                            MainModelArrayList.add(new ApiModel(id, Service, type, image, status));
//                        size_of_services++;
//
                        }
//                    if (size_of_services == jsonArray.length()) {
//
//                        ser_list_view_other.setAdapter(service_recycler_adapter);
//                    }

                        service_recycler_adapter = new Service_Recycler_Adapter(Main_Provider.this, serviceNonProList, Main_Provider.this);

                        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Main_Provider.this);
                        GridLayoutManager manager = new GridLayoutManager(Main_Provider.this, 3, GridLayoutManager.VERTICAL, false);
                        recycler_freeservice.setLayoutManager(manager);
                        recycler_freeservice.setItemAnimator(new DefaultItemAnimator());
                        recycler_freeservice.setAdapter(service_recycler_adapter);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onPostExecute(result);
            }

        }
    }

    //************************************************************
    private class Get_All_Profossional_Services extends AsyncTask<Void, Void, String> {
        int service_id;
        ProgressDialog dialog;
        int size_of_services = 0;
        View v;

        public Get_All_Profossional_Services(int service_txt_id) {
            this.service_id = service_txt_id;
            this.v = v;

        }


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Main_Provider.this);
            dialog.show();

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... voids) {

            try {


                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_Services");
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

                            serviceProList.add(new ApiModel(id, Service, type, image, status));
                            MainModelArrayList.add(new ApiModel(id, Service, type, image, status));

                        }
                        service_recycler_adapter = new Service_Recycler_Adapter(Main_Provider.this, serviceProList, Main_Provider.this);
                        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Main_Provider.this);
                        GridLayoutManager manager = new GridLayoutManager(Main_Provider.this, 3, GridLayoutManager.VERTICAL, false);
                        recycler_freeservice.setLayoutManager(manager);
                        recycler_freeservice.setItemAnimator(new DefaultItemAnimator());
                        recycler_freeservice.setAdapter(service_recycler_adapter);

                        service_recycler_adapter = new Service_Recycler_Adapter(Main_Provider.this, MainModelArrayList, Main_Provider.this);
                        GridLayoutManager manager2 = new GridLayoutManager(Main_Provider.this, 3, GridLayoutManager.VERTICAL, false);
                        recycler_freeservice.setLayoutManager(manager2);
                        recycler_freeservice.setItemAnimator(new DefaultItemAnimator());
                        recycler_freeservice.setAdapter(service_recycler_adapter);

                        // fetchService();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(result);
        }


    }

    private class CheckFirstPaymentStatusm extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;


        protected void onPreExecute() {
            dialog = new ProgressDialog(Main_Provider.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/Api/first_payment_check");
                //   URL url = new URL("http://paramgoa.com/cpepsi/Api/first_payment_check");

                JSONObject postDataParams = new JSONObject();

                Id = AppPreference.getId(Main_Provider.this);

                postDataParams.put("user_id", Id);
                Log.e("Id", Id);

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
                Log.e("PostPI", result.toString());
                try {

                    jsonObject = new JSONObject(result);
                    String res = jsonObject.getString("responce");
                    Toast.makeText(Main_Provider.this, "chk+ "+res, Toast.LENGTH_SHORT).show();

                    if (res.equals("false")) {
                        String error=jsonObject.getString("error");
                        final String charge_amount=jsonObject.getString("charge_amount");

                        final AlertDialog.Builder dialog = new AlertDialog.Builder(Main_Provider.this).setTitle("CPEPSI")
                                .setMessage(""+error +", Please Pay Rs "+charge_amount +", Your Paid service Amount Only One Time.");

                        dialog.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialog.setPositiveButton("Pay amount", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                exitLauncher();
                            }

                            private void exitLauncher() {
                                Intent intent = new Intent(Main_Provider.this, FirstTime_Payment_Activity.class);
                                intent.putExtra("ApiModel", ApiModel);
                                intent.putExtra("charge_amount", charge_amount);
                                startActivity(intent);
                                // finish();
                            }
                        });
                        final AlertDialog alert = dialog.create();
                        alert.show();

                        //**********************************************************
                        // Toast.makeText(After_service.this, ""+error, Toast.LENGTH_SHORT).show();
                        Toast.makeText(Main_Provider.this, "Please Pay First Payment, Your Paid service Amount Only One Time",
                                Toast.LENGTH_LONG).show();



                    } else {

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
