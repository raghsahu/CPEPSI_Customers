package com.customer.admin.cpepsi_customers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Java_files.StateModel;
import com.customer.admin.cpepsi_customers.util.HttpHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

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
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class Register_Customer extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final String TAG = "Register_Customer";

    CheckBox terms_view;
    Button sbmt;
    TextView tx_terms;
    EditText cus_name, cus_email, cus_con,cus_add;
    String Cus_add;
    public static String res;

    Spinner spin_state;
    ArrayList<String> ChooseState=new ArrayList<String>();
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<StateModel> stateList=new ArrayList<StateModel>();

    Spinner spin_distt;
    ArrayList<String>ChooseDistt=new ArrayList<String>();
    private ArrayAdapter<String> disttAdapter;
    private ArrayList<StateModel> disttList=new ArrayList<StateModel>();

    public String Spin_state, Spin_distt;

    public HashMap<Integer, StateModel> StateHashMap = new HashMap<Integer, StateModel>();
     GoogleApiClient mGoogleApiClient;
     LocationManager mLocationManager;
    private LocationManager locationManager;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 5 * 1000;
    private long FASTEST_INTERVAL = 5000;
     String mLatitudeString;
     String mLongitudeString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register__customer);

        terms_view = (CheckBox) findViewById(R.id.terms_view);
        cus_name = findViewById(R.id.cus_name);
        cus_email = findViewById(R.id.cus_email);
//        cus_num = findViewById(R.id.cus_num);
//        cus_pass = findViewById(R.id.cus_pass);
        cus_con = findViewById(R.id.cus_con);
        cus_add = findViewById(R.id.cus_add);
        sbmt = (Button) findViewById(R.id.sbmt);
        tx_terms = (TextView) findViewById(R.id.tx_terms);
        spin_state = (Spinner)findViewById(R.id.state_type);
        spin_distt = (Spinner)findViewById(R.id.sub_type_distt);

        new stateExecuteTask().execute();

        spin_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{
                    if(disttList.size() !=0)
                    {
                        ChooseDistt.clear();
                        disttAdapter.notifyDataSetChanged();
                        spin_distt.setAdapter(null);

                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                for (int i = 0; i < StateHashMap.size(); i++)
                {

                    if (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position)))
                    {

                        new DisttExecuteTask(StateHashMap.get(i).getState_id()).execute();
                       // Toast.makeText(Register_Customer.this, "st "+StateHashMap.get(i).getState_id(), Toast.LENGTH_SHORT).show();
                    }
                    // else (StateHashMap.get(i).getState_name().equals(spin_state.getItemAtPosition(position))
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //********************************************

        sbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_The_Validation();

            }
        });
        terms_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                warn_the_user();

            }
        });

        tx_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent= new Intent(Register_Customer.this,PDF_Viewer.class);
               startActivity(intent);

            }
        });
//***************************************************************************
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        checkLocation(); //ch


    }

    private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }
    //*******************************************************
    private void Check_The_Validation() {

        Cus_add = cus_add.getText().toString();

        Spin_state=spin_state.getSelectedItem().toString();
        Spin_distt=spin_distt.getSelectedItem().toString();

        if (cus_name.getText().toString().isEmpty()) {
            cus_name.setError("Name can not be empty");
            return;
        }
        if (cus_add.getText().toString().isEmpty()) {
            cus_add.setError("Address can not be empty");
            return;
        }

        if (cus_email.getText().toString().isEmpty()) {
            cus_email.setError("Email can not be empty");
            return;
        }
        if (!cus_name.getText().toString().isEmpty()  && !cus_email.getText().toString().isEmpty())
        {
            if (cus_con.getText().toString().isEmpty() ) {
                cus_con.requestFocus();
               // cus_con.setError("Confirm Password can not be empty");
                cus_con.setError("Specify Mobile number");
//                if(cus_num.getText().toString().length() ==10){
//
//                }

                return;
            } else {

                if (terms_view.isChecked()) {
                    new Check_Log_Infor_Cus(cus_name.getText().toString(), cus_email.getText().toString(),
                            cus_con.getText().toString()).execute();
                } else {
                    Toast.makeText(Register_Customer.this, "Please tick on Agree terms and condition of ours or fill all fields", Toast.LENGTH_SHORT).show();
                }


            }

        } else {

            Toast.makeText(Register_Customer.this, "Please tick on Agree terms and condition of ours or fill all fields", Toast.LENGTH_SHORT).show();
            tx_terms.setTextColor(Color.RED);
        }
    }


    private void warn_the_user() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("We are going to use some features from your phone please grant CPEPSI");
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        check_requested_permission();
                        Toast.makeText(Register_Customer.this, "Please clicked yes button", Toast.LENGTH_LONG).show();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void check_requested_permission() {
        ActivityCompat.requestPermissions(Register_Customer.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission_group.CONTACTS, Manifest.permission.CAMERA},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Thanks", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Register_Customer.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
//*************************************************************************
@Override
public void onConnected(Bundle bundle) {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        return;
    }

    startLocationUpdates();

    mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    if(mLocation == null){
        startLocationUpdates();
    }
    if (mLocation != null) {

        // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
        //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
    } else {
        Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
    }
}

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        mLatitudeString=(String.valueOf(location.getLatitude()));
        mLongitudeString=(String.valueOf(location.getLongitude() ));
        // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //******************address******************************

//        if (location != null) {
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//            LocationAddress locationAddress = new LocationAddress();
//            locationAddress.getAddressFromLocation(latitude, longitude,
//                    getApplicationContext(), new GeocoderHandler());
//        }

    }
//*******************************************************************************************
    private class Check_Log_Infor_Cus extends AsyncTask<String, Void, String> {
        ProgressDialog Log_in_Progress;
        String email, mobile_no, name;


        public Check_Log_Infor_Cus(String name, String email, String mobile_no) {
            this.email = email;
            this.mobile_no = mobile_no;
            this.name = name;


        }

        @Override
        protected void onPreExecute() {
            Log_in_Progress = new ProgressDialog(Register_Customer.this);
            Log_in_Progress.show();
            Log_in_Progress.setCancelable(false);
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            try {

                URL url = new URL("http://jntrcpl.com/CPEPSI/api/Cust_Ragistration");
           //     URL url = new URL("https://www.paramgoa.com/cpepsi/api/Cust_Ragistration");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", email);
               // postDataParams.put("password", password);
                postDataParams.put("contact", mobile_no);
                postDataParams.put("name", name);
                postDataParams.put("address", Cus_add);
                postDataParams.put("district", Spin_distt);
                postDataParams.put("state", Spin_state);

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

            Log_in_Progress.dismiss();

            Log.e("SendJsonDataToServer>>>", result.toString());
            try {

                JSONObject jsonObject = new JSONObject(result);
                res = jsonObject.getString("responce");

                if (res.equals("false")){

                    String error = jsonObject.getString("massage");
                    Toast.makeText(Register_Customer.this, ""+error, Toast.LENGTH_SHORT).show();
                }else {
                    String msg = jsonObject.getString("massage");
                    if (msg.equals("OTP Sent Successfully")) {
                        Intent to_completion = new Intent(Register_Customer.this, OtpActivity.class);
                        // Intent to_completion = new Intent(Register_Customer.this, OTP_Activity.class);
                        to_completion.putExtra("otp",res);
                        to_completion.putExtra("email", email);
                        //  to_completion.putExtra("password", password);
                        to_completion.putExtra("contact", mobile_no);
                        to_completion.putExtra("name", name);
                        to_completion.putExtra("add",Cus_add);
                        to_completion.putExtra("city",Spin_distt);
                        to_completion.putExtra("state",Spin_state);
                        to_completion.putExtra("Cust_Lat", mLatitudeString);
                        to_completion.putExtra("Cust_Long", mLongitudeString);

                        startActivity(to_completion);
                        Toast.makeText(Register_Customer.this, ""+msg, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {

//                    Snackbar.make(snac_v,""+response, Toast.LENGTH_LONG).show();
                        Toast.makeText(Register_Customer.this, "Otp not sent", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);


            super.onPostExecute(result);
        }
    }

    //**********************************************************************************************
    public class stateExecuteTask extends AsyncTask<String,Integer,String> {

        String output = "";

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String sever_url = "http://jntrcpl.com/CPEPSI/Api/get_state";
            // String sever_url = "http://paramgoa.com/cpepsi/Api/get_state";
            //+ AppPreference.getUserid(AddStudentActivity.this);


            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {

                    //  Toast.makeText(Service_provider_reg.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object=new JSONObject(output);
                    String res=object.getString("responce");

                    if (res.equals("true")) {
//                        ChooseState.add("Select State");

                        JSONArray jsonArray = object.getJSONArray("state");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String state_id = jsonObject1.getString("state_id");
                            String state_name = jsonObject1.getString("state_name");

                            stateList.add(new StateModel(state_id, state_name));

                            StateHashMap.put(i, new StateModel(state_id,state_name));
                            ChooseState.add(state_name);

                            // if (StateHashMap.)

                        }

                        stateAdapter = new ArrayAdapter<String>(Register_Customer.this, android.R.layout.simple_spinner_dropdown_item, ChooseState);
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_state.setAdapter(stateAdapter);


                    }else {
                        Toast.makeText(Register_Customer.this, "No state found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class DisttExecuteTask extends AsyncTask<String,Integer,String> {

        String output = "";

        String strStateId;

        public DisttExecuteTask(String strSid) {
            this.strStateId=strSid;
        }

        @Override
        protected String doInBackground(String... params) {
            String sever_url = "http://jntrcpl.com/CPEPSI/Api/get_district?state_id="+strStateId;
            //  String sever_url = "http://paramgoa.com/cpepsi/Api/get_district?state_id="+strStateId;

            output = HttpHandler.makeServiceCall(sever_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
            } else {
                try {

                    //  Toast.makeText(Service_provider_reg.this, "result is" + output, Toast.LENGTH_SHORT).show();
                    JSONObject object=new JSONObject(output);
                    String res=object.getString("responce");

                    if (res.equals("true")) {

                        // ChooseDistt.add("Select District");
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String state_id = jsonObject1.getString("state_id");
                            String state_name = jsonObject1.getString("state_name");


                            disttList.add(new StateModel(state_id, state_name));
                            // StateHashMap.put(state_id, new StateModel(state_id,state_name));
                            ChooseDistt.add(state_name);

                        }

                        disttAdapter = new ArrayAdapter<String>(Register_Customer.this, android.R.layout.simple_spinner_dropdown_item, ChooseDistt);
                        disttAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spin_distt.setAdapter(disttAdapter);


                        // reloadAllData();

                    }else {
                        Toast.makeText(Register_Customer.this, "no state found", Toast.LENGTH_SHORT).show();
                    }
                    super.onPostExecute(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
