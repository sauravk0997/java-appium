package com.disney.qa.api.disney.pojo;

public class DisneyAffiliateTracker {

    private String affiliateId = "";
    private String partnerId = "";
    private String source = "";

    public String getAffiliateId() {
        return affiliateId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public String getSource() {
        return source;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
