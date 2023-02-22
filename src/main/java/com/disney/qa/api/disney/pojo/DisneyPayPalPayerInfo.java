package com.disney.qa.api.disney.pojo;

import com.disney.qa.disney.DisneyCountryData;

public class DisneyPayPalPayerInfo {

    private String payerCountry = "US";
    private String payer = "qaa@disneystreaming.com";
    private String payerId = "1LAJ4K3AFE52";
    private DisneyPayPalPayerName payerName = new DisneyPayPalPayerName();

    public DisneyPayPalPayerInfo() {

    }

    public DisneyPayPalPayerInfo(DisneyAccount account) {
        String countryCode = account.getProfileCountry();
        DisneyCountryData countryData = new DisneyCountryData();

        this.payerCountry = countryCode.toUpperCase();
        this.setPayer(countryData.searchAndReturnCountryData(countryCode, "code", "paypalEmail"));
        DisneyPayPalPayerName payPalPayerName= new DisneyPayPalPayerName();
        this.setPayerName(payPalPayerName);
    }

    public String getPayerCountry() {
        return payerCountry;
    }

    public DisneyPayPalPayerName getPayerName() {
        return payerName;
    }

    public String getPayer() {
        return payer;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerCountry(String payerCountry) {
        this.payerCountry = payerCountry;
    }

    public void setPayerName(DisneyPayPalPayerName payerName) {
        this.payerName = payerName;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }
}
