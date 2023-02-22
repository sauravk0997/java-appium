package com.disney.qa.api.disney.pojo;

import com.disney.qa.disney.DisneyCountryData;

public class DisneyBillingDetails {

    private String country = "US";
    private String postalCode = "10011";
    private String city = "New York";
    private String state = "NY";
    private String line1 = "5";
    private String line2 = "Main Street";

    public DisneyBillingDetails() {

    }

    public DisneyBillingDetails(DisneyAccount account) {
        String countryCode = account.getProfileCountry();
        DisneyCountryData countryData = new DisneyCountryData();

        this.country = countryCode.toUpperCase();
        this.postalCode = countryData.searchAndReturnCountryData(countryCode, "code", "zip");
        this.city = countryCode;
        this.state = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }
}
