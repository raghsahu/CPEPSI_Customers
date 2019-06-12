package com.customer.admin.cpepsi_customers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Fcm.SmsListener;
import com.customer.admin.cpepsi_customers.Fcm.SmsReceiver;
import com.customer.admin.cpepsi_customers.util.AppPreference;
import com.customer.admin.cpepsi_customers.util.SessionManager;

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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Login_Constomer extends AppCompatActivity {
    TextView reg_cp;
    Button log_cp;
    EditText mobile_customer, otp_customer;
    String id = "";
    String name = "";
    String email1 = "";
    String contact = "";
    SessionManager manager;
    String address = "";

    TextInputLayout text_et_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__constomer);

        reg_cp = (TextView) findViewById(R.id.reg_cp);
        mobile_customer = findViewById(R.id.mobile_customer);
        otp_customer = findViewById(R.id.otp_customer);
        text_et_otp = findViewById(R.id.et_otp);
        mobile_customer.requestFocus();
        //*********************************auto read otp**************************

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Auto_read_sms",messageText);

              String smsString = messageText.replaceAll("\\D+","");
                Log.e("replace_sms",smsString);

                Toast.makeText(Login_Constomer.this,""+smsString,Toast.LENGTH_LONG).show();
                otp_customer.setText(smsString);
            }
        });
//**********************************************************************************
        manager = new SessionManager(this);
        if (manager.isLoggedIn()) {

            Intent intent = new Intent(Login_Constomer.this, Main_Provider.class);
            startActivity(intent);
            finish();
        }

        reg_cp.setOnClickListener(new View.OnClickListener() {
            @Override
             public void onClick(View v) {

                Intent intent = new Intent(Login_Constomer.this, Register_Customer.class);
                startActivity(intent);
            }
        }
        );

        log_cp = findViewById(R.id.login_cp);


      //************************************************************

        log_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (log_cp.getText().toString().equals("Login")){

                    if (mobile_customer.getText().toString().isEmpty()) {
                        mobile_customer.setError("Mobile can not be empty");
                    }if (otp_customer.getText().toString().isEmpty()) {
                        otp_customer.setError("OTP can not be empty");
                    }
                    else if (!mobile_customer.getText().toString().isEmpty() && !otp_customer.getText().toString().isEmpty()) {

                        new Check_login_cus(v, mobile_customer.getText().toString(),otp_customer.getText().toString()).execute();
                    }

                }

                if (log_cp.getText().toString().equals("Get OTP")){

                    if (mobile_customer.getText().toString().isEmpty()) {
                        mobile_customer.setError("Mobile can not be empty");
                    }
                    else if (!mobile_customer.getText().toString().isEmpty()) {

                        new Login_get_otp(v, mobile_customer.getText().toString()).execute();
                    }

                }

            }
        });

//        forgetPass.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Login_Constomer.this, ForgetPassword.class);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    private class Check_login_cus extends AsyncTask<String, Void, String> {

        String mobile;
        String otp;
        View snac_v;
        ProgressDialog dialog;

        public Check_login_cus(View v, String mobile, String otp) {
            this.snac_v = v;
            this.mobile = mobile;
            this.otp = otp;

        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Login_Constomer.this);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/login_customer");
           //     URL url = new URL("https://www.paramgoa.com/cpepsi/api/login_customer");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("contact", mobile);
                postDataParams.put("otp", otp);

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

            Log.e("SendJsonDataToServer>>>", result.toString());
            try {
                JSONObject jsonObject = new JSONObject(result);
                String response = jsonObject.getString("responce");
                if (response.equalsIgnoreCase("true")) {
                    manager.malegaonLogin();
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    id = jsonObject1.getString("id");
                    name = jsonObject1.getString("name");
                    email1 = jsonObject1.getString("email");
                    contact = jsonObject1.getString("contact");
                    address = jsonObject1.getString("address");
                    String status = jsonObject1.getString("status");
                    String payment_status = jsonObject1.getString("payment_status");
                    String payment_amount = jsonObject1.getString("payment_amount");
                    String image = jsonObject1.getString("image");
                    String district = jsonObject1.getString("district");
                    String state = jsonObject1.getString("state");

                   Toast.makeText(Login_Constomer.this, "id is" + id, Toast.LENGTH_LONG).show();
                    AppPreference.setId(Login_Constomer.this, id);
                    AppPreference.setName(Login_Constomer.this, name);
                    AppPreference.setEmail(Login_Constomer.this, email1);
                    AppPreference.setContact(Login_Constomer.this, contact);
                    AppPreference.setAddress(Login_Constomer.this, address);

                    if (!AppPreference.getAfterID(getApplicationContext()).equals("null")) {
                        Intent go_to_home = new Intent(Login_Constomer.this, After_service.class);
                        go_to_home.putExtra("after_id", AppPreference.getAfterID(getApplicationContext()));
                        startActivity(go_to_home);
                        finish();
                    } else {
                        Intent go_to_home = new Intent(Login_Constomer.this, Main_Provider.class);
                        startActivity(go_to_home);
                        finish();
                    }
                } else {
                    Snackbar.make(snac_v, "" + response, Toast.LENGTH_LONG).show();
                    Toast.makeText(Login_Constomer.this, "Invalid user", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
//******************************otp***********************************************
    private class Login_get_otp extends AsyncTask<String, Void, String> {

        View view1;
        ProgressDialog dialog;
        String mobile;


        public Login_get_otp(View v, String mobile) {
            this.mobile=mobile;
            this.view1=v;

        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Login_Constomer.this);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/login_otp");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("contact", mobile);

                Log.e("postDataParams", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(15000 /* milliseconds*/);
                conn.setConnectTimeout(15000  /*milliseconds*/);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
               // conn.setChunkedStreamingMode(0);

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

        Log.e("SendJsonDataToServer>>>", result.toString());
        try {
            JSONObject jsonObject = new JSONObject(result);
            String res = jsonObject.getString("responce");


            if (res.equals("false")){
                String error = jsonObject.getString("error");
                Toast.makeText(Login_Constomer.this, ""+error, Toast.LENGTH_SHORT).show();
            }
            else {
                String msg = jsonObject.getString("massage");
                if (msg.equals("OTP Sent Successfully")) {
                    Toast.makeText(Login_Constomer.this, ""+msg, Toast.LENGTH_SHORT).show();
                   // otp_customer.setText(res);
                    text_et_otp.setVisibility(View.VISIBLE);
                    log_cp.setText("Login");

                } else {

                    Toast.makeText(Login_Constomer.this, "Otp not sent", Toast.LENGTH_SHORT).show();
                }

            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(result);
    }
    }



    @Override
    protected void onDestroy() {
        SmsReceiver.unbindListener();
        super.onDestroy();
    }



}
