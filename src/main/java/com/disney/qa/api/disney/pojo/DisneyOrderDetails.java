package com.disney.qa.api.disney.pojo;

import java.util.ArrayList;
import java.util.List;

public class DisneyOrderDetails {

    private String paymentMethodId;
    private String cardSecurityCode = "123";
    private DisneyAffiliateTracker affiliateTracking = new DisneyAffiliateTracker();
    private List<DisneyLineItems> lineItems = new ArrayList<>();
    private List<DisneyCampaigns> orderCampaigns = new ArrayList<>();

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public String getCardSecurityCode() {
        return cardSecurityCode;
    }

    public DisneyAffiliateTracker getAffiliateTracking() {
        return affiliateTracking;
    }

    public List<DisneyLineItems> getLineItems() {
        return lineItems;
    }

    public List<DisneyCampaigns> getOrderCampaigns() {
        return orderCampaigns;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public void setCardSecurityCode(String cardSecurityCode) {
        this.cardSecurityCode = cardSecurityCode;
    }

    public void setAffiliateTracking(DisneyAffiliateTracker affiliateTracking) {
        this.affiliateTracking = affiliateTracking;
    }

    public void setLineItems(List<DisneyLineItems> lineItems) {
        this.lineItems = lineItems;
    }

    public void setOrderCampaigns(List<DisneyCampaigns> orderCampaigns) {
        this.orderCampaigns = orderCampaigns;
    }
}
