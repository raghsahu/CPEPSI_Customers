package com.customer.admin.cpepsi_customers.Java_files;

import java.io.Serializable;

public class ApiModel implements Serializable {

    private String id;
    private String service;
    private String type;
    private String image;
    private String status;

    public ApiModel(String id, String service, String type, String image, String status) {
        this.id = id;
        this.service = service;
        this.type = type;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
