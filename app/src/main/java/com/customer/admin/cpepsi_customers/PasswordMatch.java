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

import com.customer.admin.cpepsi_customers.Connectivitycheck.Connectivity;

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

public class PasswordMatch extends AppCompatActivity {
    EditText em_otp,em_mobile,em_newpassword;
    String Em_otp,Em_mobile,Em_newpassword;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_match);

        em_otp = (EditText)findViewById(R.id.em_otp);
        em_mobile = (EditText)findViewById(R.id.em_mobile);
        em_newpassword = (EditText)findViewById(R.id.em_newpassword);
        btn_submit = (Button)findViewById(R.id.btn_submit);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Em_otp = em_otp.getText().toString();
                Em_mobile = em_mobile.getText().toString();
                Em_newpassword = em_newpassword.getText().toString();

                if (Connectivity.isNetworkAvailable(PasswordMatch.this)){

                    if (!Em_otp.equals("")) {
                        if (!Em_mobile.equals("")) {
                            if (!Em_newpassword.equals("")) {
                                new PostSave().execute();
                            } else {
                                em_newpassword.setError("Please Enter Password");
                                em_newpassword.requestFocus();
                            }
                        } else {
                            em_mobile.setError("Please Enter Mobile no");
                            em_mobile.requestFocus();
                        }
                    } else {
                        em_otp.setError("Please Enter Otp");
                        em_otp.requestFocus();
                    }

                }else {
                    Toast.makeText(PasswordMatch.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //--------------------------------------------------

    public class PostSave extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(PasswordMatch.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://jntrcpl.com/CPEPSI/api/ForgetOtpMatch");
           //     URL url = new URL("https://www.paramgoa.com/cpepsi/api/ForgetOtpMatch");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("otp", Em_otp);
                postDataParams.put("type", "2");
                postDataParams.put("mobile_no", Em_mobile);
                postDataParams.put("newpassword", Em_newpassword);

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
                        Intent intent = new Intent(PasswordMatch.this,Login_Constomer.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PasswordMatch.this, "OTP not Match", Toast.LENGTH_SHORT).show();
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
}
