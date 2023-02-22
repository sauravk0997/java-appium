package com.disney.qa.api.disney.pojo;

import com.disney.qa.api.disney.DisneyOrder;
import com.disney.qa.api.disney.DisneyOrderParameters;
import com.disney.qa.disney.DisneyCountryData;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

public class DisneyPaymentMethodCredit {

    private String alternateName = "Disney Subscription Card";
    private String token = DisneyOrderParameters.getTokenIdForEnvironment();
    private String cardType = "CREDIT";
    private String cardTypeOverride = "CREDIT";
    private String firstSix = "500000";
    private String lastFour = "1234";
    private int expiryMonth = 8;
    private int expiryYear = 2026;
    private String brand = "VISA";
    private String country = "US";
    private String issuer = "CITIBANK";
    private String ownerFullName = "John Smith";
    private String fingerprint = "2C399FC3-3572-40A3-B491-CDF2A57F5DD9";
    private String usage = "multi_use";
    private DisneyBillingDetails billingAddress = new DisneyBillingDetails();

    private boolean isCorporate = true;

    private boolean isDefault = false;

    private boolean isShared = true;

    public DisneyPaymentMethodCredit(DisneyAccount account) {

        String countryCode = account.getProfileCountry();
        DisneyCountryData countryData = new DisneyCountryData();

        String expiration = countryData.searchAndReturnCountryData(countryCode, "code", "exp");
        int month = Integer.parseInt(expiration.substring(0, expiration.indexOf('/')));
        int year = 2000 + Integer.parseInt(expiration.substring(expiration.indexOf('/') + 1));

        String cc = countryData.searchAndReturnCountryData(countryCode, "code", DisneyOrderParameters.getProperCreditString());
        cc = cc.replace(" ", "");
        String ccFirstSix = cc.substring(0,6);
        String ccLastFour = cc.substring(cc.length()-4);

        this.setExpiryYear(year);
        this.setExpiryMonth(month);
        this.setFirstSix(ccFirstSix);
        this.setLastFour(ccLastFour);
        this.setCountry(countryCode.toUpperCase());

        DisneyBillingDetails billingDetails = new DisneyBillingDetails(account);
        billingDetails.setState(countryCode);
        billingDetails.setCity(countryCode);
        billingDetails.setCountry(countryCode.toUpperCase());
        billingDetails.setPostalCode(countryData.searchAndReturnCountryData(countryCode, "code", "zip"));
        this.setBillingAddress(billingDetails);

        this.setShared(!account.getOrderSettings().contains(DisneyOrder.SET_IS_NOT_SHARED));
    }

    public String getAlternateName() {
        return alternateName;
    }

    public String getToken() {
        return token;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardTypeOverride() {
        return cardTypeOverride;
    }

    public String getFirstSix() {
        return firstSix;
    }

    public String getLastFour() {
        return lastFour;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public String getBrand() {
        return brand;
    }

    public String getCountry() {
        return country;
    }

    public String getIssuer() {
        return issuer;
    }

    public DisneyBillingDetails getBillingAddress() {
        return billingAddress;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public String getOwnerFullName() {
        return ownerFullName;
    }

    public String getUsage() {
        return usage;
    }

    public boolean isCorporate() {
        return isCorporate;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public void setBillingAddress(DisneyBillingDetails billingAddress) {
        this.billingAddress = billingAddress;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardTypeOverride(String cardTypeOverride) {
        this.cardTypeOverride = cardTypeOverride;
    }

    public void setIsCorporate(boolean isCorporate) {
        this.isCorporate = isCorporate;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public void setFirstSix(String firstSix) {
        this.firstSix = firstSix;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setLastFour(String lastFour) {
        this.lastFour = lastFour;
    }

    public void setOwnerFullName(String ownerFullName) {
        this.ownerFullName = ownerFullName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setShared(boolean shared) {
        this.isShared = shared;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String toJsonString() {
        return replaceSpecialFields(new JSONObject(this).toString());
    }

    /**
     *  This fixes a json string for this class by undoing the JSON parsing stripping 'is'.
     * @param string takes a string to fix the JSON parsing.
     * @return provides a valid string for use.
     */
    private String replaceSpecialFields(String string) {
        String[] fieldsToReplace = {"corporate", "default", "shared"};

        for (String field : fieldsToReplace) {
            string = string.replace(field, "is" + StringUtils.capitalize(field));
        }
        return string;
    }
}
