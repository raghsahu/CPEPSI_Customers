package com.customer.admin.cpepsi_customers;

import android.app.Activity;
import instamojo.library.Config;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Java_files.ApiModel;
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

import instamojo.library.InstamojoPay;
import instamojo.library.InstapayListener;

public class FirstTime_Payment_Activity extends AppCompatActivity {
    EditText feedName, feedEmail, feedContact, feedRemark, feedPayment;
    String FeedName, FeedEmail, FeedContact, Feedremark,FeedPaymentAmount;
    Button first_payment,Cash_Payment;
    String Id;
    String strid;

    String amount="10";
    public ApiModel ApiModel;
    public  Intent intent;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firsttime_payment);

        feedName = (EditText) findViewById(R.id.feedName);
        feedEmail = (EditText) findViewById(R.id.feedEmail);
        feedContact = (EditText) findViewById(R.id.feedContact);
        feedRemark = (EditText) findViewById(R.id.feedRemark);
        feedPayment = (EditText) findViewById(R.id.feedPayment);
        first_payment = (Button) findViewById(R.id.feedSubmit);
        //Cash_Payment = (Button) findViewById(R.id.cash_Submit);

        Id = AppPreference.getId(this);
        FeedName = AppPreference.getName(this);
        FeedEmail = AppPreference.getEmail(this);
        FeedContact = AppPreference.getContact(this);

        feedName.setText(FeedName);
        feedEmail.setText(FeedEmail);
        feedContact.setText(FeedContact);
        strid = getIntent().getStringExtra("strid");
        ApiModel = (ApiModel) getIntent().getSerializableExtra("ApiModel");
        first_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FeedName = feedName.getText().toString();
                FeedEmail = feedEmail.getText().toString();
                FeedContact = feedContact.getText().toString();
                Feedremark = feedRemark.getText().toString();
                FeedPaymentAmount = feedPayment.getText().toString();


                if (Connectivity.isNetworkAvailable(FirstTime_Payment_Activity.this)){

                    callInstamojoPay(FeedEmail,FeedContact,FeedPaymentAmount,Feedremark,FeedName);

                }else {
                    Toast.makeText(FirstTime_Payment_Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
//*******************************************************

//        Cash_Payment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               // new FirstPaymentSucccess().execute();
//
//            }
//        });

    }

    @Override
    protected void onDestroy() {
        if(dialog.isShowing() && dialog !=null)
        {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    private void callInstamojoPay(String email, String phone, String FeedPaymentAmount, String purpose, String buyername) {
        final Activity activity = this;
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", FeedPaymentAmount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(activity, pay, listener);
    }

    InstapayListener listener;


    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                new FirstPaymentSucccess().execute();
                intent = new Intent(FirstTime_Payment_Activity.this, After_service.class);
                intent.putExtra("ApiModel" ,ApiModel);
                new FirstPaymentSucccess().execute();
                startActivity(intent);
                finish();
//                 intent = new Intent(FirstTime_Payment_Activity.this, After_service.class);
//                intent.putExtra("strid" ,strid );
//                startActivity(intent);
//               finish();
            }

            @Override
            public void onFailure(int code, String reason) {
                Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                        .show();
            }
        };
    }

    public class FirstPaymentSucccess extends AsyncTask<String, Void, String> {



        protected void onPreExecute() {
            dialog = new ProgressDialog(FirstTime_Payment_Activity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/Api/first_payment");

                JSONObject postDataParams = new JSONObject();

                Id= AppPreference.getId(FirstTime_Payment_Activity.this);

                postDataParams.put("user_id",Id);
                postDataParams.put("payment_amount",FeedPaymentAmount );
                Log.e("payment_amount", FeedPaymentAmount);

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

                    jsonObject = new JSONObject(result);
                    String responce = jsonObject.getString("responce");

                    if (responce.equals("true")) {
                        Toast.makeText(FirstTime_Payment_Activity.this, "One time Service Amount Success, You can take our service now!",
                                Toast.LENGTH_LONG).show();

//                        Intent intent = new Intent(FirstTime_Payment_Activity.this, After_service.class);
//                        intent.putExtra("strid" ,strid );
//                        startActivity(intent);
//                        finish();

                        // finish();
                    }else {
                        Toast.makeText(FirstTime_Payment_Activity.this, "Some Problem Please Try Again", Toast.LENGTH_SHORT).show();
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
