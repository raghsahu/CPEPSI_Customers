package com.customer.admin.cpepsi_customers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customer.admin.cpepsi_customers.util.AppPreference;
import com.customer.admin.cpepsi_customers.util.CameraUtils;
import com.customer.admin.cpepsi_customers.util.CommonUtils;
import com.customer.admin.cpepsi_customers.util.Utilities;
import com.customer.admin.cpepsi_customers.util.Utility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    EditText profileName, profileEmail, profileNumber, profilePassword,profileLocation;
    String ProfileName, ProfileEmail, ProfileNumber, ProfilePassword,ProfileLocation;
    Button profile_submit;
    private String userChoosenTask;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final String KEY_IMAGE_STORAGE_PATH = "image_path";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final String GALLERY_DIRECTORY_NAME = "Hello Camera";
    public static final String IMAGE_EXTENSION = "jpg";
    private static String imageStoragePath;
    private Bundle savedInstanceState;
    int Gallery_view = 2;
    String UserId;
    private String communStr = "http://heightsmegamart.com/CPEPSI/uploads/";
  //  private String communStr = "https://www.paramgoa.com/cpepsi/uploads/";
    String image;
    CircleImageView profile_image;
    ImageView badge_notification_1;

    TextView tv_plan_type;


//*****************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = (EditText) findViewById(R.id.profileName);
        profileEmail = (EditText) findViewById(R.id.profileEmail);
        profileNumber = (EditText) findViewById(R.id.profileNumber);
       // profilePassword = (EditText) findViewById(R.id.profilePassword);
        profileLocation = (EditText) findViewById(R.id.profileLocation);
        profile_submit = (Button) findViewById(R.id.profile_submit);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        badge_notification_1 = (ImageView) findViewById(R.id.badge_notification_1);
        tv_plan_type=findViewById(R.id.tv_plan_type);

     //   UserId = AppPreference.getId(ProfileActivity.this);
    //    Log.e("UserId>>>", UserId);

        badge_notification_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        if (Connectivity.isNetworkAvailable(this)) {
            new PostProfile().execute();
        } else {
            Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
        }

        profile_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileName = profileName.getText().toString();
                ProfileEmail = profileEmail.getText().toString();
                ProfileNumber = profileNumber.getText().toString();
               // ProfilePassword = profilePassword.getText().toString();
                ProfileLocation = profileLocation.getText().toString();

                if (Connectivity.isNetworkAvailable(ProfileActivity.this)) {
                    new PostProfileUpdate().execute();
                } else {
                    Toast.makeText(ProfileActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //----------------------------------------------

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProfileActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        if (CameraUtils.checkPermissions(getApplicationContext())) {
                            captureImage();
                            restoreFromBundle(savedInstanceState);
                        } else {
                            requestCameraPermission(MEDIA_TYPE_IMAGE);
                        }
                    //captureImage();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //------------ private void galleryIntent()
    private void galleryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start new activity with the LOAD_IMAGE_RESULTS to handle back the results when image is picked from the Image Gallery.
        startActivityForResult(i, Gallery_view);
    }

 /*   private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(KEY_IMAGE_STORAGE_PATH, imageStoragePath);
    }

    /**
     * Restoring image path from saved instance state
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
    }
    //--------------------------------------------------------------------

    private void restoreFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(KEY_IMAGE_STORAGE_PATH)) {
                imageStoragePath = savedInstanceState.getString(KEY_IMAGE_STORAGE_PATH);
                if (!TextUtils.isEmpty(imageStoragePath)) {
                    if (imageStoragePath.substring(imageStoragePath.lastIndexOf(".")).equals("." + IMAGE_EXTENSION)) {
                        previewCapturedImage();
                    }
                }
            }
        }
    }

    private void previewCapturedImage() {

        try {
            // hide video preview
            Bitmap bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            new ImageCompression().execute(imageStoragePath);

            profile_image.setImageBitmap(bitmap);
            File imgFile = new File(imageStoragePath);
/*
            if (imgFile.exists()) {
                //  imgPolicyNo.setImageURI(Uri.fromFile(imgFile));
                //  show(imgFile);
                new ImageUploadTask(imgFile).execute();
                //  Toast.makeText(ClaimActivity.this,"data:="+imgFile,Toast.LENGTH_LONG).show();
            }*/

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions required!")
                .setMessage("Camera needs few permissions to work properly. Grant them in settings.")
                .setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(ProfileActivity.this);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;
        String result = "";
        File Image;

        public ImageUploadTask(File imgFile) {
            this.Image = imgFile;

        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ProfileActivity.this);
            dialog.setMessage("Processing");

            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            try {

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);


                entity.addPart("image", new FileBody(Image));
                entity.addPart("customer_id", new StringBody(AppPreference.getId(ProfileActivity.this)));


                result = Utilities.postEntityAndFindJson("http://heightsmegamart.com/CPEPSI/api/Cust_upload_profile", entity);
            //    result = Utilities.postEntityAndFindJson("https://www.paramgoa.com/cpepsi/api/Cust_upload_profile", entity);

                return result;

            } catch (Exception e) {
                // something went wrong. connection with the server error
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // Log.v("profile",result);

            String result1 = result;
            if (result != null) {

                dialog.dismiss();
                Log.e("result_Image", result);
                try {
                    JSONObject object = new JSONObject(result);
                    String data = object.getString("data");
                    image = object.getString("image");
                    String responce = object.getString("responce");

                    if (responce.equals("true")) {
//                        if (!image.isEmpty()){
//                            AppPreference.setProfileImage(ProfileActivity.this,image);
//                        }

                        Toast.makeText(ProfileActivity.this, data, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, data, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    dialog.dismiss();
                    Toast.makeText(ProfileActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            } else {
                dialog.dismiss();
                //  Toast.makeText(Registration.this, "No Response From Server", Toast.LENGTH_LONG).show();
            }
        }
    }

    //.............................................................................
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = CameraUtils.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Refreshing the gallery
                previewCapturedImage();

                CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                // successfully captured the image
                // display it in image view
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == Gallery_view && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            imageStoragePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            profile_image.setImageBitmap(BitmapFactory.decodeFile(imageStoragePath));
            if (imageStoragePath != null) {
                new ImageCompression().execute(imageStoragePath);
            }

            cursor.close();
        }
    }

    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    //---------------------------

    public class ImageCompression extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return CommonUtils.compressImage(strings[0]);
        }

        protected void onPostExecute(String imagePath) {
            // imagePath is path of new compressed image.
//            mivImage.setImageBitmap(BitmapFactory.decodeFile(new File(imagePath).getAbsolutePath()));
            if (imagePath != null) {
                File imgFile = new File(imagePath);
                if (imgFile.exists()) {
                    new ImageUploadTask(imgFile).execute();
                }
            } else {
                //  AlertDialogCreate();
            }

        }
    }

    //-----------------------------------------------

    public class PostProfile extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(ProfileActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/CustGet_profile");
              //  URL url = new URL("https://www.paramgoa.com/cpepsi/api/CustGet_profile");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("customer_id", AppPreference.getId(ProfileActivity.this));
              //  Log.e("UserId", UserId);
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
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    String id = dataObj.getString("id");
                    String name = dataObj.getString("name");
                    String email = dataObj.getString("email");
                    String contact = dataObj.getString("contact");
                   // String password = dataObj.getString("password");
                    String address = dataObj.getString("address");
                    String status = dataObj.getString("status");
                    String payment_status = dataObj.getString("payment_status");
                    String payment_amount = dataObj.getString("payment_amount");
                    String image = dataObj.getString("image");

                    AppPreference.setName(ProfileActivity.this, name);
                    AppPreference.setEmail(ProfileActivity.this, email);
                    AppPreference.setContact(ProfileActivity.this, contact);
                    AppPreference.setAddress(ProfileActivity.this, address);

                    if (status.equals("1")){
                        tv_plan_type.setText("You are using 30 days free trial 'Daily Life Service', After free trial you can use Paid Service.");
                    }
                    if (status.equals("2")){
                        tv_plan_type.setText("You are using Paid 'Daily Life Service'.");
                    }
                    if (status.equals("0")){
                        tv_plan_type.setText("Your Free Trial 'Daily Life Service' Hasbeen Expired, Please Pay Only One Time Payment Amount And Use Permanent 'Daily Life Service'");
                    }

                    profileName.setText(name);
                    profileEmail.setText(email);
                    profileNumber.setText(contact);
                   // profilePassword.setText(password);
                    profileLocation.setText(address);

                    Picasso.with(ProfileActivity.this)
                            .load(communStr + image)
                            .placeholder(R.drawable.noimg)
                            .into(profile_image);

                    Log.e("communStr + image>>>", communStr + image);

                    if (responce.equals("true")) {
                        Toast.makeText(ProfileActivity.this, responce, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, responce, Toast.LENGTH_SHORT).show();
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

    //-------------------------------------------------------------

    public class PostProfileUpdate extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        protected void onPreExecute() {
            dialog = new ProgressDialog(ProfileActivity.this);
            dialog.show();

        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://heightsmegamart.com/CPEPSI/api/custmoer_update");
             //   URL url = new URL("https://www.paramgoa.com/cpepsi/api/custmoer_update");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("customer_id", AppPreference.getId(ProfileActivity.this));
                postDataParams.put("name", ProfileName);
                postDataParams.put("contact", ProfileNumber);
                postDataParams.put("email", ProfileEmail);
               // postDataParams.put("password", ProfilePassword);
                postDataParams.put("address", ProfileLocation);

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
                    JSONObject dataObj = jsonObject.getJSONObject("data");
                    String name = dataObj.getString("name");
                    String contact = dataObj.getString("contact");
                    String email = dataObj.getString("email");
                   // String password = dataObj.getString("password");
                    String address = dataObj.getString("address");

                    profileName.setText("");
                    profileEmail.setText("");
                    profileNumber.setText("");
                    //profilePassword.setText("");
                    profileLocation.setText("");


                    if (responce.equals("true")) {

                        AppPreference.setName(ProfileActivity.this, name);
                        AppPreference.setEmail(ProfileActivity.this, email);
                        AppPreference.setContact(ProfileActivity.this, contact);
                        AppPreference.setAddress(ProfileActivity.this, address);


                        Intent intent = new Intent(ProfileActivity.this, Main_Provider.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(ProfileActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, responce, Toast.LENGTH_SHORT).show();
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
