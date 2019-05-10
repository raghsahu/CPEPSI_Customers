package com.customer.admin.cpepsi_customers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    Button log_cp, forgetPass;
    EditText em_customer, pass_customer;
    private EditText pass_cus, emai_cus;
    String id = "";
    String name = "";
    String email1 = "";
    String contact = "";
    SessionManager manager;
    String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__constomer);

        reg_cp = (TextView) findViewById(R.id.reg_cp);
        emai_cus = findViewById(R.id.em_customer);
        pass_cus = findViewById(R.id.pass_customer);
       // forgetPass = findViewById(R.id.forgetPass);
        emai_cus.requestFocus();

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
        log_cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent go_to_home = new Intent(Login_Constomer.this, Main_Provider.class);
//                startActivity(go_to_home);
//                finish();
                if (pass_cus.getText().toString().isEmpty()) {
                    pass_cus.setError("Password can not be empty");
                }
                if (emai_cus.getText().toString().isEmpty()) {
                    emai_cus.setError("Email can not be empty");
                }
                if (emai_cus.getText().toString().isEmpty() && pass_cus.getText().toString().isEmpty()) {
                    pass_cus.setError("Password can not be empty");
                    emai_cus.setError("Email can not be empty");
                } else if (!emai_cus.getText().toString().isEmpty() && !pass_cus.getText().toString().isEmpty()) {

                    new Check_login_cus(v, emai_cus.getText().toString(), pass_cus.getText().toString()).execute();

                }
               /* Intent intent2 = new Intent(Login_Constomer.this, Main_Provider.class);
                startActivity(intent2);*/
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

        String email, password;
        View snac_v;
        ProgressDialog dialog;

        public Check_login_cus(View v, String email, String password) {
            this.snac_v = v;
            this.email = email;
            this.password = password;
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
                postDataParams.put("emailid", email);
                postDataParams.put("password", password);

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
                    String password1 = jsonObject1.getString("password");
                    address = jsonObject1.getString("address");
                    String status = jsonObject1.getString("status");
                    String payment_status = jsonObject1.getString("payment_status");
                    String payment_amount = jsonObject1.getString("payment_amount");
                    String image = jsonObject1.getString("image");

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

}
