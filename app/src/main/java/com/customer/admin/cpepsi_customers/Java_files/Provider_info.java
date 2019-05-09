package com.customer.admin.cpepsi_customers.Java_files;

public class Provider_info {
    String State;
    String Place;
    String TypeofFirm, NameFirm, NatureFirm, contactpersonname, Designation, number, Email_id, provider_name, procontactnumber,contactpersonnames,user_id,Service,ServiceSubCategory,feedbackservice;


    public Provider_info(String typeofFirm, String nameFirm, String natureFirm, String contactpersonname, String designation, String email_id, String provider_name, String user_id, String service, String serviceSubCategory,String feedbackservice) {
        TypeofFirm = typeofFirm;
        NameFirm = nameFirm;
        NatureFirm = natureFirm;
       this.contactpersonnames = contactpersonname;
        Designation = designation;
        Email_id = email_id;
        this.provider_name = provider_name;
        Service = service;
        ServiceSubCategory = serviceSubCategory;
        this.user_id = user_id;
        this.feedbackservice = feedbackservice;

    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public Provider_info(String name, String service, String designation, String place, String state, String emailid) {
        this.provider_name = name;
        this.Service = service;
        this.Designation = designation;
        this.Place = place;
        this.State = state;
        this.Email_id = emailid;

    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getTypeofFirm() {
        return TypeofFirm;
    }

    public void setTypeofFirm(String typeofFirm) {
        TypeofFirm = typeofFirm;
    }

    public String getNameFirm() {
        return NameFirm;
    }

    public void setNameFirm(String nameFirm) {
        NameFirm = nameFirm;
    }

    public String getNatureFirm() {
        return NatureFirm;
    }

    public void setNatureFirm(String natureFirm) {
        NatureFirm = natureFirm;
    }

    public String getContactpersonnames() {
        return contactpersonnames;
       // this.contactpersonnames = contactpersonname;
    }

    public void setContactpersonnames(String contactpersonname) {
        this.contactpersonname = contactpersonname;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail_id() {
        return Email_id;
    }

    public void setEmail_id(String email_id) {
        Email_id = email_id;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public String getProcontactnumber() {
        return procontactnumber;
    }

    public void setProcontactnumber(String procontactnumber) {
        this.procontactnumber = procontactnumber;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        Service = service;
    }

    public String getServiceSubCategory() {
        return ServiceSubCategory;
    }

    public void setServiceSubCategory(String serviceSubCategory) {
        ServiceSubCategory = serviceSubCategory;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFeedbackservice() {
        return feedbackservice;
    }

    public void setFeedbackservice(String feedbackservice) {
        this.feedbackservice = feedbackservice;
    }
}
