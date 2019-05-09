package com.customer.admin.cpepsi_customers.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*import com.example.ics.pollchat.BeanClass.contactdetail;*/

/**
 * Created by ICS on 5/3/2016.
 */
public class Utilities {


    public static String getRequestAndfindJSON(String sever_url, Context context){

        String output = "";
        InputStream is = null;
       // String Token = AppPrefences.getToken(context);


        try {
            URL url = new URL(sever_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(25000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");
            conn.setRequestProperty("Content-Language", "en-US");
          //  conn.setRequestProperty("BearerToken", AppPrefences.getToken(context));
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            int response = conn.getResponseCode();

            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = "";
            int i = 0;
            while ((i = is.read()) != -1) {
                contentAsString = contentAsString + (char) i;
            }
            is.close();
            output=contentAsString;
            // Toast.makeText(CatlogueSelect.this,contentAsString,Toast.LENGTH_LONG).show();
            Log.d("The response : ", contentAsString);

        }
        catch (Exception e) {
            System.out.println("exception in jsonparser class ........");
            e.printStackTrace();
            return null;
        }

        return output;

    }

/*public static String getResizedBitmap(Bitmap bm,int newHeight,int newWidth)

{
    int width=newWidth;
    int height =newHeight;
    float scaleWidth=((float)newWidth/width);
    float scaleheight=((float)newHeight/height);
    Matrix matrix=new Matrix();

   return resizedimage;
}*/
    //method for post json
    public static String postParamsAndfindJSON(String url, String params) {
        JSONObject jObj = new JSONObject();
        String result = "";
        System.out.println("URL comes in jsonparser class is:  " + url + params);
        try {
            URL myurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) myurl.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(params.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            //Send request
            DataOutputStream wr = new DataOutputStream(
                    connection.getOutputStream ());
            wr.writeBytes(params);
            wr.flush();
            wr.close();
            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();


        } catch (Exception e) {
            System.out.println("exception in jsonparser class ........");
            e.printStackTrace();
            return null;
        }


    }

    //post method with legacy jar file
    public static String postParamsAndfindJSONwithLegacy(String url, ArrayList<NameValuePair> params) {
        JSONObject jObj = new JSONObject();
        String result = "";

        System.out.println("URL comes in jsonparser class is:  " + url + params);
        try {
            int TIMEOUT_MILLISEC = 10000; // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            // httpGet.setURI(new URI(url));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int status = httpResponse.getStatusLine().getStatusCode();
            System.out.println("status  in jsonparser class ........" + status);
            InputStream is = httpResponse.getEntity().getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            System.out.println("result  in jsonparser class ........" + result);

        } catch (Exception e) {
            System.out.println("exception in jsonparser class ........");
            e.printStackTrace();
            return null;
        }
        return result;
    }

    public static String postEntityAndFindJson(String url, MultipartEntity params) {
        JSONObject jObj = new JSONObject();
        String result = "";

        System.out.println("URL comes in jsonparser class is:  " + url + params);
        try {
            int TIMEOUT_MILLISEC = 10000; // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(params);
            // httpGet.setURI(new URI(url));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            int status = httpResponse.getStatusLine().getStatusCode();
            System.out.println("status  in jsonparser class ........" + status);
            InputStream is = httpResponse.getEntity().getContent();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            System.out.println("result  in jsonparser class ........" + result);

        } catch (Exception e) {
            System.out.println("exception in jsonparser class ........");
            e.printStackTrace();
            return null;
        }
        return result;
    }
    public static boolean isValidna(String na) {
        if (na.length() != 0 && na.length() < 10) {
            return true;
        }
        return false;
    }

    // Method or showing alert dialog
    public static void Alert(final Context context, final String string) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert");
       // builder.seticon(R.drawable.common_plus_signin_btn_text_light_normal);
//        builder.seticon(R.drawable.logo);
        builder.setMessage(string)
                .setCancelable(false).setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    //Method for St type face


    //Method for checking internet connection
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static void ShowToastMessage(Context ctactivity, String message) {
        Toast toast = Toast.makeText(ctactivity, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static boolean isValidName(String name) {

        if (name==null){
            return false;
        }else {
            String NAME_PATTERN = "^[A-Za-z]+$";
            Pattern pattern = Pattern.compile(NAME_PATTERN);
            Matcher matcher = pattern.matcher(name);
            return matcher.matches();
        }

    }
    public static boolean isValidNumber(String number) {

        if (number==null){
            return false;
        }
        else {
            String NAME_PATTERN = "[0-9]+";
            Pattern pattern = Pattern.compile(NAME_PATTERN);
            Matcher matcher = pattern.matcher(number);
            return matcher.matches();
        }

    }
    public final static boolean isValidEmail(CharSequence email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }




 /*  public static ArrayList<contactdetail> removeDuplicates(ArrayList<contactdetail> list) {

       // Store unique items in result.
       ArrayList<contactdetail> result = new ArrayList<>();

       // Record encountered Strings in HashSet.
       HashSet<contactdetail> set = new HashSet<>();

       // Loop over argument list.
       for (contactdetail contactdetail1 : list) {

           // If String is not in set, add it to the list and the set.
           if (!set.contains(contactdetail1)) {
               result.add(contactdetail1);
               set.add(contactdetail1);
           }
       }
       return result;
   }*/
}
