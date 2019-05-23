package com.customer.admin.cpepsi_customers;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
//import instamojo.library.InstapayListener;
//import instamojo.library.InstamojoPay;
//import instamojo.library.Config;
import org.json.JSONObject;
import org.json.JSONException;

import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.customer.admin.cpepsi_customers.util.AppPreference;
import com.customer.admin.cpepsi_customers.util.SessionManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class OtpActivity extends AppCompatActivity {
    EditText ed_otp;
    String Ed_otp, Email, Contact, Name,Add;
    Button btn_verify;
    SessionManager manager;
    String id = "";
    String name = "";
    String email1 = "";
    String contact = "";
    String address = "";
    String City = "";
    String State = "";
    String Cust_Lat;
    String Cust_Long;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        // Call the function callInstamojo to start payment here

        manager = new SessionManager(this);

        ed_otp = (EditText) findViewById(R.id.ed_otp);
        btn_verify = (Button) findViewById(R.id.btn_verify);

        Ed_otp = getIntent().getStringExtra("otp").toString();
        ed_otp.setText(Ed_otp);

        Email = getIntent().getStringExtra("email").toString();
       // Password = getIntent().getStringExtra("password").toString();
        Contact = getIntent().getStringExtra("contact").toString();
        Name = getIntent().getStringExtra("name").toString();
        Add = getIntent().getStringExtra("add");
        City = getIntent().getStringExtra("city");
        State = getIntent().getStringExtra("state");
        Cust_Lat = getIntent().getStringExtra("Cust_Lat");
        Cust_Long = getIntent().getStringExtra("Cust_Long");

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ed_otp = ed_otp.getText().toString();

                new CompleteReg().execute();

            }
        });
    }

    private class CompleteReg extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(OtpActivity.this);
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/CustOtpMatch");
           //     URL url = new URL("https://www.paramgoa.com/cpepsi/api/CustOtpMatch");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", Email);
                postDataParams.put("contact", Contact);
                postDataParams.put("name", Name);
                postDataParams.put("otp", Ed_otp);
                postDataParams.put("address", Add);
                postDataParams.put("district", City);
                postDataParams.put("state", State);
                postDataParams.put("latitude", Cust_Lat);
                postDataParams.put("longitude", Cust_Long);


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

            if (result != null) {
                dialog.dismiss();

                Log.e("PostRegistration", result.toString());

                try {
                    // Toast.makeText(OtpActivity.this, "reg+"+result, Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(result);
                    String res = jsonObject.getString("responce");


                    if (res.equals("true")) {
                        JSONObject object = jsonObject.getJSONObject("data");

                        id = object.getString("id");
                        name = object.getString("name");
                        email1 = object.getString("email");
                        contact = object.getString("contact");
                        // String password = object.getString("password");
                        address = object.getString("address");
                        String status = object.getString("status");
                        String payment_status = object.getString("payment_status");
                        String payment_amount = object.getString("payment_amount");
                        String image = object.getString("image");
                        String state= object.getString("state");
                        String district = object.getString("district");




                        if (status.equals("1")){

                            final AlertDialog.Builder dialog = new AlertDialog.Builder(OtpActivity.this).setTitle("CPEPSI")
                                    .setMessage("You are using 30 days free trial 'Daily Life Service', After free trial you can use Paid Service.");

                            dialog.setCancelable(false);
                            dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    manager.malegaonLogin();
                                    AppPreference.setId(OtpActivity.this, id);
                                    AppPreference.setName(OtpActivity.this, name);
                                    AppPreference.setEmail(OtpActivity.this, email1);
                                    AppPreference.setContact(OtpActivity.this, contact);
                                    AppPreference.setAddress(OtpActivity.this, address);



                                    if (!AppPreference.getAfterID(getApplicationContext()).equals("null")) {
                                        Intent to_completion = new Intent(OtpActivity.this, After_service.class);
                                        to_completion.putExtra("after_id1", AppPreference.getAfterID(getApplicationContext()));
                                        startActivity(to_completion);
                                        finish();
                                    } else {
                                        Intent to_completion = new Intent(OtpActivity.this, Main_Provider.class);
                                        startActivity(to_completion);
                                        finish();
                                    }
                                }

                            });
                            final AlertDialog alert = dialog.create();
                            alert.show();
                        }
                        Toast.makeText(OtpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                    } else {
                       String error = jsonObject.getString("error");
                        Toast.makeText(OtpActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                        Toast.makeText(OtpActivity.this, "Could not register the user", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            super.onPostExecute(result);
        }

    }
}
