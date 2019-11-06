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

  WebView webview_terms;
    String term_and_conditions="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf__viewer);


        webview_terms=findViewById(R.id.webview_terms);

        webview_terms.getSettings().setJavaScriptEnabled(true);
        webview_terms.getSettings().setSupportZoom(true);
        webview_terms.loadUrl("http://jntrcpl.com/CPEPSI/term_condition_app.php");

       // webview_terms.loadData(term_and_conditions, "text/html; charset=UTF-8", null);
       // webview_terms.loadDataWithBaseURL(null, "<html>...</html>", "text/html", "utf-8", null);


    }





}
