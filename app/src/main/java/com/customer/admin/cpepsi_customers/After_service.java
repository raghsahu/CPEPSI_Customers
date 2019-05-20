package com.customer.admin.cpepsi_customers;


import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Java_files.ApiModel;
import com.customer.admin.cpepsi_customers.Java_files.DataModel;
import com.customer.admin.cpepsi_customers.util.AppPreference;
import com.customer.admin.cpepsi_customers.util.SessionManager;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class After_service extends AppCompatActivity {
    Button next_ser;
    EditText service_ser, problem,address;
    int service_from_main;
    Spinner serviceType;
    ArrayList<String> uomList;
    private ArrayList<DataModel> uomSpinnerList;
    private DataModel dataModel;
    private ArrayAdapter<String> uomAdapter;
    String ServiceItem;
    String Id;
    //public static String Problem;
    String Problem = "";
    int sss;
    String Service_Name;
    String strId;
    private int strNew;
    SessionManager manager;
    String After_SerName;
    public ApiModel apiModel;
   // public NonProModel apiModel;
    String Adss;

    public HashMap<Integer, DataModel> SubServiceId_HasMap=new HashMap<Integer, DataModel>();
     String Service_Sub_ID;
     Geocoder geocoder;
     List<Address> event_address;
     LatLng p1;
    String Address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_service);

        Adss =AppPreference.getAddress(After_service.this);
        Log.e("Adss",Adss);
        manager = new SessionManager(this);

        next_ser = (Button) findViewById(R.id.next_ser);
        service_ser = findViewById(R.id.service_ser);
        serviceType = (Spinner) findViewById(R.id.serviceType);
        problem = (EditText) findViewById(R.id.problem);
        address = (EditText) findViewById(R.id.address);
       // address.setText(Adss);
        Address=address.getText().toString();

        problem.setText(AppPreference.getProblem(After_service.this));
        apiModel = (ApiModel) getIntent().getSerializableExtra("ApiModel");
        if (getIntent() != null) {
            if (AppPreference.getAfterID(getApplicationContext()).equals("null")) {


                try {
                    strId = apiModel.getId();
                    String SerName = apiModel.getService();
                    String Type = apiModel.getType();
                    String Status = apiModel.getStatus();
                    Log.e("Rajkumar bhai", "");
                    service_ser.setText(SerName);
                    if (!AppPreference.getAfterID(getApplicationContext()).equals("null"))
                        strNew = Integer.parseInt(AppPreference.getAfterID(getApplicationContext()));

                    else {
                        strNew = Integer.parseInt(strId);
                    }
                } catch (Exception nfe) {
                    //  strId = getIntent().getStringExtra("strid");
                    System.out.println("Could not parse " + nfe);
                }


                After_SerName = service_ser.getText().toString();

            } else {
                try {
                    strId = apiModel.getId();

                    String SerName = apiModel.getService();
                    String Type = apiModel.getType();
                    String Status = apiModel.getStatus();
                    Log.e("Rajkumar with", "");
                  //  Toast.makeText(this, ""+strId, Toast.LENGTH_SHORT).show();

                    service_ser.setText(SerName);
                    After_SerName = service_ser.getText().toString();

                } catch (Exception e) {
                    strId = String.valueOf(AppPreference.getAfterID(getApplicationContext()));
                    service_ser.setText(manager.isAfter_servie_Name());
                    e.printStackTrace();
                }

            }


        }

        uomList = new ArrayList<>();
        uomSpinnerList = new ArrayList<>();

        if (Connectivity.isNetworkAvailable(this)) {
            if (!AppPreference.getAfterID(getApplicationContext()).equals("null")) {
                // strId = String.valueOf(AppPreference.getAfterID(getApplicationContext()));
                try {
                    if (!strId.isEmpty()) {
                        new ServiceData().execute();
                    }
                } catch (Exception e) {
                    //  strId=getIntent().getStringExtra("after_id");
                    // AppPreference.setAfterId(getApplicationContext(),"null");
                    new ServiceData().execute();
                }


            } else {
                new ServiceData().execute();
            }

        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }
//***************************************************************
        serviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ServiceItem = uomAdapter.getItem(position).toString();

                for (int i = 0; i < SubServiceId_HasMap.size(); i++)
                {
                   // Toast.makeText(After_service.this, "item pos "+serviceType.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                    if (SubServiceId_HasMap.get(i).getServiceSubCategory().equals(serviceType.getItemAtPosition(position)))
                    {
                        Service_Sub_ID=SubServiceId_HasMap.get(i).getId();
                         Toast.makeText(After_service.this, "subId "+Service_Sub_ID, Toast.LENGTH_SHORT).show();
                    }else
                    {
                        //Toast.makeText(After_service.this, "nonono", Toast.LENGTH_SHORT).show();
                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      /*  service_from_main = getIntent().getIntExtra("ServiceId", 0);
        Service_Name = getIntent().getStringExtra("ServiceName");*/

        next_ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loc_on_mark(address.getText().toString()) != null) {
                    LatLng cust_location = loc_on_mark(address.getText().toString());
                    Toast.makeText(After_service.this, "cust_loc "+cust_location, Toast.LENGTH_SHORT).show();
                }
               // Toast.makeText(After_service.this, "Add "+address.getText().toString(), Toast.LENGTH_SHORT).show();

                if (manager.isLoggedIn()) {

                    Problem = problem.getText().toString();
                    AppPreference.setProblem(After_service.this, Problem);
                    if (!ServiceItem.equals("--Select--")) {
                        if (!Problem.equals("")) {
                            new CheckFirstPaymentStatus(Service_Sub_ID).execute();
                        } else {
                            problem.setError("Please Enter Problem.");
                            problem.requestFocus();
                        }
                    } else {
                        Toast.makeText(After_service.this, "Please Select", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AppPreference.setAfterId(After_service.this, strId);
                    // AppPreference.setAfterName(After_service.this,After_SerName);
                    Toast.makeText(After_service.this, "" + strId, Toast.LENGTH_SHORT).show();
                    manager.setAfterName(After_SerName);
                    Toast.makeText(After_service.this, "" + After_SerName, Toast.LENGTH_SHORT).show();

                    Toast.makeText(After_service.this, "Please login first", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(After_service.this, Login_Constomer.class);
                    startActivity(intent);
                    finish();
                }

            }
        });


       /* if (Service_Name != 0) {
            service_ser.setText(Service_Name);
        }*/

    }
//*******************************************************************
    private LatLng loc_on_mark(String address) {

        String address1="452011";
        geocoder = new Geocoder(After_service.this, Locale.getDefault());
        try {
            event_address = geocoder.getFromLocationName(address, 5);
            // Toast.makeText(GET_Service_providers.this, "add "+event_address, Toast.LENGTH_SHORT).show();
            Log.e("log123" , "destination "+address);

            if (event_address.isEmpty()) {
                return null;
            } else {
                Address location = event_address.get(0);
                location.getLatitude();
                location.getLongitude();
                p1 = new LatLng(location.getLatitude(), location.getLongitude());

            }
            Toast.makeText(After_service.this, "latlong "+p1, Toast.LENGTH_SHORT).show();
        }

        catch (IOException e) {
            Toast.makeText(After_service.this, "catch+ "+e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return p1;

    }

    //------------------------------------------------------


    public class ServiceData extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(After_service.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Get_SubServices");
                //URL url = new URL("https://www.paramgoa.com/cpepsi/api/Get_SubServices");

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("Servicesid", strId);
                Log.e("strId", strId);
                //  Log.e("sss>>>",sss+"");

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
                Log.e("PostPIinsuranceDetails", result.toString());
                try {

//                    uomSpinnerList.add(new DataModel("", "", "","",""));
                    uomList.add("--Select--");
                    jsonObject = new JSONObject(result);
                    String responce = jsonObject.getString("responce");
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject Obj = dataArray.getJSONObject(i);
                        String id = Obj.getString("id");
                        String Service = Obj.getString("Service");
                        String type = Obj.getString("type");
                        String ServiceSubCategory = Obj.getString("ServiceSubCategory");
                        String status = Obj.getString("status");
                        uomList.add(ServiceSubCategory);
                        uomSpinnerList.add(new DataModel(id, Service, type, ServiceSubCategory, status));

                        SubServiceId_HasMap.put(i,new DataModel(id, Service, type, ServiceSubCategory, status));

                    }

                    uomAdapter = new ArrayAdapter<String>(After_service.this, R.layout.spinner_row, uomList);
                    uomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    serviceType.setAdapter(uomAdapter);


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

    //*************************************************
    public class CheckFirstPaymentStatus extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;
        String Ser_Sub_ID;

        public CheckFirstPaymentStatus(String service_sub_id) {
            this.Ser_Sub_ID=service_sub_id;

        }

        protected void onPreExecute() {
            dialog = new ProgressDialog(After_service.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/Api/first_payment_check");
            //   URL url = new URL("http://paramgoa.com/cpepsi/Api/first_payment_check");

                JSONObject postDataParams = new JSONObject();

                Id = AppPreference.getId(After_service.this);

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

                    if (res.equals("false")) {
                        Toast.makeText(After_service.this, "Please pay Rs 20, Your Paid service Amount only one time",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(After_service.this, FirstTime_Payment_Activity.class);
                        intent.putExtra("ApiModel", apiModel);
                        startActivity(intent);
                        // finish();


                    } else {
                        Intent to_map = new Intent(After_service.this, GET_Service_providers.class);
                        to_map.putExtra("service_id", strNew);
                        Log.e("strNew?????", strNew + "");
                        to_map.putExtra("Problem", Problem);
                        to_map.putExtra("ServiceSub", ServiceItem);
                       to_map.putExtra("SubSer_ID",Ser_Sub_ID);
                        startActivity(to_map);
                        finish();
                        // AppPreference.setAfterId(getApplicationContext(),"null");
                        //      Snackbar.make(After_service.this, "We are working on it", Snackbar.LENGTH_LONG);
                        Toast.makeText(After_service.this, "We are working on it", Toast.LENGTH_SHORT).show();
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