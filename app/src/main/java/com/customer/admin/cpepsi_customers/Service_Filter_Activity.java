package com.customer.admin.cpepsi_customers;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.Adapters.SearchAdapter;
import com.customer.admin.cpepsi_customers.Adapters.Service_Recycler_Adapter;
import com.customer.admin.cpepsi_customers.Java_files.ApiModel;
import com.customer.admin.cpepsi_customers.Java_files.SearchModel;
import com.customer.admin.cpepsi_customers.util.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Service_Filter_Activity extends AppCompatActivity implements Service_Recycler_Adapter.Service_Recycler_Adapter_Listener{

  SearchView searchView_service;
  RecyclerView recycler_serachResult;
    String server_url;

    ArrayList<ApiModel> MainModelArrayList = new ArrayList<>();
    Service_Recycler_Adapter service_recycler_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service__filter_);

        searchView_service=findViewById(R.id.searchView_service);
        recycler_serachResult=findViewById(R.id.serach_result);

        searchView_service.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (Connectivity.isNetworkAvailable(Service_Filter_Activity.this)) {
                    if (!newText.isEmpty()){
                        new GetSearch_Service(newText).execute();
                    }else {
                        try{
                            if (!MainModelArrayList.isEmpty()){
                                MainModelArrayList.clear();
                                service_recycler_adapter.notifyDataSetChanged();
                            }
                        }catch (Exception e){

                        }
                        //Toast.makeText(Service_Filter_Activity.this, "No text found", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Service_Filter_Activity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });


    }

    @Override
    public void onItemSelected(ApiModel apiModel) {

    }

    private class GetSearch_Service extends AsyncTask<String, String, String> {
        String output = "";
       // ProgressDialog dialog;
        private String newText;

        public GetSearch_Service(String newText) {
            this.newText = newText;
        }

        @Override
        protected void onPreExecute() {
//            dialog = new ProgressDialog(Service_Filter_Activity.this);
//            dialog.setMessage("Processing");
//            dialog.setCancelable(true);
//            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                server_url = "http://heightsmegamart.com/CPEPSI/api/get_service_name?service_name=" + newText;
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
                //dialog.dismiss();
            } else {
                try {
                    //dialog.dismiss();
                    MainModelArrayList.clear();
                    JSONObject jsonObject = new JSONObject(output);
                    String responce = jsonObject.getString("responce");
                    if (responce.equals("true")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject Service_json_object = jsonArray.getJSONObject(i);
                                String id = Service_json_object.getString("id");
                                String Service = Service_json_object.getString("Service");
                                String type = Service_json_object.getString("type");
                                String image = Service_json_object.getString("image");
                                String status = Service_json_object.getString("status");

                                MainModelArrayList.add(new ApiModel(id, Service, type, image, status));

                            }
                            service_recycler_adapter = new Service_Recycler_Adapter(Service_Filter_Activity.this, MainModelArrayList, Service_Filter_Activity.this);
                            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Main_Provider.this);
                            GridLayoutManager manager = new GridLayoutManager(Service_Filter_Activity.this, 3, GridLayoutManager.VERTICAL, false);
                            recycler_serachResult.setLayoutManager(manager);
                            recycler_serachResult.setItemAnimator(new DefaultItemAnimator());
                            recycler_serachResult.setAdapter(service_recycler_adapter);

                    } else {
                        try{
                                MainModelArrayList.clear();
                                service_recycler_adapter.notifyDataSetChanged();

                        }catch (Exception e){
                            //Toast.makeText(Service_Filter_Activity.this, "pp", Toast.LENGTH_SHORT).show();
                        }

                        MainModelArrayList.clear();
                        service_recycler_adapter.notifyDataSetChanged();
                        Toast.makeText(Service_Filter_Activity.this, "No Service Found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //dialog.dismiss();
                }
                super.onPostExecute(output);
            }
        }
    }

}
