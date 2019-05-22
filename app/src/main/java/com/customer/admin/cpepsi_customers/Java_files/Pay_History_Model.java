package com.customer.admin.cpepsi_customers.Java_files;

public class Pay_History_Model {
    private String ph_id;
    private String user_id;
    private String amount;
    private String remark;
    private String payment_type;
    private String datetime;
    private String provider_name;
    private String service_sub;


    public Pay_History_Model(String ph_id, String user_id, String amount, String remark, String payment_type, String datetime, String provider_name, String service_sub) {

   this.ph_id=ph_id;
   this.user_id=user_id;
   this.amount=amount;
   this.remark=remark;
   this.payment_type=payment_type;
   this.datetime=datetime;
   this.provider_name=provider_name;
   this.service_sub=service_sub;

    }

    public String getPh_id() {
        return ph_id;
    }

    public String getRemark() {
        return remark;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getProvider_name() {
        return provider_name;
    }

    public void setProvider_name(String provider_name) {
        this.provider_name = provider_name;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public String getService_sub() {
        return service_sub;
    }

    public void setService_sub(String service_sub) {
        this.service_sub = service_sub;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setPh_id(String ph_id) {
        this.ph_id = ph_id;
    }
}
