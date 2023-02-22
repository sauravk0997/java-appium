package com.disney.qa.api.disney.pojo;

public class DisneySubscriptions {

    private String orderId = "";

    private String subscriptionId = "";

    private String subscriptionSku = "";

    private String subscriptionProductId = "";

    private String subscriptionGuid = "";

    private String subscriptionStatus = "";
    
    private String orderRef = "";
    
    public String getOrderId() {
        return orderId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public String getSubscriptionSku() {
        return subscriptionSku;
    }

    public String getSubscriptionProductId() {
        return subscriptionProductId;
    }

    public String getSubscriptionGuid() {
        return subscriptionGuid;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public String getOrderRef() {
        return orderRef;        
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public void setSubscriptionSku(String subscriptionSku) {
        this.subscriptionSku = subscriptionSku;
    }

    public void setSubscriptionProductId(String subscriptionProductId) {
        this.subscriptionProductId = subscriptionProductId;
    }

    public void setSubscriptionGuid(String subscriptionGuid) {
        this.subscriptionGuid = subscriptionGuid;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

}
