package com.disney.qa.api.disney.pojo;

public class DisneyEntitlement {

    DisneyOffer offer;
    String subVersion;

    public DisneyEntitlement(DisneyOffer offer, String subVersion) {
        this.setOffer(offer);
        this.setSubVersion(subVersion);
    }

    public DisneyOffer getOffer() {
        return offer;
    }

    public String getSubVersion() {
        return subVersion;
    }

    public void setOffer(DisneyOffer offer) {
        this.offer = offer;
    }

    public void setSubVersion(String subVersion) {
        this.subVersion = subVersion;
    }
}
