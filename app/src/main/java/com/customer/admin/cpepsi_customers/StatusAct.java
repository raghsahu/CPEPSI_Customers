package com.customer.admin.cpepsi_customers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Java_files.NotificationModel;

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

public class StatusAct extends AppCompatActivity {
    EditText apvName, apvEmail, apvMobile, apvDescription,apvAddress,cust_rating;
    Button accept, decline,feedback;
    String Name, Email, Mobile, Description, Id;
    String ReqStatus;
    String Pr_Id,ProvId;
     String Adds;
    NotificationModel notificationModel;
     String Cust_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        apvName = (EditText) findViewById(R.id.apvName);
        apvEmail = (EditText) findViewById(R.id.apvEmail);
        apvMobile = (EditText) findViewById(R.id.apvMobile);
        apvDescription = (EditText) findViewById(R.id.apvDescription);
        apvAddress = (EditText) findViewById(R.id.apvAddress);
        cust_rating = (EditText) findViewById(R.id.cust_rating);
        //accept = (Button) findViewById(R.id.accept);
        decline = (Button) findViewById(R.id.decline);
        feedback = (Button) findViewById(R.id.feedback);


        if (getIntent() != null) {
            notificationModel = (NotificationModel) getIntent().getSerializableExtra("NotificationModel");
           /* Name = notificationModel.getPrId();
            Email = notificationModel.getCustomerId();
            Mobile = notificationModel.getMobile();*/
            Description = notificationModel.getDiscription();
            Id = notificationModel.getCustomerId();
            Pr_Id = notificationModel.getPrId();
            ProvId = notificationModel.getProviderId();
            Adds  = notificationModel.getDesignation();
            Name  = notificationModel.getName();
            Email  = notificationModel.getEmailid();
            Mobile  = notificationModel.getNumber();
            Cust_rating  = notificationModel.getFeedback_to_cust();

            apvDescription.setText(Description);
            cust_rating.setText(Cust_rating);


           // if (notificationModel.getProstatus().equals("3")){
              // decline.setEnabled(false);
               // feedback.setEnabled(false);
                apvName.setText(Name);
                apvEmail.setText(Email);
                apvMobile.setText(Mobile);
                apvAddress.setText(Adds);
               // Toast.makeText(this, "This service is already completed", Toast.LENGTH_SHORT).show();
           // }
            if (notificationModel.getProstatus().equals("2")){
              //  decline.setEnabled(false);
               // feedback.setEnabled(false);
                Toast.makeText(this, "This service is Decline", Toast.LENGTH_SHORT).show();
            }
            if (notificationModel.getProstatus().equals("1")){
                apvName.setText(Name);
                apvEmail.setText(Email);
                apvMobile.setText(Mobile);
                apvAddress.setText(Adds);
                Toast.makeText(this, "This service is Accept", Toast.LENGTH_SHORT).show();
            }


        }


//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (Connectivity.isNetworkAvailable(StatusAct.this)) {
//                    new PostAccept().execute();
//                } else {
//                    Toast.makeText(StatusAct.this, "No Internet", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationModel.getProstatus().equals("3")|| notificationModel.getProstatus().equals("2") || notificationModel.getProstatus().equals("0") ){
                    if (notificationModel.getProstatus().equals("3")){
                        Toast.makeText(StatusAct.this, "This Service is Already Completed", Toast.LENGTH_LONG).show();
                    }
                    if (notificationModel.getProstatus().equals("2")){
                        Toast.makeText(StatusAct.this, "This Service is Decline", Toast.LENGTH_LONG).show();
                    }
                    if (notificationModel.getProstatus().equals("0")){
                        Toast.makeText(StatusAct.this, "This Service is Now Pending, Please wait for Accept", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    if (Connectivity.isNetworkAvailable(StatusAct.this)) {
                        Intent intent = new Intent(StatusAct.this, FeedbackForm.class);
                        intent.putExtra("PrID",Pr_Id);
                        intent.putExtra("ProvId",ProvId);
                        intent.putExtra("Description",Description);
                        startActivity(intent);
                    } else {
                        Toast.makeText(StatusAct.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (notificationModel.getProstatus().equals("2") || notificationModel.getProstatus().equals("3")){
                    if (notificationModel.getProstatus().equals("2")){
                        Toast.makeText(StatusAct.this, "This Service is Already Decline", Toast.LENGTH_LONG).show();
                    }
                    if (notificationModel.getProstatus().equals("3")){
                        Toast.makeText(StatusAct.this, "This Service is Already Completed", Toast.LENGTH_LONG).show();
                    }
                }

                else {
                    if (Connectivity.isNetworkAvailable(StatusAct.this)) {
                        new PostDecline().execute();
                    } else {
                        Toast.makeText(StatusAct.this, "No Internet", Toast.LENGTH_SHORT).show();
                    }
                }



            }
        });
    }

    public class PostAccept extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(StatusAct.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Cust_approve_decline");
              //  URL url = new URL("https://www.paramgoa.com/cpepsi/api/Cust_approve_decline");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("customer_id", Id);
                postDataParams.put("pr_id", Pr_Id);
                postDataParams.put("status", "1");
                postDataParams.put("provider_id", ProvId);

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
                    String data = jsonObject.getString("data");
                    String responce = jsonObject.getString("responce");

                    if (responce.equals("true")) {
                      /*  apvName.setText("");
                        apvEmail.setText("");
                        apvMobile.setText("");*/
                        apvDescription.setText("");
                        Intent intent = new Intent(StatusAct.this,Main_Provider.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(StatusAct.this, "Some Problem", Toast.LENGTH_SHORT).show();
                    }


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

    //------------------------------------------------------------

    public class PostDecline extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(StatusAct.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Cust_approve_decline");
            //    URL url = new URL("https://www.paramgoa.com/cpepsi/api/Cust_approve_decline");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("customer_id", Id);
                postDataParams.put("pr_id", Pr_Id);
                postDataParams.put("status", "2");
                postDataParams.put("provider_id", ProvId);

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
               // Toast.makeText(StatusAct.this, "res "+result, Toast.LENGTH_SHORT).show();

                JSONObject jsonObject = null;
                String s = result.toString();
                try {
                    jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    String responce = jsonObject.getString("responce");

                    if (responce.equals("true")) {
//                        apvName.setText("");
//                        apvEmail.setText("");
//                        apvMobile.setText("");
//                        apvDescription.setText("");
                        Intent intent = new Intent(StatusAct.this,Main_Provider.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(StatusAct.this, "Some Problem", Toast.LENGTH_SHORT).show();
                    }


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
    //------------------------------------------------------------
}
