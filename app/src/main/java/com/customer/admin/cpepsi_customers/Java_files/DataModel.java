package com.customer.admin.cpepsi_customers.Java_files;

public class DataModel {

    private String id;
    private String service;
    private String type;
    private String serviceSubCategory;
    private String status;

    public DataModel(String id, String service, String type, String serviceSubCategory, String status) {
        this.id = id;
        this.service = service;
        this.type = type;
        this.serviceSubCategory = serviceSubCategory;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServiceSubCategory() {
        return serviceSubCategory;
    }

    public void setServiceSubCategory(String serviceSubCategory) {
        this.serviceSubCategory = serviceSubCategory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
