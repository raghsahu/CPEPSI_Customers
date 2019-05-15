package com.customer.admin.cpepsi_customers;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.SearchManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.customer.admin.cpepsi_customers.Adapters.CustomInfoWindowAdapter;
import com.customer.admin.cpepsi_customers.Adapters.SearchAdapter;
import com.customer.admin.cpepsi_customers.Java_files.PlaceInfo;
import com.customer.admin.cpepsi_customers.Java_files.Provider_info;
import com.customer.admin.cpepsi_customers.Java_files.SearchModel;
import com.customer.admin.cpepsi_customers.LocationUtil.GpsTracker;
import com.customer.admin.cpepsi_customers.R;
import com.customer.admin.cpepsi_customers.util.AppPreference;
import com.customer.admin.cpepsi_customers.util.HttpHandler;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

public class GET_Service_providers extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    LatLng p1 = null;
    LatLng p2 = new LatLng(23,77);
    private GoogleMap mMap;
    Dialog Provider_show_dialog;
    EditText get_loc_map;
    int[] Stored_Ids;
    Marker mCurrLocationMarker;
    public Button give_loc_back;
    GpsTracker gpsTracker;
    String address1 = "";
    double longitude;
    Geocoder geocoder;
    HashMap<Integer, Provider_info> Provider_infoHashMap = new HashMap<>();
    Marker current_marker;
    Marker[] providerMarkers;

    //loction_str;
    CustomInfoWindowAdapter customInfoWindowAdapter;
    Address address;
    double latitude;
    String[] Destinations, City, ServiceSubCategory, Service;
    List<Address> event_address;
    String locality = "Indore", place = "Krishnodaya Nagar";
    String state_of_provider, place_of_provider;
    int selected_ser_name_from;
    Intent to_add_event;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    String CustProblem;
    int user_id;
    String ProviderId;
    String SubServiceNmae;
    String SubSer_ID;
    // StreetViewPanorama streetViewPanorama;
    SearchView searchView1;
    private SearchAdapter searchAdapter;
    private ArrayList<SearchModel> searchModelList;
    ImageView Search_place;
    RecyclerView serachResult;
    String Place;
    String service;
    String server_url;
    String RatingRes;
    String adds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__service_providers);

        searchView1 = (SearchView) findViewById(R.id.searchView1);
        serachResult = (RecyclerView) findViewById(R.id.serachResult);

        searchModelList = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        selected_ser_name_from = getIntent().getIntExtra("service_id", 0);
        if (selected_ser_name_from == 0) {
            selected_ser_name_from = Integer.parseInt(AppPreference.getAfterID(getApplicationContext()));
        }
        CustProblem = getIntent().getStringExtra("Problem");
        SubServiceNmae = getIntent().getStringExtra("ServiceSub");
        SubSer_ID = getIntent().getStringExtra("SubSer_ID");
        Toast.makeText(this, "ssi "+SubSer_ID, Toast.LENGTH_SHORT).show();

        checkthepermisions();

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (Connectivity.isNetworkAvailable(GET_Service_providers.this)) {
                    new GetSearch(newText).execute();
                } else {
                    Toast.makeText(GET_Service_providers.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    private void checkthepermisions() {

        if (ActivityCompat.checkSelfPermission(GET_Service_providers.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(GET_Service_providers.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            lets_get_current_loc();
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private Boolean lets_get_current_loc() {
        //     Toast.makeText(GET_Service_providers.this, "location called", Toast.LENGTH_SHORT).show();
        gpsTracker = new GpsTracker(this);
        if (gpsTracker.canGetLocation()) {

            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            //////Cobvert Lat long to address
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(this, Locale.getDefault());

            if (latitude == 0.0 && longitude == 0.0) {

                //     Toast.makeText(GET_Service_providers.this, "GPS is not able to find your location please enter manually", Toast.LENGTH_SHORT).show();
                return false;
//                Toast.makeText(LandDetailActivity.this, addresslatitude + "\n" + addresslongitude, Toast.LENGTH_SHORT).show();
            } else {

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5\
//                    state_of_provider = address.getAdminArea().toString();
//                    place_of_provider = address.getSubLocality().toString();
//                    to_add_event.putExtra("city_of_pro",state_of_provider);
//                    to_add_event.putExtra("place_of_pro",place_of_provider);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
//                    String address = "";
                    if (addresses.get(0).getMaxAddressLineIndex() == 1 || addresses.get(0).getMaxAddressLineIndex() == 0) {
                        address1 = address1 + " " + addresses.get(0).getAddressLine(0);
                        Log.e("my location", "is" + address1);
                        return true;
                    } else {
                        for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
                            address1 = address1 + " " + addresses.get(0).getAddressLine(i); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            return true;
                        }
                    }

                }
            }
        } else {

            gpsTracker.showSettingsAlert();
            return false;
        }
        return false;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (selected_ser_name_from != 0) {
                    new Get_Places(selected_ser_name_from).execute();
                }
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            //       mMap.setMyLocationEnabled(true);
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //        Toast.makeText(getApplicationContext(), "sdgsdgf"+marker.getSnippet(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(GET_Service_providers.this));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (int k = 0; k < Stored_Ids.length; k++) {
                    //  Toast.makeText(getApplicationContext(), "Stored_Ids is" + Stored_Ids[k], Toast.LENGTH_SHORT).show();
                    if (Provider_infoHashMap.get(Stored_Ids[k]).getContactpersonnames().equals(marker.getTitle())) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(GET_Service_providers.this);
                        LayoutInflater inflater = getLayoutInflater();

                        View dialogLayout = inflater.inflate(R.layout.location_dialog, null);
                        final TextView txtype = dialogLayout.findViewById(R.id.txtype);
                        txtype.setText(Provider_infoHashMap.get(Stored_Ids[k]).getTypeofFirm());

                        final TextView locName = dialogLayout.findViewById(R.id.locName);
                        locName.setText(Provider_infoHashMap.get(Stored_Ids[k]).getContactpersonnames());

                        final TextView locEmail = dialogLayout.findViewById(R.id.locEmail);
                        locEmail.setText(Provider_infoHashMap.get(Stored_Ids[k]).getEmail_id());

                        final TextView locService = dialogLayout.findViewById(R.id.locService);
                        locService.setText(Provider_infoHashMap.get(Stored_Ids[k]).getService());

                        final TextView locSubService = dialogLayout.findViewById(R.id.locSubService);
                        locSubService.setText(SubServiceNmae);
                        // locSubService.setText(Provider_infoHashMap.get(Stored_Ids[k]).getServiceSubCategory());

                        final TextView locState = dialogLayout.findViewById(R.id.locState);
                        locState.setText(Provider_infoHashMap.get(Stored_Ids[k]).getNatureFirm());

                        final TextView locLocation = dialogLayout.findViewById(R.id.locLocation);
                        locLocation.setText(Provider_infoHashMap.get(Stored_Ids[k]).getDesignation());

                        ProviderId = Provider_infoHashMap.get(Stored_Ids[k]).getUser_id();

                        final TextView rating = dialogLayout.findViewById(R.id.rating);
                        RatingRes = Provider_infoHashMap.get(Stored_Ids[k]).getFeedbackservice();
                        if (RatingRes.equals("good")) {
                            rating.setText("5.0");
                        } else if (RatingRes.equals("average")) {
                            rating.setText("3.0");
                        } else if (RatingRes.equals("bad")) {
                            rating.setText("1.0");
                        }
                        //rating.setText(RatingRes);

                        builder.setView(dialogLayout);
                        builder.setPositiveButton("Request Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Connectivity.isNetworkAvailable(GET_Service_providers.this)) {
                                    new SendRequest().execute();
                                } else {
                                    Toast.makeText(GET_Service_providers.this, "No Internet", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.show();
                    } else {
                        Toast.makeText(GET_Service_providers.this, "Current Location", Toast.LENGTH_SHORT).show();
                    }
                }
               // Toast.makeText(getApplicationContext(), "sdgsdgf" + marker.getSnippet(), Toast.LENGTH_SHORT).show();
            }
        });

        if (address1 != null) {
            Log.e("ypur", "locatopn");
            Toast.makeText(GET_Service_providers.this, "your location", Toast.LENGTH_SHORT).show();
            LatLng current = new LatLng(latitude, longitude);
            current_marker = mMap.addMarker(new MarkerOptions().position(current).title("Your Current location"));
//            get_loc_map.setText(address1);
            final CameraPosition cameraPosition = new CameraPosition.Builder().target(current).zoom(13)
                    .bearing(90)
                    .tilt(30)
                    .build();
            mMap.addMarker(new MarkerOptions().position(new LatLng(current.latitude, current.longitude)).title("Your location is here"));
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                                                        @Override
                                                        public boolean onMyLocationButtonClick() {
                                                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                                            return true;
                                                        }
                                                    }
            );
//            mMap.addMarker(new MarkerOptions().position(current).title("my location"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(current));

        } else {
//            LatLng sydney2 = new LatLng(-34, 151);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(GET_Service_providers.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }


    private LatLng put_location_to_the_marker(String destination) {
        geocoder = new Geocoder(this);
        try {
            event_address = geocoder.getFromLocationName(destination, 5);
            if (event_address.isEmpty()) {
                return null;
            } else {
                Address location = event_address.get(0);
                state_of_provider = location.getLocality().toString();
//                place_of_provider = location.getSubLocality().toString();
                Log.e("location getLocality", "" + location.getLocality().toString());
                Log.e("location getSubLocality", "" + location.getSubLocality().toString());
                Log.e("location getAdminArea", "" + location.getAdminArea().toString());
                location.getLatitude();
                location.getLongitude();
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(40));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }
            });
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private class Get_Places extends AsyncTask<String, Void, String> {
        String place_get, full_address_get, city_get;
        int selected_service;
        int sub_service;
        ProgressDialog places_bar;

        public Get_Places(int selected_ser_name_from) {
            this.selected_service = selected_ser_name_from;

        }

        @Override
        protected void onPreExecute() {
            places_bar = new ProgressDialog(GET_Service_providers.this);
            places_bar.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... strings) {
            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/GetproviderByservice");
                // URL url = new URL("https://www.paramgoa.com/cpepsi/api/GetproviderByservice");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("Servicesid", selected_service);
                postDataParams.put("subServicesid", SubSer_ID);

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
            places_bar.dismiss();
            if (result != null) {
                //  dialog.dismiss();
                Log.e("PostRegistration", result.toString());

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    boolean responce = jsonObject.getBoolean("responce");
                    JSONArray providersJsonArray = jsonObject.getJSONArray("data");
                    Destinations = new String[providersJsonArray.length()];
                    Stored_Ids = new int[providersJsonArray.length()];
                    City = new String[providersJsonArray.length()];
                    providerMarkers = new Marker[providersJsonArray.length()];

                    for (int i = 0; i < providersJsonArray.length(); i++) {
                        try {
                            String px = providersJsonArray.getJSONObject(i).getString("ServiceSubCategory");
                            if (!px.isEmpty()) {
                                user_id = providersJsonArray.getJSONObject(i).getInt("user_id");
                                Provider_infoHashMap.put(user_id, new Provider_info
                                        (providersJsonArray.getJSONObject(i).getString("TypeofFirm"),
                                                providersJsonArray.getJSONObject(i).getString("emailid"),
                                                providersJsonArray.getJSONObject(i).getString("City"),
                                                providersJsonArray.getJSONObject(i).getString("name"),
                                                providersJsonArray.getJSONObject(i).getString("Designation"),
                                                providersJsonArray.getJSONObject(i).getString("emailid"),
                                                providersJsonArray.getJSONObject(i).getString("middle"),
                                                providersJsonArray.getJSONObject(i).getString("user_id"),
                                                providersJsonArray.getJSONObject(i).getString("Service"),
                                                providersJsonArray.getJSONObject(i).getString("ServiceSubCategory"),
                                                providersJsonArray.getJSONObject(i).getString("feedbackservice")));

                                Destinations[i] = providersJsonArray.getJSONObject(i).getString("Designation");
                                City[i] = providersJsonArray.getJSONObject(i).getString("name");
                                Stored_Ids[i] = user_id;

                                Toast.makeText(GET_Service_providers.this, "des "+ providersJsonArray.getJSONObject(i).getString("Designation"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception e) {
                            user_id = providersJsonArray.getJSONObject(i).getInt("user_id");
                            Provider_infoHashMap.put(user_id, new Provider_info
                                    (providersJsonArray.getJSONObject(i).getString("TypeofFirm"),
                                            providersJsonArray.getJSONObject(i).getString("emailid"),
                                            providersJsonArray.getJSONObject(i).getString("City"),
                                            providersJsonArray.getJSONObject(i).getString("name"),
                                            providersJsonArray.getJSONObject(i).getString("Designation"),
                                            providersJsonArray.getJSONObject(i).getString("emailid"),
                                            providersJsonArray.getJSONObject(i).getString("middle"),
                                            providersJsonArray.getJSONObject(i).getString("user_id"),
                                            providersJsonArray.getJSONObject(i).getString("Service"),
                                            "No Subservice found",
                                            providersJsonArray.getJSONObject(i).getString("feedbackservice")));

                            Destinations[i] = providersJsonArray.getJSONObject(i).getString("Designation");
                            City[i] = providersJsonArray.getJSONObject(i).getString("name");

                            Stored_Ids[i] = user_id;

                        }
//                        user_id = providersJsonArray.getJSONObject(i).getInt("user_id");
//                        Provider_infoHashMap.put(user_id, new Provider_info
//                                (providersJsonArray.getJSONObject(i).getString("TypeofFirm"),
//                                        providersJsonArray.getJSONObject(i).getString("emailid"),
//                                        providersJsonArray.getJSONObject(i).getString("City"),
//                                        providersJsonArray.getJSONObject(i).getString("name"),
//                                        providersJsonArray.getJSONObject(i).getString("Designation"),
//                                        providersJsonArray.getJSONObject(i).getString("emailid"),
//                                        providersJsonArray.getJSONObject(i).getString("middle"),
//                                        providersJsonArray.getJSONObject(i).getString("user_id"),
//                                        providersJsonArray.getJSONObject(i).getString("Service"),
//                                        providersJsonArray.getJSONObject(i).getString("ServiceSubCategory")));
//                        Destinations[i] = providersJsonArray.getJSONObject(i).getString("Designation");
//                        City[i] = providersJsonArray.getJSONObject(i).getString("name");

                        // Stored_Ids[i] = user_id;

                    }

                    if (Destinations.length != 0) {
                        for (int k = 0; k < Destinations.length; k++) {
                            if (loc_on_mark(Destinations[k]) != null) {
                                PlaceInfo placeInfo = new PlaceInfo();
                                placeInfo.setName("" + City[k]);
                                placeInfo.setAddress("" + Destinations[k] + "" + City[k]);
                                LatLng provider_location = loc_on_mark(Destinations[k]);
                                //LatLng provider_location = new LatLng(23.2599, 77.4126);

                                providerMarkers[k] = mMap.addMarker(new MarkerOptions()
                                        .position(provider_location)
                                        .anchor(0.5f, 0.5f)
                                        .title(City[k])
                                        .snippet("" + Destinations[k]));

                            }
                        }

                    }
                    Log.e("responce ", "is" + responce);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            super.onPostExecute(result);

        }

        private LatLng loc_on_mark(String destination) {

            geocoder = new Geocoder(GET_Service_providers.this,Locale.getDefault());
            try {
                event_address = geocoder.getFromLocationName(destination, 5);
                Toast.makeText(GET_Service_providers.this, "add "+event_address, Toast.LENGTH_SHORT).show();
                Log.e("log" , "destination"+destination);

                if (event_address.isEmpty()) {
                    return null;
                } else {
                    Address location = event_address.get(0);
                    location.getLatitude();
                    location.getLongitude();
                    p1 = new LatLng(location.getLatitude(), location.getLongitude());
                    Toast.makeText(GET_Service_providers.this, "ll "+p1, Toast.LENGTH_SHORT).show();


                }

           }

            catch (IOException e) {
                Toast.makeText(GET_Service_providers.this, "catch+ "+e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return p1;
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

    //------------------------------------------------------------

    public class SendRequest extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(GET_Service_providers.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/Add_approval");
                //  URL url = new URL("https://www.paramgoa.com/cpepsi/api/Add_approval");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("customer_id", AppPreference.getId(GET_Service_providers.this));
                postDataParams.put("provider_id", ProviderId);
                postDataParams.put("discription", CustProblem);

                //     Log.e("user_id", user_id + "");
                Log.e("user_id", user_id + "");
                Log.e("CustProblem", CustProblem);
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
                Log.e("SendRequest", result.toString());
                try {

                    jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    String responce = jsonObject.getString("responce");
                    Log.e(">>>>", jsonObject.toString() + " " + responce + " " + data);

                    if (responce.equals("true")) {
                        Toast.makeText(GET_Service_providers.this, responce, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GET_Service_providers.this, RequestActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(GET_Service_providers.this, "Some Problem.", Toast.LENGTH_SHORT).show();
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
    //*******************************************************************************************

    class GetSearch extends AsyncTask<String, String, String> {
        String output = "";
        ProgressDialog dialog;
        private String newText;

        public GetSearch(String newText) {
            this.newText = newText;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(GET_Service_providers.this);
            dialog.setMessage("Processing");
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "http://heightsmegamart.com/CPEPSI/api/search?place=" + newText;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("sever_url>>>>>>>>>", server_url);
            output = HttpHandler.makeServiceCall(server_url);
            System.out.println("getcomment_url" + output);
            return output;
        }

        @Override
        protected void onPostExecute(String output) {
            if (output == null) {
                dialog.dismiss();
            } else {
                try {
                    dialog.dismiss();
                    searchModelList.clear();
                    JSONObject object = new JSONObject(output);
                    String responce = object.getString("responce");
                    if (responce.equals("true")) {
                        JSONArray dataArray = object.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            String user_id = dataObj.getString("user_id");
                            String TypeofFirm = dataObj.getString("TypeofFirm");
                            String Designation = dataObj.getString("Designation");
                            String business = dataObj.getString("business");
                            String City = dataObj.getString("City");
                            String state = dataObj.getString("state");
                            String place = dataObj.getString("place");
                            String number = dataObj.getString("number");
                            String name = dataObj.getString("name");
                            String dob = dataObj.getString("dob");
                            String adharno = dataObj.getString("adharno");
                            String middle = dataObj.getString("middle");
                            String sirname = dataObj.getString("sirname");
                            String emailid = dataObj.getString("emailid");
                            String password = dataObj.getString("password");
                            String service = dataObj.getString("service");
                            String sub_service = dataObj.getString("sub_service");
                            String status = dataObj.getString("status");
                            String image = dataObj.getString("image");
                            searchModelList.add(new SearchModel(user_id, TypeofFirm, Designation, business, City, state, place, number,
                                    name, dob, adharno, middle, sirname, emailid, password, service, sub_service, status, image));
                        }
                        searchAdapter = new SearchAdapter(GET_Service_providers.this, searchModelList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(GET_Service_providers.this);
                        serachResult.setLayoutManager(mLayoutManager);
                        serachResult.setItemAnimator(new DefaultItemAnimator());
                        serachResult.setAdapter(searchAdapter);
                      /*  searchAdapter = new SearchAdapter(GET_Service_providers.this, searchModelList);
                        serachResult.setAdapter(searchAdapter);*/

                    } else {
                        Toast.makeText(GET_Service_providers.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }

    //*******************************************************************************************
}
