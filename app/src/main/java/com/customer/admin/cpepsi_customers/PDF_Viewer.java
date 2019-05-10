package com.customer.admin.cpepsi_customers;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.media.Image;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PDF_Viewer extends AppCompatActivity {

    TextView image_pdf;
  WebView webview_terms;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf__viewer);


        webview_terms=findViewById(R.id.webview_terms);

        webview_terms.getSettings().setJavaScriptEnabled(true);
        webview_terms.loadUrl("http://android**********" +
                "Terms and Conditions");



//        image_pdf=findViewById(R.id.image_pdf);
//
//        AssetManager assetManager = getAssets();
//
//        InputStream input;
//        try {
//            input = assetManager.open("terms_and_conditions.docx");
//
//            int size = input.available();
//            byte[] buffer = new byte[size];
//            input.read(buffer);
//            input.close();
//
//            // byte buffer into a string
//            String text = new String(buffer);
//
//            image_pdf.setText(text);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }





//////////////////////*********************************************************************************
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openPDF(ImageView targetView) throws IOException {

        //open file in assets

        ParcelFileDescriptor fileDescriptor;

        String FILENAME = "terms_and_conditions.docx";


        // Create file object to read and write on
        File file = new File(PDF_Viewer.this.getCacheDir(), FILENAME);
        if (!file.exists()) {
            AssetManager assetManager = PDF_Viewer.this.getAssets();
            //FileUtils.copyAsset(assetManager, FILENAME, file.getAbsolutePath());
        }

        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

        PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);

        //Display page 0
        PdfRenderer.Page rendererPage = pdfRenderer.openPage(0);
        int rendererPageWidth = rendererPage.getWidth();
        int rendererPageHeight = rendererPage.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(
                rendererPageWidth,
                rendererPageHeight,
                Bitmap.Config.ARGB_8888);
        rendererPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        targetView.setImageBitmap(bitmap);
        rendererPage.close();
        pdfRenderer.close();
    }


    public static boolean copyAsset(AssetManager assetManager, String fromAssetPath, String toPath) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(fromAssetPath);
            new File(toPath).createNewFile();
            out = new FileOutputStream(toPath);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
}
