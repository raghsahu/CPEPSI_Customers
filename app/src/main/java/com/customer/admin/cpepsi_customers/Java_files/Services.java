package com.customer.admin.cpepsi_customers.Java_files;

import android.widget.TextView;

public class Services {
    String service_doc;
    TextView Download;
    String Email;
    String Doc_date;

//
//    public Reports(String report_Doc, String aContinue, String view_profile) {
//        Report_Doc = report_Doc;
//        Continue = aContinue;
//        View_profile = view_profile;
//    }


    public String getDoc_date() {
        return Doc_date;
    }

    public void setDoc_date(String doc_date) {
        Doc_date = doc_date;
    }

    public TextView getDownload() {
        return Download;
    }

    public void setDownload(TextView download) {
        Download = download;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


//    public String getContinue() {
//        return Continue;
//    }
//
//    public void setContinue(String aContinue) {
//        Continue = aContinue;
//    }

    public Services(String services_doc) {
        this.service_doc = services_doc;

    }

    public String getService_doc() {
        return service_doc;
    }

    public void setService_doc(String report_Doc) {
        service_doc = report_Doc;
    }
}